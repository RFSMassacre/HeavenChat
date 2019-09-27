package us.rfsmassacre.HeavenChat.Tasks;

import org.bukkit.entity.Player;

import net.milkbowl.vault.chat.Chat;

import us.rfsmassacre.HeavenChat.ChatBridge;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;

public class SendLoginTask extends SendBukkitTask
{
	public SendLoginTask(Player player)
	{
		super(player, PluginChannel.LOGIN, null);
	}
	
	@Override
	public void run() 
	{
		if (player != null)
		{
			Chat chat = ChatBridge.getVaultChat();
			String prefix = (chat != null ? chat.getPlayerPrefix(player) : "");
				
			player.sendPluginMessage(ChatBridge.getInstance(), channel.toString(), prefix.getBytes());
		}
	}
}
