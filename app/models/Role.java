package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name = "roles")
public class Role extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	public String status;
	
	public String name;
	
	//Relationships	
	
	@ManyToMany(mappedBy = "roles") @JsonIgnore
	public List<User> users = new ArrayList<>();
	
	@ManyToMany @JsonIgnore
	public List<Permission> permissions = new ArrayList<>();
	

	public static final Finder<Long, Role> find = new Finder<>(Role.class);
	
}
