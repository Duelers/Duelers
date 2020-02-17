import os
import subprocess
import shutil

SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))
FILENAME = 'AddJsonCardToGame'
EXTENTION = {"python": ".py", "executable": ".exe", "spec": ".spec"}

if __name__ == "__main__":
    path = os.path.join(SCRIPT_PATH, FILENAME + EXTENTION["python"])
    out_path = os.path.join(SCRIPT_PATH, "tmp")
    
    assert os.path.isfile(path), f"ERROR: {path} is not a valid file!"

    command = f'pyinstaller -y -F  "{path}" --distpath "{out_path}"'

    process = subprocess.Popen(command)
    process.wait()

    print("copy build .exe")
    exe_file = os.path.join(out_path, FILENAME + EXTENTION["executable"])
    shutil.copy(exe_file, SCRIPT_PATH)

    print("removing build files and dirs")
    os.remove(os.path.join(SCRIPT_PATH, FILENAME + EXTENTION["spec"]))
    shutil.rmtree(out_path)
    shutil.rmtree(os.path.join(SCRIPT_PATH, "__pycache__"))
    shutil.rmtree(os.path.join(SCRIPT_PATH, "build"))

    print("Complete")