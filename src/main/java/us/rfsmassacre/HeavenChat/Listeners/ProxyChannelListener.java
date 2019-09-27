package us.rfsmassacre.HeavenChat.Listeners;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Events.ChannelJoinEvent;
import us.rfsmassacre.HeavenChat.Events.ChannelLeaveEvent;
import us.rfsmassacre.HeavenChat.Managers.ChannelManager;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public class ProxyChannelListener implements Listener
{
	private LocaleManager locale;
	private ChannelManager channels;
	
	public ProxyChannelListener()
	{
		locale = ChatPlugin.getLocaleManager();
		channels = ChatPlugin.getChannelManager();
	}
	
	@EventHandler
	public void onChannelJoin(ChannelJoinEvent event)
	{
		channels.saveChannel(event.getChannel());
		
		for (Member member : event.getChannel().getOnlineMembers())
		{
			if (!member.equals(event.getMember()))
			{
				locale.sendLocale(member.getPlayer(), "channel-notify.joined", "{member}", event.getMember().getDisplayName(),
					"{channel}", event.getChannel().getDisplayName());
			}
		}
	}
	
	@EventHandler
	public void onChannelLeave(ChannelLeaveEvent event)
	{
		channels.saveChannel(event.getChannel());
		
		for (Member member : event.getChannel().getOnlineMembers())
		{
			if (!member.equals(event.getMember()))
			{
				locale.sendLocale(member.getPlayer(), event.isKicked() ? "channel-notify.kicked" :"channel-notify.left", 
					"{member}", event.getMember().getDisplayName(),
					"{channel}", event.getChannel().getDisplayName());
			}
		}
	}
}
