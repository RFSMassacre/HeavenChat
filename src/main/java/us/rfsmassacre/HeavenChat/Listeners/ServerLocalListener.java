package us.rfsmassacre.HeavenChat.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import us.rfsmassacre.HeavenChat.ChatBridge;
import us.rfsmassacre.HeavenChat.Tasks.SendProximityTask;

public class ServerLocalListener implements Listener
{
    private static final int RANGE = 128;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        new SendProximityTask(player).runTask(ChatBridge.getInstance());

        for (Player other : player.getWorld().getPlayers())
        {
            if (player.getLocation().distance(other.getLocation()) <= RANGE)
            {
                new SendProximityTask(other).runTask(ChatBridge.getInstance());
            }
        }
    }
}
