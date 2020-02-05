"""
    Cleanup

    Run this to return the Application directory back to is defualt state.

    This script is useful to run in a build failed previously since it will (attempt to) clean up any mess.

    For best results run as admin.
"""

import os 
import shutil
import stat 
SCRIPT_DIR = os.path.dirname(__file__)

def redo_with_write(redo_func, path, err):
    os.chmod(path, stat.S_IWRITE)
    redo_func(path)


if __name__ == "__main__":
    print("deleting application folder...")
    app = os.path.join(SCRIPT_DIR, "application")
    shutil.rmtree(app, onerror=redo_with_write)

    print("recreating empty folder structure")
    os.makedirs(os.path.join(SCRIPT_DIR, "application", "Client"))
    os.makedirs(os.path.join(SCRIPT_DIR, "application", "jre"))