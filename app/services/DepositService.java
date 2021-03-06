package services;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Permission;
import models.Role;
import models.Transaction;
import models.Type;
import models.User;
import play.mvc.Http;

public class DepositService {
	
	private Transaction transaction;
	private List<Transaction> transactions;
	private ObjectNode transactionNode;
	private User user, userChecker;
	private Role role;
	private Type depositType = Type.find.query().where().eq("name", "deposit").findOne();
	private Permission createPermission = Permission.find.query().where().eq("name", "create_deposit").findOne();
	private Permission readPermission = Permission.find.query().where().eq("name", "read_deposit").findOne();
	private Permission updatePermission = Permission.find.query().where().eq("name", "update_deposit").findOne();
	private Permission deletePermission = Permission.find.query().where().eq("name", "delete_deposit").findOne();
	private DecimalFormat decimalFormat = new DecimalFormat("#.##");
	private Double amount;
	
	public User getUser(Http.Request request) {
		String token = request.session().getOptional("auth_token").get();
	    return User.findByAuthToken(token);
	}
	
	public Transaction get(Http.Request request, Long id) {
		if(hasPermission(request, id, readPermission)) {
			return Transaction.find.byId(id);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public List<Transaction> getAll(Http.Request request, Integer min, Integer max) {
		user = getUser(request);
		if(hasPermission(request, null, readPermission)) {
			return Transaction.find.query()
					.where()
					.eq("type.name", "deposit")
					.orderBy("date desc")
					.setFirstRow(min)
			        .setMaxRows(max)
			        .findList();
		} else {
			return Transaction.find.query()
					.where()
					.eq("type.name", "deposit").and().eq("users.id", user.id)
					.orderBy("date desc")
					.setFirstRow(min)
			        .setMaxRows(max)
			        .findList();
		}
	}
	
	public Integer getSize(Http.Request request) {
		user = getUser(request);
		if(hasPermission(request, null, readPermission)) {
			return Transaction.find.query().where().eq("type.name", "deposit").findList().size();
		} else {
			return Transaction.find.query().where().eq("type.name", "deposit").and().eq("users.id", user.id).findList().size();
		}
	}
	
	public Transaction create(Http.Request request) {
		if(isUser(request)) {
			transaction = new Transaction();
			user = getUser(request);
			transactionNode = (ObjectNode) request.body().asJson().get("deposit");
			
			amount = Double.parseDouble(decimalFormat.format(transactionNode.findValue("amount").asDouble()));
			if(amount > 0.00) {
				transaction.amount = Double.parseDouble(decimalFormat.format(transactionNode.findValue("amount").asDouble()));
				transaction.date = new Date();
				transaction.status = "active";
				transaction.type = depositType;
				transaction.sender = user.id.toString();
				transaction.users.add(user);
				transaction.save();
				
				user.balance += transaction.amount;
				user.update();
				
				return transaction;
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Transaction update(Http.Request request, Long id) {
		if(hasPermission(request, id, updatePermission)) { 
			transaction = Transaction.find.byId(id);
//			transactionNode = (ObjectNode) request.body().asJson().get("deposit");
//			transaction.amount = transactionNode.findValue("amount").asDouble();	
//			transaction.update();
			
			return transaction;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Transaction delete(Http.Request request, Long id) {
		if(hasPermission(request, id, deletePermission)) { 
			transaction = Transaction.find.byId(id);
			transaction.status = "deleted";
			transaction.update();
		
			return transaction;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Boolean hasPermission(Http.Request request, Long id, Permission function) {
		userChecker = getUser(request);
		if(isUser(request)) {
			try {
				transactions = Transaction.find.query().where().eq("users.id", user.id).and().eq("id", id).findList();
				if(transactions.isEmpty()) {
					return false;
				} else {
					return true;
				}
			} catch(Exception e) {
				return false;
			}
		} else {
			if(role.permissions.contains(function)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public Boolean isUser(Http.Request request) {
		userChecker = getUser(request);
		role = userChecker.roles.get(0);
		if(role.name.equals("user")) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean isStaff(Http.Request request) {
		userChecker = getUser(request);
		role = userChecker.roles.get(0);
		if(role.name.equals("staff")) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean isModerator(Http.Request request) {
		userChecker = getUser(request);
		role = userChecker.roles.get(0);
		if(role.name.equals("moderator")) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean isAdmin(Http.Request request) {
		userChecker = getUser(request);
		role = userChecker.roles.get(0);
		if(role.name.equals("admin")) {
			return true;
		} else {
			return false;
		}
	}

}