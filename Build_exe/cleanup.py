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
    client_path = os.path.join(SCRIPT_DIR, "application", "Client")
    os.makedirs(os.path.join(SCRIPT_DIR, "application", "Client"))

    jre_path = os.path.join(SCRIPT_DIR, "application", "jre")
    os.makedirs(jre_path)

    print("Adding .gitkeep files")
    ## Note that you cannot add empty directories to source control.
    ## To solve this issue we add this empty files to the directories.
    open(os.path.join(jre_path, ".gitkeep"), "w+")
    open(os.path.join(client_path, ".gitkeep"), "w+")

    print("Clean up Script Complete.")