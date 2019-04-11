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
@Table(name = "functions")
public class Function extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	public String name;

	public String status;
	
	//Relationships
	
	@ManyToMany(mappedBy = "functions") @JsonIgnore
	public List<Role> roles = new ArrayList<>();

	
	public static final Finder<Long, Function> find = new Finder<>(Function.class);
	
}
