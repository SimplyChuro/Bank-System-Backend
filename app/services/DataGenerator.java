package services;

import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.ConfigFactory;

import models.Permission;
import models.Role;
import models.Type;
import models.User;

public class DataGenerator {

	private static final String ADMIN_MAIL = "custom.settings.admin.mail";
	private static final String ADMIN_PASSWORD = "custom.settings.admin.password";
	
	public Role role;
	public Permission permission;
	public Type type;
	public User admin;
	public List<User> users;
	public List<Role> roles;
	public List<Type> types;
	public List<Permission> permissions;
	public List<Permission> adminPermissions = new ArrayList<>();
	public List<Permission> moderatorPermissions = new ArrayList<>();
	public List<Permission> staffPermissions = new ArrayList<>();
	
	
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
		role.permissions = adminPermissions;
		role.save();
		
		role = new Role();
		role.status = "active";
		role.name = "moderator";
		role.permissions = moderatorPermissions;
		role.save();
		
		role = new Role();
		role.status = "active";
		role.name = "staff";
		role.permissions = staffPermissions;
		role.save();
		
		role = new Role();
		role.status = "active";
		role.name = "user";
		role.save();
	
	}
	
	public void generateFunctions() {
		
		//User permissions		
		permission = new Permission();
		permission.status = "active";
		permission.name = "create_user";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "read_user";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		staffPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "update_user";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "delete_user";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		//Deposit permissions		
		permission = new Permission();
		permission.status = "active";
		permission.name = "create_deposit";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "read_deposit";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		staffPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "update_deposit";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "delete_deposit";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		//Payment permissions		
		permission = new Permission();
		permission.status = "active";
		permission.name = "create_payment";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "read_payment";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		staffPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "update_payment";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "delete_payment";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		//Withdrawal permissions		
		permission = new Permission();
		permission.status = "active";
		permission.name = "create_withdrawal";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "read_withdrawal";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		staffPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "update_withdrawal";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "delete_withdrawal";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
			
		//Picture permissions		
		permission = new Permission();
		permission.status = "active";
		permission.name = "create_picture";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "read_picture";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "update_picture";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "delete_picture";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		//Staff permissions		
		permission = new Permission();
		permission.status = "active";
		permission.name = "create_staff";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "read_staff";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "update_staff";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "delete_staff";
		permission.save();
		adminPermissions.add(permission);
		moderatorPermissions.add(permission);
		
		//Moderator permissions		
		permission = new Permission();
		permission.status = "active";
		permission.name = "create_moderator";
		permission.save();
		adminPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "read_moderator";
		permission.save();
		adminPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "update_moderator";
		permission.save();
		adminPermissions.add(permission);
		
		permission = new Permission();
		permission.status = "active";
		permission.name = "delete_moderator";
		permission.save();
		adminPermissions.add(permission);
			
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
		permissions = Permission.find.all();
		if(permissions.isEmpty()) {
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