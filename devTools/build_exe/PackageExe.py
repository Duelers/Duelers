import os
import shutil

DIR_PATH = os.path.dirname(os.path.realpath(__file__)) # path of this file, i.e \devTools\build_exe

BAT_NAME = "CardBoard.bat"
EXE_NAME = "CardBoard.exe"
CLIENT_VERSION = "clientVersion.txt"

CARDBOARD_EXE_PATH = os.path.join(DIR_PATH, "..", "..", "Client", "target", EXE_NAME)
CARDBOARD_BAT_PATH = os.path.join(DIR_PATH, BAT_NAME)
CLIENTVERSION_TXT_PATH = os.path.join(DIR_PATH, "..", "..", "Client", "src", "main", "resources", CLIENT_VERSION)

OUTPUT_DIR = os.path.join(DIR_PATH, "release")


def get_version(txt_file: str) -> str:
    version = "0.0.0"
    with open(txt_file, "r") as f:
        version = f.readline()

    return version.strip()
     

def make_dirs(dir: str, dirname: str) -> str:
    path = os.path.join(dir, dirname)
    
    os.makedirs(path, exist_ok=False, mode=0x77)

    # make subdirs
    logs_path = os.path.join(path, "logs")
    docs_path = os.path.join(path, "docs")
    data_path = os.path.join(path, "data")

    os.makedirs(logs_path, exist_ok=False, mode=0x77)
    os.makedirs(docs_path, exist_ok=False, mode=0x77)
    os.makedirs(data_path, exist_ok=False, mode=0x77)

    return path


if __name__ == "__main__":

    print("RUNNING PACKAGING SCRIPT")

    assert os.path.isfile(CARDBOARD_EXE_PATH), f"missing {EXE_NAME}"
    assert os.path.isfile(CLIENTVERSION_TXT_PATH), f"missing {CLIENT_VERSION}" 
    assert os.path.isfile(CARDBOARD_BAT_PATH), f"missing {BAT_NAME}" 
    assert os.path.isdir(OUTPUT_DIR), f"{OUTPUT_DIR} does not exist"

    print("Getting Client version...")
    version = get_version(CLIENTVERSION_TXT_PATH)
    release_name = f"CardBoard_{version}"
    print(f"Version found: {version}")

    print("Creating directory...")
    release_dir = make_dirs(OUTPUT_DIR, release_name)
    print(f"output directory created at: {release_dir}")

    print("Copying files to out_dir...")
    shutil.copy(CARDBOARD_EXE_PATH, os.path.join(release_dir, "data"))
    shutil.copy(CARDBOARD_BAT_PATH, release_dir)


    print("Script COMPLETE.")



