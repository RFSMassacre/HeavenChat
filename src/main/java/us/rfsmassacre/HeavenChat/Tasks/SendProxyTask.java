package us.rfsmassacre.HeavenChat.Tasks;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class SendProxyTask implements Runnable
{
	protected ProxiedPlayer player;
	protected String channel;
	protected String data;
	
	public SendProxyTask(ProxiedPlayer player, String channel, String data)
	{
		this.player = player;
		this.channel = channel;
		this.data = data;
	}
	
	@Override
	public void run()
	{
		if (player != null && player.getServer() != null)
		{
			player.getServer().sendData(channel, data.getBytes());
		}
	}
}
