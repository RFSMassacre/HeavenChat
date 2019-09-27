package us.rfsmassacre.HeavenChat.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import us.rfsmassacre.HeavenChat.ChatBridge;
import us.rfsmassacre.HeavenChat.Tasks.SendAFKTask;

public class ServerAFKListener implements Listener
{
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		//Don't continue if players hasn't moved form the block
		if (event.getFrom().getBlockX() == event.getTo().getBlockX()
	     && event.getFrom().getBlockY() == event.getTo().getBlockY()
		 && event.getFrom().getBlockZ() == event.getTo().getBlockZ())
			return;
		
		new SendAFKTask(player).runTask(ChatBridge.getInstance());
	}
}
