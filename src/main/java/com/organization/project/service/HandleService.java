package com.organization.project.service;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.RoutingContext;

public class HandleService {
	private static final String CONTENT_TYPE[] = { "text/html", "application/json" };

	public void handlingResponse(RoutingContext routingContext) {
		handlingResponse(routingContext, null);
	}

	public void handlingResponse(RoutingContext routingContext, Object object) {
		HttpServerResponse response = routingContext.response();
		if (object instanceof JsonObject) {
			response.putHeader("content-type", CONTENT_TYPE[1]).end(((JsonObject) object).encodePrettily());
		} else if (object instanceof JsonArray) {
			response.putHeader("content-type", CONTENT_TYPE[1]).end(((JsonArray) object).encodePrettily());
		} else if (object instanceof String) {
			response.putHeader("content-type", CONTENT_TYPE[1]).end((String) object);
		} else if (object == null) {
			response.end();
		} else {
			response.end();
		}
	}

	public void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}

	public JsonObject getJsonObjectFromResultSet(ResultSet rs) {
		JsonObject jsObject = new JsonObject();
		jsObject.put("columNames", rs.getColumnNames());
		return jsObject;
	}
}
