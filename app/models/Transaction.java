package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.format.Formats;

@Entity
@Table(name = "transactions")
public class Transaction extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	public Double amount;
	
	public String status;
	
	public String sender;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date date;
	
	
	//Relationships
	
	@ManyToMany(mappedBy = "transactions") @JsonIgnore
	public List<User> users = new ArrayList<>();
	
	@ManyToOne @JsonIgnore
	public Type type;
	

	public static final Finder<Long, Transaction> find = new Finder<>(Transaction.class);
	
}
