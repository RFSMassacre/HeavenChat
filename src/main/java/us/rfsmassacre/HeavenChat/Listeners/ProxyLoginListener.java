package us.rfsmassacre.HeavenChat.Listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import net.md_5.bungee.event.EventPriority;
import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Events.MemberJoinEvent;
import us.rfsmassacre.HeavenChat.Events.MemberLeaveEvent;
import us.rfsmassacre.HeavenChat.Managers.ChannelManager;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ConfigManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public class ProxyLoginListener implements Listener
{
	private ConfigManager config;
	private LocaleManager locale;
	private ChannelManager channels;
	private MemberManager members;
	
	public ProxyLoginListener()
	{
		locale = ChatPlugin.getLocaleManager();
		config = ChatPlugin.getConfigManager();
		channels = ChatPlugin.getChannelManager();
		members = ChatPlugin.getMemberManager();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerProxyJoin(PostLoginEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		Member member = members.getMember(player);
		if (member != null)
		{
			if (!player.hasPermission("heavenchat.nickname") && !member.getNickname().isEmpty())
			{
				member.setNickname("");
			}
		}
	}
	
	/*
	 * Rather than listening to proxy joining, this listens to
	 * the sub-server's notification. If the player's data hasn't
	 * been loaded in yet, it's assumed this is their first login.
	 */
	@EventHandler
	public void onPlayerJoin(PluginMessageEvent event)
	{
		if (event.getTag().equals(PluginChannel.LOGIN))
		{	
			ChannelManager channels = ChatPlugin.getChannelManager();
			
			String[] data = new String(event.getData()).split(":");
			String prefix = "";
			if (data.length >= 1)
			{
				prefix = data[0];
			}
			String suffix = "";
			if (data.length >= 2)
			{
				suffix = data[1];
			}

			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
			Member member = members.getMember(player);
			
			//Player has not logged in yet
			if (member == null)
			{
				member = members.loadMember(player);
				
				//Very first time logging on
				if (member == null)
				{
					member = new Member(player);
					
					//Add member to all channels available
					for (Channel channel : channels.getAllChannels())
					{
						if (channel.canJoin(member))
							channel.addMember(member, false);
					}
					
					Channel defaultChannel = channels.getChannel(config.getString("default-channel"));
					defaultChannel.addMember(member, false);
					
					member.setPrefix(prefix);
					member.setSuffix(suffix);
					member.setFocusedChannel(defaultChannel);
					members.addMember(member);
					members.saveMember(member);
					
					if (config.getBoolean("enable-join-messages"))
					{
						locale.broadcastLocale(false, "login.first-login-message", "{player}", member.getDisplayName());
						ProxyServer.getInstance().getPluginManager().callEvent(new MemberJoinEvent(member, true));
					}
				}
				//Returning player logging on
				else
				{
					member.setPrefix(prefix);
					member.setSuffix(suffix);
					
					if (config.getBoolean("enable-join-messages"))
					{
						locale.broadcastLocale(false, "login.login-message", "{player}", member.getDisplayName());
						ProxyServer.getInstance().getPluginManager().callEvent(new MemberJoinEvent(member));
					}
					
					if (config.getBoolean("force-focus-channels"))
					{
						Channel serverChannel = channels.getChannel(config.getString("servers." + member.getServer() + ".channel"));
						if (serverChannel != null && serverChannel.hasMember(member) && 
						!config.getStringList("ignored-channels").contains(member.getFocusedChannel().getName()))
						{
							member.setFocusedChannel(serverChannel);
						}
					}
				}
			}
		}

	}
	
	@EventHandler
	public void onPlayerLeave(PlayerDisconnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		Member member = members.getMember(player);
		
		//Unload data when player logs off the network
		if (member != null)
		{
			//Broadcast logout
			members.saveMember(member);
			members.removeMember(member);
			
			if (config.getBoolean("enable-join-messages"))
			{
				locale.broadcastLocale(false, "login.logout-message", "{player}", member.getDisplayName());
				ProxyServer.getInstance().getPluginManager().callEvent(new MemberLeaveEvent(member));
			}

			if (!player.hasPermission("heavenchat.nickname") && !member.getNickname().isEmpty())
			{
				member.setNickname("");
			}
		}
	}
	
	@EventHandler
	public void onPlayerSwitch(ServerConnectedEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		Member member = members.getMember(player);
		
		//Update the server they are currently connected in
		if (member != null)
		{
			member.setServer(event.getServer().getInfo().getName());
			
			if (config.getBoolean("force-focus-channels"))
			{
				Channel serverChannel = channels.getChannel(config.getString("servers." + member.getServer() + ".channel"));
				if (serverChannel != null && serverChannel.hasMember(member) && 
				!config.getStringList("ignored-channels").contains(member.getFocusedChannel().getName()))
				{
					member.setFocusedChannel(serverChannel);
				}
			}

			if (!player.hasPermission("heavenchat.nickname") && !member.getNickname().isEmpty())
			{
				member.setNickname("");
			}
		}
	}
}
