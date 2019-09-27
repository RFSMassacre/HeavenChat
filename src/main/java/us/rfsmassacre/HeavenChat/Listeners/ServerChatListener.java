package us.rfsmassacre.HeavenChat.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import us.rfsmassacre.HeavenChat.ChatBridge;
import us.rfsmassacre.HeavenChat.Tasks.SendAFKTask;
import us.rfsmassacre.HeavenChat.Tasks.SendChatTask;

public class ServerChatListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		/*
		 * Forward the chat to the proxy to be processed there.
		 * Fires last in order to be compatible with other plugins.
		 */
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			new SendChatTask(player, event.getMessage()).runTask(ChatBridge.getInstance());
			new SendAFKTask(player).runTask(ChatBridge.getInstance());
			event.setCancelled(true);
		}
	}
}
