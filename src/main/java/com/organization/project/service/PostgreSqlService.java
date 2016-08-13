package com.organization.project.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

public class PostgreSqlService {

	private JDBCClient jdbc;

	public JsonObject initialConfig() {
		return new JsonObject().put("url", "jdbc:postgresql://localhost:5432/db_micropayment").put("user", "postgres")
				.put("password", "root").put("driver_class", "org.postgresql.Driver");
	}

	public JDBCClient setUp(Vertx vertx, String type) {
		if ("createShared".equals(type)) {
			createShared(vertx);
		} else if ("createNonShared".equals(type)) {
			createNonShared(vertx);
		} else {
			// Default
			createShared(vertx);
		}
		
		return jdbc;
	}

	private void createShared(Vertx vertx) {
		this.jdbc = JDBCClient.createShared(vertx, initialConfig());
	}

	private void createNonShared(Vertx vertx) {
		this.jdbc = JDBCClient.createNonShared(vertx, initialConfig());
	}

}
