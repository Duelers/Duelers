# CardBoard

CardBoard is an open-source project to build a multiplayer collectable-card-game with units that move freely on a battlefield.
The name is a terrible pun; the game has cards, and its played on a board.

The core tech-stack is Java (with Fx) with Python used for scripting and Maven for our build automation. If you are interested in contributing please drop us a message.

### Screenshot

![Gameplay Screenshot](promoScreenshot.png)

### Building and installing with Maven

1. Import as a maven project (you will need the maven-plugin to do this)
2. Run the command: `mvn clean package`
3. In `Client/target` there should be a file `client-*-shaded.jar`
4. Run the above file and enjoy playing the game!
   - from the command line the run command is `java -jar Client/target/client-*-shaded.jar`

### Building an Executable (Windows)

- Please see the readme in the 'build_exe' directory for instructions.

### Original Project

The Original 'Duelers' Project was created by:

    1. Ahmad Salimi
    2. MohammadMahdi Jarrahi
    3. Mohammad Hadi Esnaashari

...And has been copied under the MIT license.

The original repo can be found here: https://github.com/aps2019project/Duelers

### NOTES:

- Sound effects just work on Windows. On Linux you need to install Glib.

## Licensing

### Code Contributions

Code contributed to Project CardBoard is licensed under the [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.html) or later. This includes all code, whether it be Java, Python, or any other language that is currently part of the project or is added in the future. This also includes data files, such as maps, as well as story content.

### Visual and Audio Contributions

All visual and audio assets are licensed under the [Creative Commons BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/). The contributor always retains the full rights to their own work and can continue to use it however they wish.

Unless explicitly stated otherwise, all visual and audio assets that are posted to the [Art](https://projectcardboard.freeforums.net/board/14/art) forum are considered as potential contributions and are automatically placed under the CC BY-SA 4.0.

Visual and audio assets for which you do not own the rights cannot be accepted, nor can any derivative works, nor anything that falls under fair use.

### Microservice Config Dump

You are going to need a PostgresQL database. You can name it whatever you want, but it has to exist.

Then, you will need a private.key and public.key for ES256 and a bunch of configs:

```
users.yaml
postgres:
  database: {DB_NAME}
  user: {DB_USER_NAME}
  password: {DB_PASSWORD}
  host: {DB_HOST}
  port: 5432
  minsize: 1
  maxsize: 5
authentication.yaml
users:
  url: "http://users"
keys:
  location: "/var/config/private.key"
token.yaml
users:
  url: "http://users"
keys:
  location: "/var/config/public.key"
registration.yaml
users:
  url: "http://users"
```

Then you create a bunch of secrets out of them with kubectl:
```
kubectl create secret generic authentication --from-file=./authentication.yaml --from-file=./keys/private.key
kubectl create secret generic token --from-file=./token.yaml --from-file=./keys/public.key
kubectl create secret generic registration --from-file=./registration.yaml
kubectl create secret generic users --from-file=./users.yaml
```

Then you can apply all the kubernetes templates from repos (the 4 python ones, and the 4 java ones)
Once you apply the users template and have that running, you need to sh into the pod and run python3 init_db.py

Then you will need something to direct external traffic into the cluster. I use Nginx external to the cluster and NodePorts so here is the config:

```
location /api/authentication/ {
   proxy_pass http://{ANY_KUBERNETES_NODE_HOST}:30081/public/;
}
location /api/registration/ {
   proxy_pass http://{ANY_KUBERNETES_NODE_HOST}:30082/public/;
}
location /api/token/ {
   proxy_pass http://{ANY_KUBERNETES_NODE_HOST}:30083/public/;
}
location /websockets/ {
   proxy_pass http://{ANY_KUBERNETES_NODE_HOST}:30888/websockets/;
   proxy_http_version 1.1;
   proxy_set_header Upgrade $http_upgrade;
   proxy_set_header Connection $connection_upgrade;
   proxy_set_header Host $host;
   proxy_read_timeout 900s;
}
location /test1/websockets/ {
   proxy_pass http://{ANY_KUBERNETES_NODE_HOST}:30889/websockets/;
   proxy_http_version 1.1;
   proxy_set_header Upgrade $http_upgrade;
   proxy_set_header Connection $connection_upgrade;
   proxy_set_header Host $host;
   proxy_read_timeout 900s;
}
location /test2/websockets/ {
   proxy_pass http://{ANY_KUBERNETES_NODE_HOST}:30890/websockets/;
   proxy_http_version 1.1;
   proxy_set_header Upgrade $http_upgrade;
   proxy_set_header Connection $connection_upgrade;
   proxy_set_header Host $host;
   proxy_read_timeout 900s;
}
location /test3/websockets/ {
   proxy_pass http://{ANY_KUBERNETES_NODE_HOST}:30891/websockets/;
   proxy_http_version 1.1;
   proxy_set_header Upgrade $http_upgrade;
   proxy_set_header Connection $connection_upgrade;
   proxy_set_header Host $host;
   proxy_read_timeout 900s;
}
```

If you run everything on one machine, you could just replace nodeports with actual kubernetes services and put nginx inside kubernetes and expose it instead.

After that you just need a hostname and a TLS certificate. Once you have those, you can change configs in the java part of the game and it should work.

#### Summary

Essentially you need to end up with a single endpoint that has `/api/authentication/`, `/api/registration/`, `/api/token/` that the client/server can be pointed at. Those three internally need to communicate with a `users` instance that has a postgres database.