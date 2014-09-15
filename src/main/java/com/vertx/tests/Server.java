package com.vertx.tests;


import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.platform.Verticle;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Server extends Verticle {

    private static Logger logger = LoggerFactory.getLogger(Server.class);


    @Override
    public void start() {

        final Verticle env = this;
        JsonObject appConfig = this.getContainer().config();
        logger.info(appConfig.toString());

        String pythonModule = appConfig.getObject("deploy_modules").getString("python_module");
        this.getContainer().deployModule(pythonModule, appConfig, new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> stringAsyncResult) {
                if(stringAsyncResult.succeeded()) {
                    logger.info("Python Module deployed: " + stringAsyncResult.succeeded());
                    env.getVertx().eventBus().send("com.vertx.tests", "Main verticle says hi.", new Handler<Message<String>>() {
                        @Override
                        public void handle(Message<String> message) {
                            logger.info("Response: " + message.body());
                        }
                    });

                } else {
                    logger.error("Python Module deploy failed. Reason: " + getStackTrace(new Exception(stringAsyncResult.cause())));
                }
            }
        });

    }

    private static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
