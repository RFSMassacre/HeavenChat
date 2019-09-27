package us.rfsmassacre.HeavenChat.Tasks;

import org.bukkit.entity.Player;

import us.rfsmassacre.HeavenChat.Channels.PluginChannel;

public class SendChatTask extends SendBukkitTask
{
	public SendChatTask(Player player, String chat)
	{
		super(player, PluginChannel.CHAT, chat);
	}
}
