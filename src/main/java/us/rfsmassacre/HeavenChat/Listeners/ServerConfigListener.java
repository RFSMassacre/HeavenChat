package us.rfsmassacre.HeavenChat.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import us.rfsmassacre.HeavenChat.ChatBridge;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Managers.OptionManager;

public class ServerConfigListener implements PluginMessageListener
{
	private OptionManager options;
	
	public ServerConfigListener()
	{
		options = ChatBridge.getOptionManager();
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) 
	{
		//Keeps all option managers in sync with the proxy server config file
		if (channel.equals(PluginChannel.CONFIG))
		{
			String stringData = new String(data);
			String[] option = stringData.split(":");
			
			options.setString(option[0], option[1]);
		}
	}
}
