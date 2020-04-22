package us.rfsmassacre.HeavenChat.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import us.rfsmassacre.HeavenChat.ChatBridge;
import us.rfsmassacre.HeavenChat.Tasks.SendAFKTask;
import us.rfsmassacre.HeavenChat.Tasks.SendLoginTask;

public class ServerLoginListener implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		//Notify proxy of joining a second after fully loaded in
		new SendLoginTask(player).runTaskLater(ChatBridge.getInstance(), 5L);
		new SendAFKTask(player).runTaskLater(ChatBridge.getInstance(), 5L);
	}
}
