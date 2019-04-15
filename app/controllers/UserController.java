package controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;


import models.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.Secured;
import services.UserService;

public class UserController  extends Controller {

	private HttpExecutionContext httpExecutionContext;
	private User user;
	private UserService userService = new UserService();
	private List<User> users;
	private int size;
	private int min, max;
	private static final int itemsPerPage = 10;
	
	@Inject
    public UserController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get user
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> get(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = userService.get(request, id);
				return ok(Json.toJson(user));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get users
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll(Http.Request request, Integer page) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				min = itemsPerPage * page - itemsPerPage;
				max = itemsPerPage * page;
				users = userService.getAll(request, min, max);
				return ok(Json.toJson(users));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());		
	}
	
	//Get user size
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getSize(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = userService.getSize(request);
				return ok(Json.toJson(size));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get users Page size
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> getPageSize(Http.Request request, Integer pageSize) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = userService.getSize(request);
				if(pageSize > 0) {
					int pageChecker = ((int) (size/pageSize));
					if(size > (pageChecker/pageSize)) {
						pageChecker++;
					}
					return ok(Json.toJson(pageChecker));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}

	//Create user
	public CompletionStage<Result> create(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = userService.create(request);
				return ok(Json.toJson(user));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update user
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {	
			try {
				user = userService.update(request, id);
				return ok(Json.toJson(user));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete user 
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = userService.delete(request, id);
				return ok(Json.toJson(""));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
}