package com.organization.project.controller;

import com.organization.project.service.BaseSQLClient;
import com.organization.project.service.HandleService;
import com.organization.project.type.OperationQuery;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

public class UserController {
	private JsonObject jsonObject;
	private SQLConnection sqlConnection;
	private JDBCClient jdbc;
	
	private HandleService handleService = new HandleService();

	public UserController(JDBCClient jdbc) {
		super();
		this.jdbc = jdbc;
	}

	protected void getDataExample(RoutingContext routingContext) {
		BaseSQLClient baseClient = new BaseSQLClient();
		HttpServerResponse response = routingContext.response();
		sqlConnection = null;
		jdbc.getConnection(res -> {
			if (res.succeeded()) {
				sqlConnection = res.result();
				jsonObject = baseClient.executeQuery("select nama from t_testing", sqlConnection, null,
						OperationQuery.SELECT);
				handleService.handlingResponse(routingContext, jsonObject);
			} else {
				handleService.sendError(404, response);
			}
		});

	}
}
