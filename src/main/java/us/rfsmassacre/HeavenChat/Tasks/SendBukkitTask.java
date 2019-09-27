package us.rfsmassacre.HeavenChat.Tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import us.rfsmassacre.HeavenChat.ChatBridge;

public abstract class SendBukkitTask extends BukkitRunnable
{
	protected Player player;
	protected String channel;
	protected String data;
	
	public SendBukkitTask(Player player, String channel, String data)
	{
		this.player = player;
		this.channel = channel;
		this.data = data;
	}
	
	@Override
	public void run() 
	{
		if (player != null)
		{
			player.sendPluginMessage(ChatBridge.getInstance(), channel, data.getBytes());
		}
	}
}
