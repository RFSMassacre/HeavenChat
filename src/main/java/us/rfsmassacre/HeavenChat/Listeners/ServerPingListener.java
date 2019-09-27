package us.rfsmassacre.HeavenChat.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import us.rfsmassacre.HeavenChat.ChatBridge;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Managers.OptionManager;

public class ServerPingListener implements PluginMessageListener
{
	private OptionManager options;
	
	public ServerPingListener()
	{
		options = ChatBridge.getOptionManager();
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) 
	{
		if (channel.equals(PluginChannel.PING))
		{
			float volume = options.getFloat("PING_VOLUME");
			float pitch = options.getFloat("PING_PITCH");
			
			for (Sound sound : Sound.values())
			{
				if (sound.toString().contains(options.getString("PING_SOUND")))
				{
					Player target = Bukkit.getPlayer(UUID.fromString(new String(data)));
					if (target != null)
						player.playSound(player.getLocation(), sound, volume, pitch);
					
					return;
				}
			}
		}
	}
}
