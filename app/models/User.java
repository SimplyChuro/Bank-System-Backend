package models;

import java.util.*;
import javax.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.ebean.*;
import play.data.format.Formats;

@Entity
@Table(name = "users")
public class User extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	@Column(nullable = false)
	public String name;
    
    @Column(nullable = false)
	public String surname;
    
    @Column(unique = true, nullable = false)
	public String email;
    
    @JsonIgnore
	private String password;
    
    @JsonIgnore
	private String authToken;
    
	public String status;
	
	public Boolean verified;
	
	@Column(unique = true, nullable = false)
	public String bankAccount;
	
	public Double balance;
    
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="GMT")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date date;
	

    //Relationships

	@OneToOne(fetch = FetchType.LAZY)
    public Picture avatar; 
	
	@ManyToMany
	public List<Role> roles = new ArrayList<>();
	
	@ManyToMany
	public List<Transaction> transactions = new ArrayList<>();
	
	
	public static final Finder<Long, User> find = new Finder<>(User.class);
	
	public String createToken() {
        authToken = UUID.randomUUID().toString();
        save();
        return authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        save();
    }
   
    public Boolean hasAuthToken() {
    	if(authToken != null) {
    		return true;
    	}else {
    		return false;
    	}
    }

    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }

        try  {
            return find.query().where().eq("authToken", authToken).findOne();
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getAuthToken() {
    	return authToken;
    }
    
    public String getPassword() {
		return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());;
    }

}
