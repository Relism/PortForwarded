# PortForwarded

![Banner Image](https://raw.githubusercontent.com/Relism/PortForwarded/master/pfbannertop.png)

**PortForwarded** is a _server-side_ fabric mod that allows you to securely expose your Minecraft server to the internet using Ngrok v3 **without opening your router's ports**. With **PortForwarded** you can easily and securely **share your server with friends and players around the world** without the hassle of configuring port forwarding on your router. 

**Note:** Your players don't need to install the mod themselves!

## Installation

To install **PortForwarded** follow these steps

1. Download the mod and place it in the 'mods' folder of your Minecraft server.
2. Download the [Fabric API jarfile](https://modrinth.com/mod/fabric-api) and place it in the same 'mods' folder.
3. Restart your server to load the mod.
4. After the restart, a `config.yml` file will be generated in `/config/portforwarded/config.yml`.

## Configuration

In order to use **PortForwarded**, you will need to update the following fields in your `config.yml` file

- `ngrok_region`: Specify the ngrok region closest to your server location for optimal latency. Select one of the following options: EU, AP, AU, IN, JP, SA, US, US_CAL_1.
- `ngrok_token`: Get a free ngrok authtoken by registering at [https://ngrok.com/](https://ngrok.com/) and visiting the "Your authtoken" page in the left sidebar.
- `Discord_webhook_url`: If you want to receive webhook notifications with your new ngrok URL every time the server starts up, enter your Discord webhook URL. Leave this blank if you don't want to receive webhook notifications.

Make sure you save the changes to the `config.yml` file after updating the fields.

Please note that since **PortForwarded** uses ngrok, the URL will change each time the server is restarted. This is to ensure the security and integrity of your server.

## Usage

Once you have **PortForwarded** installed and configured, start your Minecraft server as usual. The mod will automatically handle the ngrok integration and securely expose your server to the internet. The ngrok URL will appear in the console, and if specified, the Discord webhook URL can be shared with your friends and players to connect to your Minecraft server.

For any further assistance or troubleshooting, please reach out to our [support server](https://discord.gg/myAxAf7fCP) on Discord.
