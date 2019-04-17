package controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigFactory;

import models.Picture;
import models.User;
import play.libs.Json;
import play.libs.Files.TemporaryFile;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import play.Environment;
import services.PictureFilter;
import services.Secured;

public class PictureController  extends Controller {

	private PictureFilter filter = new PictureFilter();
	private HttpExecutionContext httpExecutionContext;
	private JsonNode jsonNode;
	private Picture picture;
	private List<Picture> pictures;
	private User user;
	private int size;
	private int min, max;
	private static final int itemsPerPage = 10;
	private static final String IMAGE_URL = "custom.settings.host.imageUrl";
	
	@Inject
	private Environment environment;
	
	@Inject
    public PictureController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get User
	public User getUser(Http.Request request) {
		String token = request.session().getOptional("auth_token").get();
	    return User.findByAuthToken(token);
	}
	
	//Get picture
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> get(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				picture = Picture.find.byId(id);
				return ok(Json.toJson(picture));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get pictures
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll(Http.Request request, Integer page) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				min = itemsPerPage * page - itemsPerPage;
				max = itemsPerPage * page;
				pictures = Picture.find.query()
						.where()
						.orderBy("published desc")
						.setFirstRow(min)
				        .setMaxRows(max)
				        .findList();
				return ok(Json.toJson(pictures));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());		
	}
	
	//Get Picture size
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getSize(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = Picture.find.all().size();
				return ok(Json.toJson(size));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get Picture Page size
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getPageSize(Http.Request request, Integer pageSize) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = Picture.find.all().size();
				int pageChecker = ((int) (size/10));
				if(size > (pageChecker/10)) {
					pageChecker++;
				}
				return ok(Json.toJson(pageChecker));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}

	//Create picture
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
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
				            
				            File newFile = new File(environment.rootPath().toString() + "//public//images//" + customName);
			                file.moveFileTo(newFile);
			            
			                picture.save();

				            user = getUser(request);
				            
				            if(user.avatar != null) {
								Files.deleteIfExists(Paths.get(environment.rootPath().toString() + "//public//images//" + user.avatar.name));
								user.avatar.status = "deleted";
								user.avatar.update();
								
								user.avatar = picture;
								user.update();
							} else {
								user.avatar = picture;
								user.update();
							}
			                
				            return ok(Json.toJson(picture));	
		                } else {
		                    return badRequest();    
		                }
		            } else {
		            	return forbidden();
		            }
		        } else {
		            return badRequest();
		        }
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update picture -- inactive
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {	
			try {
				picture = Picture.find.byId(id);
				return ok(Json.toJson(picture));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete picture 
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = getUser(request);
				if(user.avatar.id == id) {
					picture = Picture.find.byId(id);
					Files.deleteIfExists(Paths.get(environment.rootPath().toString() + "//public//images//" + picture.name));
					
					picture.status = "deleted";
					picture.update();
					
					user.avatar = null;
					user.update();
					
					return ok(Json.toJson(""));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
}
