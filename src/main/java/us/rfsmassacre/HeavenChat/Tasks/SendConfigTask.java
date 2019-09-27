package us.rfsmassacre.HeavenChat.Tasks;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;

public class SendConfigTask extends SendProxyTask
{
	public SendConfigTask(ProxiedPlayer player, String key, String option) 
	{
		super(player, PluginChannel.CONFIG, key + ":" + option);
	}
	public SendConfigTask(ProxiedPlayer player, String key, int value)
	{
		super(player, PluginChannel.CONFIG, key + ":" + Integer.toString(value));
	}
	public SendConfigTask(ProxiedPlayer player, String key, double value)
	{
		super(player, PluginChannel.CONFIG, key + ":" + Double.toString(value));
	}
	public SendConfigTask(ProxiedPlayer player, String key, float value)
	{
		super(player, PluginChannel.CONFIG, key + ":" + Float.toString(value));
	}
}
