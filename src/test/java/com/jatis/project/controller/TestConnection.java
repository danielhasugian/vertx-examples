package com.jatis.project.controller;


import org.junit.BeforeClass;
import org.junit.Test;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

public class TestConnection {

	private static JDBCClient jdbc;
	private static SQLConnection conn;

	public static void initial() {
		VertxOptions options = new VertxOptions(); 
		options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
		Vertx vertx = Vertx.vertx(options);
		jdbc = JDBCClient.createShared(vertx,
				new JsonObject().put("url", "jdbc:postgresql://localhost:5432/db_micropayment").put("user", "postgres")
						.put("password", "root").put("driver_class", "org.postgresql.Driver"));
	}

	@BeforeClass
	public static void setupBeforeClass() {
		initial();
		jdbc.getConnection(res -> {
			if (res.succeeded()) {
				System.out.println("success setup");
				conn = res.result();
			} else {
				System.out.println("failed setup");
			}
		});
	}

	@Test
	public void testSelect() {
		conn.queryWithParams("select nama from t_testing where id=?", new JsonArray().add(1),
				query -> {
					if (query.succeeded()) {
						System.out.println("succes");
					} else {
						System.out.println(query.cause());
					}
				});

	}

	@Test
	public void testInsert() {
		conn.updateWithParams("Insert into t_testing(id, nama) values(?, ?)", new JsonArray().add(10).add("Arri"),
				query -> {
					if (query.succeeded()) {
						System.out.println("succes");
					} else {
						System.out.println(query.cause());
					}
				});
	}

}
