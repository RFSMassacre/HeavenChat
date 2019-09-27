package us.rfsmassacre.HeavenChat.Listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

public class ProxyNameListener implements Listener
{
	@EventHandler
	public void onPlayerNameUpdate(PluginMessageEvent event)
	{
		if (event.getTag().equals(PluginChannel.PREFIX))
		{	
			MemberManager members = ChatPlugin.getMemberManager();
			
			String prefix = new String(event.getData());
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
			Member member = members.getMember(player);
			
			//Update player prefix
			if (member != null)
				member.setPrefix(prefix);
			
			return;
		}
		
		if (event.getTag().equals(PluginChannel.SUFFIX))
		{
			MemberManager members = ChatPlugin.getMemberManager();
			
			String suffix = new String(event.getData());
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
			Member member = members.getMember(player);
			
			//Update player suffix
			if (member != null)
				member.setSuffix(suffix);
			
			return;
		}
	}
}
