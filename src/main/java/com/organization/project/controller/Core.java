package com.organization.project.controller;

import java.util.HashMap;
import java.util.Map;

import com.organization.project.util.Runner;
import com.organization.project.util.Text;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class Core extends AbstractVerticle {

	private static final int LISTENED_PORT = 88;
	private static final String CONTENT_TYPE[] = {"text/html","application/json"};
	private Router router;
	private Map<String, JsonObject> employees = new HashMap<>();

	public static void main(String[] args) {
		Runner.runExample(Core.class);
	}

	@Override
	public void start() {
		router = Router.router(vertx);

		setUpInitialData();
		router.route().handler(BodyHandler.create());
		router.get("/").handler(routingContext -> {
			routingContext.response().putHeader("content-type", CONTENT_TYPE[0]).end(getMessage());
		});
		router.get("/employees").handler(this::handleListEmployees);
		router.get("/employees/clear").handler(this::handleClearEmployees);
		router.get("/employees/setup").handler(this::handleSetUpInitialData);
		router.get("/employees/:employeeId").handler(this::handleGetEmployee);
		router.post("/employees/:employeeId").handler(this::handleAddEmployee);
		vertx.createHttpServer().requestHandler(router::accept).listen(LISTENED_PORT);
	}
	
	private void handleGetEmployee(RoutingContext routingContext) {
		String employeeId = routingContext.request().getParam("employeeId");
		HttpServerResponse response = routingContext.response();
		if (employeeId == null) {
			sendError(400, response);
		} else {
			JsonObject employee = employees.get(employeeId);
			if (employee == null) {
				sendError(404, response);
			} else {
				response.putHeader("content-type", CONTENT_TYPE[1]).end(employee.encodePrettily());
			}
		}
	}

	private void handleAddEmployee(RoutingContext routingContext) {
		String employeeId = routingContext.request().getParam("employeeId");
		HttpServerResponse response = routingContext.response();
		if (employeeId == null) {
			sendError(400, response);
		} else {
			JsonObject employee = routingContext.getBodyAsJson();
			if (employee == null) {
				sendError(400, response);
			} else {
				employees.put(employeeId, employee);
				response.end();
			}
		}
	}

	private void handleListEmployees(RoutingContext routingContext) {
		JsonArray arr = new JsonArray();
		employees.forEach((k, v) -> arr.add(v));
		routingContext.response().putHeader("content-type", CONTENT_TYPE[1]).end(arr.encodePrettily());
	}

	private void handleClearEmployees(RoutingContext routingContext) {
		employees = new HashMap<>();
		routingContext.response().end();
	}

	private void handleSetUpInitialData(RoutingContext routingContext) {
		setUpInitialData();
		routingContext.response().end();
	}

	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	private void setUpInitialData() {
		for (int i = 0; i <5; i++){
			addEmployee(new JsonObject()
					.put("id", "H1L00"+String.valueOf(i+1))
					.put("name", Text.getSaltString(5)));
		}
	}

	private void addEmployee(JsonObject employee) {
		employees.put(employee.getString("id"), employee);
	}

	public String getMessage(){
		return "<center>"
				+ "	<h1 style='color:blue;'>Vertext Example Project</h1>"
				+ "</center>";
	}

}
