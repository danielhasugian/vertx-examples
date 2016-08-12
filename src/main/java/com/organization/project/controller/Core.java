package com.organization.project.controller;

import com.organization.project.util.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Core extends AbstractVerticle {

	private static final int LISTENED_PORT = 88;
	private static final String CONTENT_TYPE[] = {"text/html","application/json"};
	private Router router;
	private EmployeController employeController;
	
	public static void main(String[] args) {
		Runner.runExample(Core.class);
	}

	@Override
	public void start() {
		router = Router.router(vertx);
		employeController = new EmployeController();
		
		router.route().handler(BodyHandler.create());
		router.get("/").handler(routingContext -> {
			routingContext.response().putHeader("content-type", CONTENT_TYPE[0]).end(getMessage());
		});
		
		//initial postgreSql
		
		//Setup EmployeController
		employeController.setUpInitialData();
		router.get("/employees").handler(employeController::handleListEmployees);
		router.post("/employees/clear").handler(employeController::handleClearEmployees);
		router.post("/employees/setup").handler(employeController::handleSetUpInitialData);
		router.post("/employees/:employeeId").handler(employeController::handleGetEmployee);
		router.post("/employees/:employeeId").handler(employeController::handleAddEmployee);
		vertx.createHttpServer().requestHandler(router::accept).listen(LISTENED_PORT);
	}

	public String getMessage(){
		return "<center><h1 style='color:blue;'>Vertext Example Project</h1></center><br/>"
				+ "<center><h2>Under Construction</h2></center>";
	}

}
