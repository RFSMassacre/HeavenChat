package us.rfsmassacre.HeavenChat.Tasks;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import us.rfsmassacre.HeavenChat.Channels.PluginChannel;

public class SendProximityTask extends SendBukkitTask
{
	private static final int RANGE = 128;
	
	public SendProximityTask(Player player)
	{
		super(player, PluginChannel.PROXIMITY, null);
		
		ArrayList<String> playerIds = new ArrayList<String>();

		for (Player nearbyPlayer : player.getWorld().getPlayers())
		{
			if (nearbyPlayer.getLocation().distance(player.getLocation()) <= RANGE)
			{
				playerIds.add(nearbyPlayer.getUniqueId().toString());
			}
		}
		
		this.data = String.join(" ", playerIds);
	}
}
