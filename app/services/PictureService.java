package services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;

import models.Permission;
import models.Picture;
import models.Role;
import models.Transaction;
import models.Type;
import models.User;
import play.Environment;
import play.libs.Json;
import play.libs.Files.TemporaryFile;
import play.mvc.Http;

public class PictureService {
	
	private Picture picture;
	private List<Picture> pictures;
	private ObjectNode pictureNode;
	private User user, userChecker;
	private Role role;
	private Permission createPermission = Permission.find.query().where().eq("name", "create_picture").findOne();
	private Permission readPermission = Permission.find.query().where().eq("name", "read_picture").findOne();
	private Permission updatePermission = Permission.find.query().where().eq("name", "update_picture").findOne();
	private Permission deletePermission = Permission.find.query().where().eq("name", "delete_picture").findOne();
	private PictureFilter filter = new PictureFilter();
	private static final String IMAGE_URL = "custom.settings.host.imageUrl";
	
	@Inject
	private Environment environment;
	
	public User getUser(Http.Request request) {
		String token = request.session().getOptional("auth_token").get();
	    return User.findByAuthToken(token);
	}
	
	public Picture get(Http.Request request, Long id) {
		if(hasPermission(request, id, readPermission)) {
			return Picture.find.byId(id);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public List<Picture> getAll(Http.Request request, Integer min, Integer max) {
		user = getUser(request);
		if(hasPermission(request, null, readPermission)) {
			return Picture.find.query()
					.where()
					.orderBy("date desc")
					.setFirstRow(min)
			        .setMaxRows(max)
			        .findList();
		} else {
			return Picture.find.query()
					.where()
					.eq("user.id", user.id)
					.orderBy("date desc")
					.setFirstRow(min)
			        .setMaxRows(max)
			        .findList();
		}
	}
	
	public Integer getSize(Http.Request request) {
		user = getUser(request);
		if(hasPermission(request, null, readPermission)) {
			return Picture.find.all().size();
		} else {
			return picture.find.query().where().eq("user.id", user.id).findList().size();
		}
	}
	
	public Picture create(Http.Request request) {
        System.out.println(environment.rootPath().toString() + "//public//images//" );
		if(isUser(request)) {
			Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
	        Http.MultipartFormData.FilePart<TemporaryFile> pictureFile = body.getFile("picture");
	        
	        if (pictureFile != null) {
	            String fileName = pictureFile.getFilename();
	            long fileSize = pictureFile.getFileSize();
	            String contentType = pictureFile.getContentType();

	            if(filter.isValidPicture(fileName, contentType, fileSize)) {
	            	if (ConfigFactory.load().hasPath(IMAGE_URL)) {
	                    TemporaryFile file = pictureFile.getRef();
			            Date currentDate = new Date();
			            String customName = currentDate.getTime() + "_" + fileName;
		                
			            picture = new Picture();
			            
			            picture.name = customName;
			            picture.published = currentDate;
			            picture.type = contentType;
			            picture.url = ConfigFactory.load().getString(IMAGE_URL) + customName;
			            System.out.println(environment.rootPath().toString() + "//public//images//" + customName);
			            
			            File newFile = new File(environment.rootPath().toString() + "//public//images//" + customName);
		                file.moveFileTo(newFile);
		            
		                picture.save();

			            user = getUser(request);
			            
			            if(user.avatar != null) {
			            	try {
								Files.deleteIfExists(Paths.get(environment.rootPath().toString() + "//public//images//" + user.avatar.name));
								user.avatar.status = "deleted";
								user.avatar.update();
								
								user.avatar = picture;
								user.update();
			            	} catch(Exception e) {
			        			throw new IllegalArgumentException();  
			            	}
						} else {
							user.avatar = picture;
							user.update();
						}
		    
			            return picture;	
	                } else {
	        			throw new IllegalArgumentException();  
	                }
	            } else {
	    			throw new IllegalArgumentException();
	            }
	        } else {
				throw new IllegalArgumentException();
	        }
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Picture update(Http.Request request, Long id) {
		if(hasPermission(request, id, updatePermission)) { 
			picture = Picture.find.byId(id);
			return picture;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Picture delete(Http.Request request, Long id) {
		if(hasPermission(request, id, deletePermission)) { 
			try {
				picture = Picture.find.byId(id);
				Files.deleteIfExists(Paths.get(environment.rootPath().toString() + "//public//images//" + picture.name));
				
				picture.status = "deleted";
				picture.update();
				
				user.avatar = null;
				user.update();
				
				return picture;
			} catch(Exception e) {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Boolean hasPermission(Http.Request request, Long id, Permission function) {
		userChecker = getUser(request);
		if(isUser(request)) {
			try {
				pictures = Picture.find.query().where().eq("user.id", userChecker.id).and().eq("id", id).findList();
				if(picture == null) {
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