package us.rfsmassacre.HeavenChat.Tasks;

import org.bukkit.entity.Player;

import us.rfsmassacre.HeavenChat.Channels.PluginChannel;

public class SendAFKTask extends SendBukkitTask
{
	public SendAFKTask(Player player) 
	{
		super(player, PluginChannel.AFK, Long.toString(System.currentTimeMillis()));
	}
}
