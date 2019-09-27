package us.rfsmassacre.HeavenChat.Tasks;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.Channels.PluginChannel;

public class SendPingTask extends SendProxyTask
{
	public SendPingTask(ProxiedPlayer player) 
	{
		super(player, PluginChannel.PING, player.getUniqueId().toString());
	}
}
