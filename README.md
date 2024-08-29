# BlockBridge Discord Bot #
BlockBridge is a Utility/Api for connecting Minecraft with Discord Servers

## Prerequisites ##
In order to run the bot you will need the following:
1) A Discord Bot Token ([Check out how to get one here](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token))
2) Either a Pterodactyl/Multicraft Panel or a VPS/Dedicated Server

## Installation ##
There are two ways to install the bot, either by using a pre-built panel (like Pterodactyl or Multicraft) or by using a VPS/Dedicated Server.
### Pterodactyl/Multicraft Panel ###
For this installation method you will need some technical knowledge and access to the server's file system. I will not be going over specifics for each panel, however you can join the Discord for help on specific panel installations.
#### Download the latest release from the [Releases](https://ci.vanillaflux.com)
Download the latest stable release from the releases page.

#### Create a new server on your panel
If you're having issues on this step join the [Discord]() for help

#### Upload the bot jar file to the root of your server
Once you have the jar file uploaded, to the root of the server, verify that your server jar file is set to the bot jar file.

#### Start the server
On first launch you should generate a bunch of errors, this is normal. The bot will generate a config file and then stop. Edit the config file ([More info here](#configuration-)).

#### Editing the config file
The config file will be located in the root of your server, and will be named `config.json`. These are the values we need to change before launching again:
   ```json
   {
    "cmd_channel": 0,
    "log_channel": 0,
    "guild_id": 0,
    "bot_token": 0,
    "web_port": 8585
   }
   ```
You can learn how to get channel/guild ids [here](https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID-) (Server ID is the same as Guild ID)
* `cmd_channel`: The channel id that the bot will listen for commands in.
* `log_channel`: The channel id that the bot will log messages to. (commands are accepted here as well for debugging purposes)
* `guild_id`: The id of the guild that the bot will be running in.
* `bot_token`: The token of the bot that you created.
* `web_port`: The port that the bot will listen for web requests on. This is optional to change, however if you are already running a service on port 8585 you will need to change it.

#### Launch the server again
Once you have edited the config file, start the server again. The bot should now be running.

### VPS/Dedicated Server ###
For this installation method you will need some technical knowledge and access to the server's file system as well as 'sudo' access. I will not be going over specifics for each OS type, however you can join the Discord for help on specific OS installations. I will only be going over Ubuntu 20.04 LTS.
#### Download the latest release from the [Releases](https://ci.vanillaflux.com)
Download the latest stable release from the releases page.

#### Install Java
1) Update the package list
    ```bash
    sudo apt update
    ```
2) Check to see if Java is already installed
    ```bash
    java -version
    ```
3) If Java is not installed, you should see an error message listing the available Java versions:
    ```output
    Command 'java' not found, but can be installed with:

    sudo apt install openjdk-11-jre-headless  # version 11.0.11+9-0ubuntu2~20.04, or
    sudo apt install default-jre              # version 2:1.11-72
    sudo apt install openjdk-13-jre-headless  # version 13.0.7+5-0ubuntu1~20.04
    sudo apt install openjdk-16-jre-headless  # version 16.0.1+9-1~20.04
    sudo apt install openjdk-8-jre-headless   # version 8u292-b10-0ubuntu1~20.04
    ```
4) Install the default Java Runtime Environment
    ```bash
    sudo apt install default-jre
    ```
5) Verify the installation
    ```bash
    java -version
    ```
    ```output
    openjdk version "11.0.11" 2021-04-20
    OpenJDK Runtime Environment (build 11.0.11+9-Ubuntu-0ubuntu2.20.04)
    OpenJDK 64-Bit Server VM (build 11.0.11+9-Ubuntu-0ubuntu2.20.04, mixed mode, sharing)
    ```
6) Install the Java Development Kit
    ```bash
    sudo apt install default-jdk
    ```
7) Verify the installation
    ```bash
    javac -version
    ```
    ```output
    javac 11.0.11
    ```
#### Set up a folder for the bot
1) Create a new directory for the bot
    ```bash
    mkdir ~/blockbridge
    ```
2) Move the bot jar file to the new directory. You can use a file transfer program like WinSCP or FileZilla to move the file, or if you have apache installed you can download it using the curl command.
    ```bash
    cd ~/blockbridge
    curl -O https://ci.vanillaflux.com/job/block_bridge_discord/lastSuccessfulBuild/artifact/build/libs/block_bridge_discord-1.0.0-all.jar
    ```
3) Create a new file called 'start.sh' in the same directory. In this example I used the `nano` text editor, however you can use any text editor you prefer.
    ```bash
   nano start.sh
    ```
4) Add the following code to the file
    ```shell
    #!/bin/bash
    cd ~/blockbridge
    java -jar block_bridge_discord-1.0.0-all.jar
    ```
5) Save the file and exit the text editor
6) Make the file executable
    ```bash
    chmod +x start.sh
    ```
#### Start the bot
1) Run the start script
    ```bash
    ./start.sh
    ```
2) The bot should start and generate a config file. Once the bot has generated the config file, stop the bot by pressing `CTRL + C`
3) Edit the config file ([More info here](#configuration-)).
    ```bash
    nano config.json
    ```
   These are the values we need to change before launching again:
   ```json
   {
    "cmd_channel": 0,
    "log_channel": 0,
    "guild_id": 0,
    "bot_token": 0,
    "web_port": 8585
   }
   ```
   You can learn how to get channel/guild ids [here](https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID-) (Server ID is the same as Guild ID)
   * `cmd_channel`: The channel id that the bot will listen for commands in.
   * `log_channel`: The channel id that the bot will log messages to. (commands are accepted here as well for debugging purposes)
   * `guild_id`: The id of the guild that the bot will be running in.
   * `bot_token`: The token of the bot that you created.
   * `web_port`: The port that the bot will listen for web requests on. This is optional to change, however if you are already running a service on port 8585 you will need to change it.
4) Start the bot again
    ```bash
    ./start.sh
    ```
5) The bot should now be running with no errors. If you have any issues, join the [Discord]() for help.
   
#### Set Bot as a Service (Optional)
1) Create a new file called 'blockbridge.service' in the `/etc/systemd/system/` directory. In this example I used the `nano` text editor, however you can use any text editor you prefer.
    ```bash
    sudo nano /etc/systemd/system/blockbridge.service
    ```
2) Check what user you are currently logged in as
    ```bash
    whoami
    ```
   ```output
   user
   ```
3) Add the following code to the file. Replace `{path/to/bot}` with the path to the bot folder and `{user}` with the user you are currently logged in as.
    ```shell
    [Unit]
    Description=BiFlux Discord Bot
    After=multi-user.target
    After=network-online.target
    Wants=network-online.target
    
    [Service]
    ExecStart={path/to/bot}/start.sh
    User={user}
    Group={user}
    Type=idle
    Restart=on-failure
    RestartSec=5
    RestartForceExitStatus=1
    RestartForceExitStatus=26
    TimeoutStopSec=10
    
    [Install]
    WantedBy=multi-user.target
    ```
4) Save the file and exit the text editor
5) Reload the systemd daemon
    ```bash
    sudo systemctl daemon-reload
    ```
6) Start the bot service
    ```bash
    sudo systemctl start blockbridge
    ```
7) Enable the bot service
    ```bash
    sudo systemctl enable blockbridge
    ```
8) Check the status of the bot service
    ```bash
    sudo systemctl status blockbridge
    ```
9) The bot should now be running as a service. If you have any issues, join the [Discord]() for help.

## Configuration ##
Your default configuration file will look like this:
```json
{
   "allow": [],
   "cmd_channel": 0,
   "log_channel": 0,
   "web_port": 8585,
   "command_prefix": "!",
   "token_valid_time": 24,
   "guild_id": 0,
   "bot_token": 0,
   "api_entry_point": "/api",
   "app_entry_point": "/app"
   
}
```
* `allow`: An array of user ids that are allowed to use the bot. This will be generated with the `allow` bot command.
* `cmd_channel`: The channel id that the bot will listen for commands in.
* `log_channel`: The channel id that the bot will log messages to. (commands are accepted here as well for debugging purposes)
* `web_port`: The port that the bot will listen for web requests on.
* `command_prefix`: The prefix that the bot will listen for commands with.
* `token_valid_time`: The time in hours that a token is valid before expiring.
* `guild_id`: The id of the guild that the bot will be running in.
* `bot_token`: The token of the bot that you created.
* `api_entry_point` & `app_entry_point`: The entry points for the api and app sections of the BlockBridge Discord bot. You can edit these as necessary, however remember they need to be mirrored in the mod's config.
