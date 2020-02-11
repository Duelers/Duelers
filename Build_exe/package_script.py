import os 
import shutil

import xml
import xml.etree.ElementTree as ET
import re


SCRIPT_DIR = os.path.dirname(__file__)
ROOT_DIR = os.path.abspath(os.path.join(__file__ ,"..",".."))

CLIENT_DIR = os.path.join(ROOT_DIR, "Client")
SERVER_DIR = os.path.join(ROOT_DIR, "Server")

LAUNCH4J_CONFIGS = os.path.join(SCRIPT_DIR, "launch4j_build_configs.xml")

def read_output_path(xml_file):
    """
    Reads the launch4j config file, and returns the .exe output path
    """
    tree = ET.parse(xml_file)
    root = tree.getroot()

    string= str(xml.etree.ElementTree.tostring(root))

    pattern = "<outfile>(.*)</outfile>"
    x = os.path.normpath(re.findall(pattern, string, flags= re.S)[0])
    return os.path.abspath(x)

def read_jre_path(xml_file):
     """
     Reads the launch4j config file, and returns the path of the bundled jre
     """
     tree = ET.parse(xml_file)
     root = tree.getroot()

     string= str(xml.etree.ElementTree.tostring(root))

     pattern = "<jre>.*<path>(.*)</path>"
     x = os.path.normpath(re.findall(pattern, string, flags= re.S)[0])
     return os.path.abspath(x)


if __name__ == "__main__":

    print("+===========================================+")
    print("|    WINDOWS .EXE PACKAGING SCRIPT ver1.0   |")
    print("+===========================================+")
    
    output_dir = os.path.abspath(os.path.join(os.path.dirname(read_output_path(LAUNCH4J_CONFIGS)), ".."))
    assert os.path.exists(output_dir), f"ERROR: bad path {output_dir}"

    jre_path = read_jre_path(LAUNCH4J_CONFIGS)
    assert os.path.exists(jre_path), f"ERROR: bad path {jre_path}"
    assert len(os.listdir(jre_path)) > 3, "ERROR: jre files are missing! Please ensure that '/application/jre/bin' is a valid path"

    print("Copying Client Resources and Classes...")
    client_classes = os.path.join(CLIENT_DIR, "target", "classes")
    client_resources = os.path.join(CLIENT_DIR, "resources")

    client_out = os.path.join(output_dir, "Client")

    shutil.copytree(client_classes, os.path.join(client_out, "classes"))
    shutil.copytree(client_resources, os.path.join(client_out, "resources"))

    print("Copying Server Resources and Classes...")
    server_classes = os.path.join(SERVER_DIR, "target", "classes")
    server_resources = os.path.join(SERVER_DIR, "resources")

    server_out = os.path.join(output_dir, "Server")

    shutil.copytree(server_classes, os.path.join(server_out, "classes"))
    shutil.copytree(server_resources, os.path.join(server_out, "resources"))

    print("Copying Resources...")
    resources = os.path.join(ROOT_DIR, "resources")
    resources_out = os.path.join(output_dir, "resources")

    shutil.copytree(resources, resources_out)

    print("Installation Script Complete.")