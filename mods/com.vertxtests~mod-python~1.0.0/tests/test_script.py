import vertx
import json

logger = vertx.logger();


def main(message, config):
    logger.info("Test script started: %s" % message)
    logger.info("Test config element: %s" % config["global"]["regString"])