package us.rfsmassacre.HeavenChat.Listeners;

import java.util.ArrayList;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

public class ProxyProximityListener implements Listener
{
	@EventHandler
	public void onProximityUpdate(PluginMessageEvent event)
	{
		try
		{
			if (event.getTag().equals(PluginChannel.PROXIMITY))
			{
				MemberManager members = ChatPlugin.getMemberManager();
				
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
				Member member = members.getMember(player);
				
				//Convert data to list of UUIDs
				ArrayList<UUID> nearbyIds = new ArrayList<UUID>();
				String[] stringIds = new String(event.getData()).split(" ");
				for (String stringId : stringIds)
				{
					nearbyIds.add(UUID.fromString(stringId));
				}
				member.setProximityPlayerIds(nearbyIds);
			}
		}
		catch (NullPointerException error)
		{
			//Do nothing, it means they logged off
		}
	}
}
