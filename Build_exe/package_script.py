import os
import shutil

SCRIPT_PATH = os.path.realpath(__file__)
ROOT_DIR = os.path.dirname(SCRIPT_PATH)

OUTPUT_DIR = os.path.join(os.path.dirname(SCRIPT_PATH), "target", "application") # This should be the same path given in the Launch4j configs

CLIENT_DIR = os.path.join(ROOT_DIR, "Client")
SERVER_DIR = os.path.join(ROOT_DIR, "Server")

#EXE_NAME = "cardboard.exe"

os.makedirs(OUTPUT_DIR, mode = 0o777, exist_ok = True)


## Copy Client data
client_classes = os.path.join(CLIENT_DIR, "target", "classes")
client_resources = os.path.join(CLIENT_DIR, "resources")

client_out = os.path.join(OUTPUT_DIR, "Client")

shutil.copy(client_resources, client_out)
shutil.copy(client_classes, client_out)

## Copy Server data
server_classes = os.path.join(SERVER_DIR, "target", "classes")
server_resources = os.path.join(SERVER_DIR, "resources")

server_out = os.path.join(OUTPUT_DIR, "Server")

shutil.copy(server_resources, server_out)
shutil.copy(server_classes, server_out)

## Copy Resources
resources = os.path.join(ROOT_DIR, "resources")
resources_out = os.path.join(OUTPUT_DIR, "resources")

shutil.copy(resources, resources_out)