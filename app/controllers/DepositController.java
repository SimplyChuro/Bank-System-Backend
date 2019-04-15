package controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import models.Transaction;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.DepositService;
import services.Secured;

public class DepositController  extends Controller {

	private HttpExecutionContext httpExecutionContext;
	private Transaction deposit;
	private DepositService depositService = new DepositService();
	private List<Transaction> deposits;
	private int size;
	private int min, max;
	private static final int itemsPerPage = 10;
	
	@Inject
    public DepositController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get deposit
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> get(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				deposit = depositService.get(request, id);
				return ok(Json.toJson(deposit));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get deposit
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll(Http.Request request, Integer page) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				min = itemsPerPage * page - itemsPerPage;
				max = itemsPerPage * page;
				deposits = depositService.getAll(request, min, max);
				return ok(Json.toJson(deposits));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());		
	}
	
	//Get deposit size
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getSize(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = depositService.getSize(request);
				return ok(Json.toJson(size));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get deposit Page size
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> getPageSize(Http.Request request, Integer pageSize) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = depositService.getSize(request);
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

	//Create deposit
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> create(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				deposit = depositService.create(request);
				return ok(Json.toJson(deposit));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update deposit
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {	
			try {
				deposit = depositService.update(request, id);
				return ok(Json.toJson(deposit));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete deposit 
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				deposit = depositService.delete(request, id);
				return ok(Json.toJson(""));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
}