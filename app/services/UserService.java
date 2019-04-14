package services;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Permission;
import models.Role;
import models.Transaction;
import models.Type;
import models.User;
import play.mvc.Http;

public class UserService {
	
	private User user;
	private List<User> users;
	private ObjectNode userNode;
	private Role role;
	private Permission createPermission = Permission.find.query().where().eq("name", "create_user").findOne();
	private Permission readPermission = Permission.find.query().where().eq("name", "read_user").findOne();
	private Permission updatePermission = Permission.find.query().where().eq("name", "update_user").findOne();
	private Permission deletePermission = Permission.find.query().where().eq("name", "delete_user").findOne();
	
	public User getUser(Http.Request request) {
		Long checker = Long.parseLong(request.session().getOptional("id").get());
	    return User.find.byId(checker);
	}
	
	public User get(Http.Request request, Long id) {
		if(hasPermission(request, id, createPermission)) {
			return User.find.byId(id);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public List<User> getAll(Http.Request request, Integer min, Integer max) {
		if(hasPermission(request, null, readPermission)) {
			return User.find.query()
					.where()
					.orderBy("date desc")
					.setFirstRow(min)
			        .setMaxRows(max)
			        .findList();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Integer getSize(Http.Request request) {
		if(hasPermission(request, null, deletePermission)) {
			return User.find.all().size();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public User create(Http.Request request) {
		user = new User();
		userNode = (ObjectNode) request.body().asJson().get("user");
		
		user.name = userNode.findValue("name").asText();
		user.surname = userNode.findValue("surname").asText();
		user.email = userNode.findValue("email").asText();
		user.setPassword(userNode.findValue("password").asText());
		user.date = new Date();
		user.bankAccount = user.name + "_" + user.surname + "_" + user.date.getTime();
		user.balance = 0.0;
		user.status = "active";
		
		user.save();
		return user;
	}
	
	public User update(Http.Request request, Long id) {
		if(hasPermission(request, id, updatePermission)) { 
			user = User.find.byId(id);
			userNode = (ObjectNode) request.body().asJson().get("user");
			
			user.name = userNode.findValue("name").asText();
			user.surname = userNode.findValue("surname").asText();
			user.email = userNode.findValue("email").asText();
			user.setPassword(userNode.findValue("password").asText());
			user.bankAccount = userNode.findValue("bankAccount").asText();
			
			user.update();
			return user;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public User delete(Http.Request request, Long id) {
		if(hasPermission(request, id, updatePermission)) { 
			user = getUser(request);
			
			user.status = "deactivated";
			user.update();
			return user;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Boolean hasPermission(Http.Request request, Long id, Permission function) {
		user = getUser(request);
		role = user.roles.get(0);
		
		if(role.name.equals("user")) {
			try {
				if(user.id == id) {
					return true;
				} else {
					return false;
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
