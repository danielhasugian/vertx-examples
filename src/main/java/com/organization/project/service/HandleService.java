package com.organization.project.service;

import com.organization.project.type.ContentTypeHeader;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.RoutingContext;

public class HandleService {

	public void handlingResponse(RoutingContext routingContext) {
		handlingResponse(routingContext, null);
	}

	public void handlingResponse(RoutingContext routingContext, Object object) {
		HttpServerResponse response = routingContext.response();
		if (object instanceof JsonObject) {
			response.putHeader("content-type", ContentTypeHeader.APPLICATIONJSON.getMimeType()).end(((JsonObject) object).encodePrettily());
		} else if (object instanceof JsonArray) {
			response.putHeader("content-type", ContentTypeHeader.APPLICATIONJSON.getMimeType()).end(((JsonArray) object).encodePrettily());
		} else if (object instanceof String) {
			response.putHeader("content-type", ContentTypeHeader.APPLICATIONJSON.getMimeType()).end((String) object);
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
