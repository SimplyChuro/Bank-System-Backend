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
import services.PaymentService;
import services.Secured;

public class PaymentController  extends Controller {

	private HttpExecutionContext httpExecutionContext;
	private Transaction payment;
	private PaymentService paymentService = new PaymentService();
	private List<Transaction> payments;
	private int size;
	private int min, max;
	private static final int itemsPerPage = 10;
	
	@Inject
    public PaymentController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get payment
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> get(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				payment = paymentService.get(request, id);
				return ok(Json.toJson(payment));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get payment
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll(Http.Request request, Integer page) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				min = itemsPerPage * page - itemsPerPage;
				max = itemsPerPage * page;
				payments = paymentService.getAll(request, min, max);
				return ok(Json.toJson(payments));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());		
	}
	
	//Get payment size
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getSize(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = paymentService.getSize(request);
				return ok(Json.toJson(size));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get payment Page size
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> getPageSize(Http.Request request, Integer pageSize) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = paymentService.getSize(request);
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

	//Create payment
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> create(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				payment = paymentService.create(request);
				return ok(Json.toJson(payment));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update payment
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {	
			try {
				payment = paymentService.update(request, id);
				return ok(Json.toJson(payment));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete payment 
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				payment = paymentService.delete(request, id);
				return ok(Json.toJson(""));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
}