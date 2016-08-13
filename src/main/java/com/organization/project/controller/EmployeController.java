package com.organization.project.controller;

import java.util.HashMap;
import java.util.Map;

import com.organization.project.service.HandleService;
import com.organization.project.util.Text;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class EmployeController {

	private Map<String, JsonObject> employees = new HashMap<>();
	private HandleService handleService = new HandleService();

	protected void handleGetEmployee(RoutingContext routingContext) {
		String employeeId = routingContext.request().getParam("employeeId");
		HttpServerResponse response = routingContext.response();
		if (employeeId == null) {
			handleService.sendError(400, response);
		} else {
			JsonObject employee = employees.get(employeeId);
			if (employee == null) {
				handleService.sendError(404, response);
			} else {
				handleService.handlingResponse(routingContext, employee);
			}
		}
	}

	protected void handleAddEmployee(RoutingContext routingContext) {
		String employeeId = routingContext.request().getParam("employeeId");
		HttpServerResponse response = routingContext.response();
		if (employeeId == null) {
			handleService.sendError(400, response);
		} else {
			JsonObject employee = routingContext.getBodyAsJson();
			if (employee == null) {
				handleService.sendError(400, response);
			} else {
				employees.put(employeeId, employee);
				handleService.handlingResponse(routingContext, employees);
			}
		}
	}

	protected void handleListEmployees(RoutingContext routingContext) {
		JsonArray arr = new JsonArray();
		employees.forEach((k, v) -> arr.add(v));
		handleService.handlingResponse(routingContext, arr);
	}

	protected void handleClearEmployees(RoutingContext routingContext) {
		employees = new HashMap<>();
		handleService.handlingResponse(routingContext);
	}

	protected void handleSetUpInitialData(RoutingContext routingContext) {
		setUpInitialData();
		handleService.handlingResponse(routingContext);
	}

	protected void setUpInitialData() {
		for (int i = 0; i < 5; i++) {
			addEmployee(new JsonObject().put("id", "H1L00" + String.valueOf(i + 1)).put("name", Text.getSaltString(5)));
		}
	}

	protected void addEmployee(JsonObject employee) {
		employees.put(employee.getString("id"), employee);
	}

}
