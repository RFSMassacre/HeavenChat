package us.rfsmassacre.HeavenChat.Tasks;

import org.bukkit.entity.Player;

import net.milkbowl.vault.chat.Chat;

import us.rfsmassacre.HeavenChat.ChatBridge;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;

public class SendSuffixTask extends SendBukkitTask
{	
	public SendSuffixTask(Player player)
	{
		super(player, PluginChannel.SUFFIX, null);
	}
	
	@Override
	public void run() 
	{
		if (player != null)
		{
			Chat chat = ChatBridge.getVaultChat();
			String suffix = (chat != null ? chat.getPlayerSuffix(player) : "");
				
			player.sendPluginMessage(ChatBridge.getInstance(), channel, suffix.getBytes());
		}
	}
}
