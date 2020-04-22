package us.rfsmassacre.HeavenChat.Listeners;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;

public class ServerCommandListener implements PluginMessageListener
{
    public ServerCommandListener()
    {

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data)
    {
        if (channel.equals(PluginChannel.COMMAND))
        {
            String message = new String(data);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message);
        }
    }
}
