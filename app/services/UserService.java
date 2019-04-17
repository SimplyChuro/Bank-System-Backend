package services;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Permission;
import models.Role;
import models.User;
import play.mvc.Http;

public class UserService {
	
	private User user, userChecker;;
	private ObjectNode userNode;
	private ArrayNode rolesNode;
	private Role role;
	private Role userRole = Role.find.query().where().eq("name", "user").findOne();
	private Role staffRole = Role.find.query().where().eq("name", "staff").findOne();
	private Role moderatorRole = Role.find.query().where().eq("name", "moderator").findOne();
	private Permission createPermission = Permission.find.query().where().eq("name", "create_user").findOne();
	private Permission readPermission = Permission.find.query().where().eq("name", "read_user").findOne();
	private Permission updatePermission = Permission.find.query().where().eq("name", "update_user").findOne();
	private Permission deletePermission = Permission.find.query().where().eq("name", "delete_user").findOne();
	private Permission updateStaffPermission = Permission.find.query().where().eq("name", "update_staff").findOne();
	private Permission updateModeratorPermission = Permission.find.query().where().eq("name", "update_moderator").findOne();
	
	public User getUser(Http.Request request) {
		String token = request.session().getOptional("auth_token").get();
	    return User.findByAuthToken(token);
	}
	
	public User get(Http.Request request, Long id) {
		if(hasPermission(request, id, readPermission)) {
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
		if(hasPermission(request, null, readPermission)) {
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
		user.roles.add(userRole);
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
			
			if(isAdmin(request) || isModerator(request)) {
				rolesNode = (ArrayNode)userNode.findValue("roles");
				System.out.println(rolesNode);
				if(!rolesNode.get(0).findValue("name").asText().equals(user.roles.get(0).name) && !user.roles.get(0).name.equals("admin")) {
					if(rolesNode.get(0).findValue("name").asText().equals("moderator")) {
						if(hasPermission(request, id, updateModeratorPermission)) {
							user.roles.remove(0);
							user.roles.add(moderatorRole);
						}
					} else if(rolesNode.get(0).findValue("name").asText().equals("staff")) {
						if(hasPermission(request, id, updateStaffPermission)) {
							user.roles.remove(0);
							user.roles.add(staffRole);
						}
					} else {
						user.roles.remove(0);
						user.roles.add(userRole);
					}
				}
			}
			
			if(isAdmin(request) || isModerator(request)) {
				if(user.email != userNode.findValue("email").asText()) {
					user.email = userNode.findValue("email").asText();
				}
			}
			
			if(!isAdmin(request) && !userNode.findValue("password").asText().equals("null")) {
				user.setPassword(userNode.findValue("password").asText());
			}
			
			user.bankAccount = userNode.findValue("bankAccount").asText();
			
			user.update();
			return user;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public User delete(Http.Request request, Long id) {
		if(hasPermission(request, id, deletePermission)) { 
			user = getUser(request);
			
			user.status = "deactivated";
			user.update();
			return user;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Boolean hasPermission(Http.Request request, Long id, Permission function) {
		userChecker = getUser(request);
		if(isUser(request)) {
			try {
				if(userChecker.id == id) {
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