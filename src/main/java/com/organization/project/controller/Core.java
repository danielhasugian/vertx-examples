package com.organization.project.controller;

import com.organization.project.service.PostgreSqlService;
import com.organization.project.type.ContentTypeHeader;
import com.organization.project.util.Runner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Core extends AbstractVerticle {

	private static final int LISTENED_PORT = 88;
	private static final int MAX_INITIAL_LINE_LENGTH = 16384;
	private Router router;
	private EmployeController employeController;
	private UserController userController;
	
	private PostgreSqlService postgreSqlService;
	private Vertx vertx;
	private JDBCClient jdbc;

	public static void main(String[] args) {
		Runner.runExample(Core.class);
	}

	@Override
	public void start(Future<Void> fut) {
		// initial core
		initialCore();

		router = Router.router(vertx);
		//handleService
		handleService();
		jdbc = postgreSqlService.setUp(vertx, "createShared");
		
		handleController();

		router.route().handler(BodyHandler.create());
		router.get("/").handler(routingContext -> {
			routingContext.response().putHeader("content-type", ContentTypeHeader.TEXTHTML.getMimeType()).end(getMessage());
		});

		// Setup EmployeController
		employeController.setUpInitialData();
		router.get("/employees").handler(employeController::handleListEmployees);
		router.post("/employees/clear").handler(employeController::handleClearEmployees);
		router.post("/employees/setup").handler(employeController::handleSetUpInitialData);
		router.get("/employees/:employeeId").handler(employeController::handleGetEmployee);
		router.post("/employees/:employeeId").handler(employeController::handleAddEmployee);
		
		//Setup User
		router.get("/user/sampling").handler(userController::getDataExample);

		vertx.createHttpServer(serverOption()).requestHandler(router::accept)
				.listen(config().getInteger("http.port", LISTENED_PORT), result -> {
					if (result.succeeded()) {
						fut.complete();
					} else {
						fut.fail(result.cause());
					}
				});
	}

	private String getMessage() {
		return "<center>" + "<h1 style='color:blue;'>" + "Micropayment Project" + "</h1>" + "</center>";
	}

	private HttpServerOptions serverOption() {
		HttpServerOptions opt = new HttpServerOptions();
		opt.setMaxInitialLineLength(MAX_INITIAL_LINE_LENGTH);
		return opt;
	}

	private void initialCore() {
		VertxOptions options = new VertxOptions();
		options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
		this.vertx = Vertx.vertx(options);
	}

	private void handleService() {
		postgreSqlService = new PostgreSqlService();
	}

	private void handleController() {
		employeController = new EmployeController();
		userController = new UserController(jdbc);
	}
}
