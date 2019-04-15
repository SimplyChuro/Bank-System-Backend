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
import services.Secured;
import services.WithdrawalService;

public class WithdrawalController  extends Controller {

	private HttpExecutionContext httpExecutionContext;
	private Transaction withdrawal;
	private WithdrawalService withdrawalService = new WithdrawalService();
	private List<Transaction> withdrawals;
	private int size;
	private int min, max;
	private static final int itemsPerPage = 10;
	
	@Inject
    public WithdrawalController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get withdrawal
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> get(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				withdrawal = withdrawalService.get(request, id);
				return ok(Json.toJson(withdrawal));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get withdrawal
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll(Http.Request request, Integer page) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				min = itemsPerPage * page - itemsPerPage;
				max = itemsPerPage * page;
				withdrawals = withdrawalService.getAll(request, min, max);
				return ok(Json.toJson(withdrawals));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());		
	}
	
	//Get withdrawal size
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getSize(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = withdrawalService.getSize(request);
				return ok(Json.toJson(size));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get withdrawal Page size
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> getPageSize(Http.Request request, Integer pageSize) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = withdrawalService.getSize(request);
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

	//Create withdrawal
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> create(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				withdrawal = withdrawalService.create(request);
				return ok(Json.toJson(withdrawal));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update withdrawal
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {	
			try {
				withdrawal = withdrawalService.update(request, id);
				return ok(Json.toJson(withdrawal));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete withdrawal 
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				withdrawal = withdrawalService.delete(request, id);
				return ok(Json.toJson(""));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
}