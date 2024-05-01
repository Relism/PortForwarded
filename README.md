
# PortForwarded

![Banner Image](https://raw.githubusercontent.com/Relism/PortForwarded/master/pfbannertop.png)

PortForwarded is a Serverside Fabric Mod that enables you to securely expose your Minecraft server to the internet without the need to open your router's ports, using Ngrok v3. This means that your players won't need to install the mod themselves. With PortForwarded, you can easily and securely share your server with friends and players from around the world, without the hassle of configuring port forwarding on your router.

## Installation

To install PortForwarded, follow these steps:

1. Download the plugin and place it in your Minecraft server's `mods` folder.
2. Restart the server to load the plugin.
3. After restarting, a `config.yml` file will be generated in `/config/portforwarded/config.yml`.

## Configuration

In order to use PortForwarded, you need to update the following fields in the `config.yml` file:

- `ngrok_region`: Specify the ngrok region closest to your server location for optimal latency. Choose one of the following options: EU, AP, AU, IN, JP, SA, US, US_CAL_1.
- `ngrok_token`: Obtain a free ngrok authtoken by signing up at [https://ngrok.com/](https://ngrok.com/) and visiting the "Your authtoken" page in the left sidebar.
- `discord_webhook_url`: If you wish to receive webhook notifications containing your new ngrok URL each time the server starts, provide your Discord webhook URL. Leave this field blank if you don't want to receive webhook notifications.

Make sure to save the changes to the `config.yml` file after updating the fields.

Please note that since PortForwarded uses ngrok, the URL will change each time the server restarts. This ensures the security and integrity of your server.

## Usage

Once you have installed and configured PortForwarded, start your Minecraft server as usual. The plugin will automatically handle the ngrok integration and securely expose your server to the internet. The ngrok URL will be displayed in the console, and if specified, the Discord webhook URL can be shared with your friends and players to connect to your Minecraft server.

For any further assistance or troubleshooting, please reach out to our [support server](https://discord.gg/myAxAf7fCP) on Discord.
