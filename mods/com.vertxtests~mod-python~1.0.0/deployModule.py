import vertx
import sys
import os
import importlib
from core.event_bus import EventBus

logger = vertx.logger()
config = vertx.config()


def msg_handler(message):
    logger.info("Python Module accessed. Message: %s" % message.body)
    # logger.info(os.getcwd() + "/mods/com.vertxtests~mod-python~1.0.0/tests/")
    sys.path.append(os.getcwd() + "/mods/com.vertxtests~mod-python~1.0.0/tests/")
    testscript = importlib.import_module("test_script")
    testscript.main("hello", config)
    message.reply("And now going back.")


eventbus_id = EventBus.register_handler("com.vertx.tests", handler=msg_handler)
logger.error("Handler registered: %s" % eventbus_id)


