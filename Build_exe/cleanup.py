import os 
import shutil
import stat 

"""
    Cleanup

    Run this to return the build_exe/Application directory back to is default state.
    This script is useful to run if a build failed previously and you wish to clean up any mess.

    For best results RUN AS ADMINISTRATOR.
"""

SCRIPT_DIR = os.path.dirname(__file__)

def redo_with_write(func, path, err):
    """
    Change permissions of file/directory.
    We then call func(path) 
    """
    os.chmod(path, stat.S_IWRITE)
    func(path)


if __name__ == "__main__":
    print("deleting application folder...")
    app = os.path.join(SCRIPT_DIR, "application")
    shutil.rmtree(app, onerror=redo_with_write)

    # Default Structure is empty 'jre' and 'Client' folders inside build_exe/Application
    print("recreating empty folder structure...")
    os.makedirs(os.path.join(SCRIPT_DIR, "application", "Client"))
    os.makedirs(os.path.join(SCRIPT_DIR, "application", "jre"))

    print("Clean up Script Complete.")