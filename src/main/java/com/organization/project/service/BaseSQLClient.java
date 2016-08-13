/*
 *  Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package com.organization.project.service;

import java.util.ArrayList;
import java.util.List;

import com.organization.project.type.OperationQuery;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLConnection;

/**
 *
 */
public class BaseSQLClient {
	private static final Logger logger = LoggerFactory.getLogger(BaseSQLClient.class);

	protected List<JsonArray> jsonArrays;
	protected JsonObject jsonObject;

	public JsonObject executeQuery(String queryString, SQLConnection sqlConnection, JsonArray jsonArray,
			OperationQuery type) {
		return executeQuerySuper(queryString, sqlConnection, jsonArray, type);
	}

	public JsonObject executeQuerySuper(String queryString, SQLConnection sqlConnection, JsonArray jsonArray,
			OperationQuery type) {
		jsonObject = new JsonObject();
		jsonArrays = new ArrayList<>();

		switch (type) {
		case SELECT:
			if (jsonArray == null) {
				getSelectedQuery(queryString, sqlConnection);
			} else {
				getSelectedQueryWithParams(queryString, jsonArray, sqlConnection);
			}
		case UPDATE:
			if (jsonArray == null) {
				getUpdatedQuery(queryString, sqlConnection);
			} else {
				getUpdatedQueryWithParams(queryString, jsonArray, sqlConnection);
			}
		case DELETE:
			if (jsonArray == null) {
				getUpdatedQuery(queryString, sqlConnection);
			} else {
				getUpdatedQueryWithParams(queryString, jsonArray, sqlConnection);
			}
		default:
			break;
		}

		return jsonObject;
	}

	private void getSelectedQuery(String queryString, SQLConnection sqlConnection) {
		sqlConnection.query(queryString, exec -> {
			if (exec.succeeded()) {
				jsonArrays = exec.result().getResults();
				jsonObject.put("failed", "false");
				jsonObject.put("results", jsonArrays);
			} else {
				jsonObject.put("failed", "true");
				logger.error("getSelectedQuery", exec.cause());
			}
		});
	}

	private void getUpdatedQuery(String queryString, SQLConnection sqlConnection) {
		sqlConnection.update(queryString, exec -> {
			if (exec.succeeded()) {
				jsonObject.put("failed", "false");
			} else {
				logger.error("getUpdatedQuery", exec.cause());
				jsonObject.put("failed", "true");
			}
		});
	}

	private void getSelectedQueryWithParams(String queryString, JsonArray jsonArray, SQLConnection sqlConnection) {
		sqlConnection.queryWithParams(queryString, jsonArray, exec -> {
			if (exec.succeeded()) {
				jsonArrays = exec.result().getResults();
				jsonObject.put("failed", "false");
				jsonObject.put("results", jsonArrays);
			} else {
				logger.error("getSelectedQuery", exec.cause());
				jsonObject.put("failed", "true");
			}
		});
	}

	private void getUpdatedQueryWithParams(String queryString, JsonArray jsonArray, SQLConnection sqlConnection) {
		sqlConnection.updateWithParams(queryString, jsonArray, exec -> {
			if (exec.succeeded()) {
				jsonObject.put("failed", "false");
			} else {
				logger.error("getUpdatedQuery", exec.cause());
				jsonObject.put("failed", "true");
			}
		});
	}

}
