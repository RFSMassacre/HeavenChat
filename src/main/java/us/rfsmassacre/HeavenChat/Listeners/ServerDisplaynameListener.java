package us.rfsmassacre.HeavenChat.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenLib.Spigot.Managers.ChatManager;

public class ServerDisplaynameListener implements PluginMessageListener
{
    public ServerDisplaynameListener()
    {

    }

    public void onPluginMessageReceived(String channel, Player player, byte[] data)
    {
        if (channel.equals(PluginChannel.DISPLAYNAME))
        {
            player.setDisplayName(ChatManager.format(new String(data)));
        }
    }
}
