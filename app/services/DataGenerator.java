package services;

import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.ConfigFactory;

import models.Function;
import models.Role;
import models.Type;
import models.User;

public class DataGenerator {

	private static final String ADMIN_MAIL = "custom.settings.admin.mail";
	private static final String ADMIN_PASSWORD = "custom.settings.admin.password";
	
	public Role role;
	public Function function;
	public Type type;
	public User admin;
	public List<User> users;
	public List<Role> roles;
	public List<Type> types;
	public List<Function> functions;
	public List<Function> adminFunctions = new ArrayList<>();
	public List<Function> moderatorFunctions = new ArrayList<>();
	public List<Function> staffFunctions = new ArrayList<>();
	
	
	public void generate() {
		
		if(noFunctions()) {
			generateFunctions();
		}
		
		if (noRoles()) {
			generateRoles();
		}
		
		if(noTypes()) {
			generateTypes();	
		}
		
		if(noAdmin()) {
			generateAdmin();	
		}
		
		
	}
	
	public void generateAdmin() {
		if (ConfigFactory.load().hasPath(ADMIN_MAIL) && ConfigFactory.load().hasPath(ADMIN_PASSWORD)) {
			admin = new User();
			admin.name = "Administrator";
			admin.surname = "Administrator";
			admin.email = ConfigFactory.load().getString(ADMIN_MAIL);
			admin.setPassword(ConfigFactory.load().getString(ADMIN_PASSWORD));
			admin.bankAccount = "_Administrator";
			
			role = Role.find.query().where().eq("name", "admin").findOne();
			admin.roles.add(role);
			admin.save();
		}
	}
	
	
	public void generateRoles() {
		role = new Role();
		role.status = "active";
		role.name = "admin";
		role.functions = adminFunctions;
		role.save();
		
		role = new Role();
		role.status = "active";
		role.name = "moderator";
		role.functions = moderatorFunctions;
		role.save();
		
		role = new Role();
		role.status = "active";
		role.name = "staff";
		role.functions = staffFunctions;
		role.save();
		
		role = new Role();
		role.status = "active";
		role.name = "user";
		role.save();
	
	}
	
	public void generateFunctions() {
		
		//User functions		
		function = new Function();
		function.status = "active";
		function.name = "create_user";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "read_user";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		staffFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "update_user";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "delete_user";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		//Deposit functions		
		function = new Function();
		function.status = "active";
		function.name = "create_deposit";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "read_deposit";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		staffFunctions.add(function);
		
		function.name = "update_deposit";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "delete_deposit";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		//Payment function		
		function = new Function();
		function.status = "active";
		function.name = "create_payment";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "read_payment";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		staffFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "update_payment";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "delete_payment";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		//Withdrawal function		
		function = new Function();
		function.status = "active";
		function.name = "create_withdrawal";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "read_withdrawal";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		staffFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "update_withdrawal";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "delete_withdrawal";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		//Staff function		
		function = new Function();
		function.status = "active";
		function.name = "create_staff";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "read_staff";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "update_staff";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "delete_staff";
		function.save();
		adminFunctions.add(function);
		moderatorFunctions.add(function);
		
		//Moderator function		
		function = new Function();
		function.status = "active";
		function.name = "create_moderator";
		function.save();
		adminFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "read_moderator";
		function.save();
		adminFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "update_moderator";
		function.save();
		adminFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "delete_moderator";
		function.save();
		adminFunctions.add(function);
		
		//Admin function		
		function = new Function();
		function.status = "active";
		function.name = "create_admin";
		function.save();
		adminFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "read_admin";
		function.save();
		adminFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "update_admin";
		function.save();
		adminFunctions.add(function);
		
		function = new Function();
		function.status = "active";
		function.name = "delete_admin";
		function.save();
		adminFunctions.add(function);
			
	}
	
	public void generateTypes() {
		type = new Type();
		type.status = "active";
		type.name = "deposit";
		type.save();
		
		type = new Type();
		type.status = "active";
		type.name = "payment";
		type.save();
		
		type = new Type();
		type.status = "active";
		type.name = "withdrawal";
		type.save();
	}
	
	
	public Boolean missing() {
		if(noAdmin() || noRoles() || noFunctions() || noTypes()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean noAdmin() {
		role = Role.find.query().where().eq("name", "admin").findOne();
		users = User.find.query().where().eq("roles.id", role.id).findList();
		if(users.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean noRoles() {
		roles = Role.find.all();
		if(roles.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean noFunctions() {
		functions = Function.find.all();
		if(functions.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean noTypes() {
		types = Type.find.all();
		if(types.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

}