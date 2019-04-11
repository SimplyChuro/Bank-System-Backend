package services;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Function;
import models.Role;
import models.Transaction;
import models.Type;
import models.User;
import play.mvc.Http;

public class WithdrawalService {
	
	private Transaction transaction;
	private List<Transaction> transactions;
	private ObjectNode transactionNode;
	private User user;
	private Role role;
	private Type withdrawalType = Type.find.query().where().eq("name", "withdrawal").findOne();
	private Function createFunction = Function.find.query().where().eq("name", "create_withdrawal").findOne();
	private Function readFunction = Function.find.query().where().eq("name", "read_withdrawal").findOne();
	private Function updateFunction = Function.find.query().where().eq("name", "update_withdrawal").findOne();
	private Function deleteFunction = Function.find.query().where().eq("name", "delete_withdrawal").findOne();
	
	public User getUser(Http.Request request) {
		Long checker = Long.parseLong(request.session().getOptional("id").get());
	    return User.find.byId(checker);
	}
	
	public Transaction get(Http.Request request, Long id) {
		if(hasPermission(request, id, createFunction)) {
			return Transaction.find.byId(id);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public List<Transaction> getAll(Http.Request request, Integer min, Integer max) {
		user = getUser(request);
		if(hasPermission(request, null, readFunction)) {
			return Transaction.find.query()
					.where()
					.eq("types.name", "withdrawal")
					.orderBy("date desc")
					.setFirstRow(min)
			        .setMaxRows(max)
			        .findList();
		} else {
			return Transaction.find.query()
					.where()
					.eq("types.name", "withdrawal").and().eq("users.id", user.id)
					.orderBy("date desc")
					.setFirstRow(min)
			        .setMaxRows(max)
			        .findList();
		}
	}
	
	public Integer getSize(Http.Request request) {
		user = getUser(request);
		if(hasPermission(request, null, readFunction)) {
			return Transaction.find.all().size();
		} else {
			return Transaction.find.query().where().eq("types.name", "withdrawal").and().eq("users.id", user.id).findList().size();
		}
	}
	
	public Transaction create(Http.Request request) {
		if(isUserRole(request)) {
			transaction = new Transaction();
			user = getUser(request);
			transactionNode = (ObjectNode) request.body().asJson().get("withdrawal");
			
			transaction.amount = transactionNode.findValue("amount").asDouble();
			transaction.date = new Date();
			transaction.status = "active";
			transaction.type = withdrawalType;
			transaction.users.add(user);
			transaction.save();
			
			user.balance -= transaction.amount;
			user.update();
			
			return transaction;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Transaction update(Http.Request request, Long id) {
		if(hasPermission(request, id, updateFunction)) { 
			transaction = Transaction.find.byId(id);
//			transactionNode = (ObjectNode) request.body().asJson().get("withdrawal");
//			transaction.amount = transactionNode.findValue("amount").asDouble();	
//			transaction.update();
			
			return transaction;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Transaction delete(Http.Request request, Long id) {
		if(hasPermission(request, id, deleteFunction)) { 
			transaction = Transaction.find.byId(id);
			transaction.status = "deleted";
			transaction.update();
		
			return transaction;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Boolean hasPermission(Http.Request request, Long id, Function function) {
		user = getUser(request);
		role = user.roles.get(0);
		
		if(role.name.equals("user")) {
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
			if(role.functions.contains(function)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public Boolean isUserRole(Http.Request request) {
		user = getUser(request);
		role = user.roles.get(0);
		if(role.name.equals("user")) {
			return true;
		} else {
			return false;
		}
	}

}