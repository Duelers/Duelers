"""
    Cleanup

    Run this to return the Application directory back to is defualt state.

    This script is useful to run in a build failed previously since it will (attempt to) clean up any mess
"""

import os 
import shutil

SCRIPT_DIR = os.path.dirname(__file__)

if __name__ == "__main__":
    print("deleting application folder...")
    app = os.path.join(SCRIPT_DIR, "application")
    shutil.rmtree(app)

    print("recreating empty folder structure")
    os.makedirs(os.path.join(SCRIPT_DIR, "application", "Client"))
    os.makedirs(os.path.join(SCRIPT_DIR, "application", "jre"))