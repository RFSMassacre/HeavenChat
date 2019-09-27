package us.rfsmassacre.HeavenChat.Listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Events.ChannelMessageEvent;
import us.rfsmassacre.HeavenChat.Events.PrivateMessageEvent;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public class ProxyAFKListener implements Listener
{
	private LocaleManager locale;
	
	public ProxyAFKListener()
	{
		locale = ChatPlugin.getLocaleManager();
	}
	
	@EventHandler
	public void onPlayerMove(PluginMessageEvent event)
	{
		if (event.getTag().equals(PluginChannel.AFK))
		{
			MemberManager members = ChatPlugin.getMemberManager();
			
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
			Member member = members.getMember(player);
			
			//Update AFK status
			if (member != null)
			{
				member.setLastMovement(Long.parseLong(new String(event.getData())));
				if (member.isAFK())
				{
					member.setAFK(false);
					locale.broadcastLocale(false, "afk.returned", "{player}", member.getDisplayName());
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerChannelMessage(ChannelMessageEvent event)
	{
		Member member = event.getSender();
		if (member.isAFK())
		{
			member.setAFK(false);
			locale.broadcastLocale(false, "afk.returned", "{player}", member.getDisplayName());
		}
	}
	
	@EventHandler
	public void onPlayerPrivateMessage(PrivateMessageEvent event)
	{
		Member member = event.getSender();
		if (member.isAFK())
		{
			member.setAFK(false);
			locale.broadcastLocale(false, "afk.returned", "{player}", member.getDisplayName());
		}
	}
}
