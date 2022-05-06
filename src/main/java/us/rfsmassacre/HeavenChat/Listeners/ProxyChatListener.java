package us.rfsmassacre.HeavenChat.Listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Events.ChannelMessageEvent;
import us.rfsmassacre.HeavenChat.Events.ChannelMessageReceiveEvent;
import us.rfsmassacre.HeavenChat.Events.PrivateMessageEvent;
import us.rfsmassacre.HeavenChat.Events.SpyMessageEvent;
import us.rfsmassacre.HeavenChat.Managers.LogsManager;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Managers.SpamManager;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenChat.Tasks.SendPingTask;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ChatManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ConfigManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

import java.util.ArrayList;
import java.util.List;

public class ProxyChatListener implements Listener
{
	private ConfigManager config;
	private LocaleManager locale;
	private MemberManager members;
	private SpamManager spam;
	
	public ProxyChatListener()
	{
		config = ChatPlugin.getConfigManager();
		members = ChatPlugin.getMemberManager();
		locale = ChatPlugin.getLocaleManager();
		spam = ChatPlugin.getSpamManager();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChannelSpam(ChannelMessageEvent event)
	{
		if (!event.isCancelled())
		{
			Member member = event.getSender();
			
			//Cancel if not fully logged in yet
			if (member != null)
			{
				//Cancel spam check if it's a whitelisted command.
				String message = event.getMessage();
				
				//Check for each kind of spam before sending it to the sub servers
				if (spam.isFloodSpam(member))
				{
					locale.sendLocale(member.getPlayer(), "filter.flood", "{time}", spam.getFloodSpamTime(member));
					event.setCancelled(true);
					return;
				}
				if (spam.isRepititionSpam(member, message))
				{
					locale.sendLocale(member.getPlayer(), "filter.repitition");
					event.setCancelled(true);
					return;
				}
				
				//Prechecks permission and filters out 
				event.setMessage(spam.filterSpam(member, message));
				//Record the time of message
				spam.setLastMessage(member, message);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPMSpam(PrivateMessageEvent event)
	{
		if (!event.isCancelled())
		{
			Member member = event.getSender();
			
			//Cancel if not fully logged in yet
			if (member != null)
			{
				//Cancel spam check if it's a whitelisted command.
				String message = event.getMessage();
				
				//Check for each kind of spam before sending it to the sub servers
				if (spam.isFloodSpam(member))
				{
					locale.sendLocale(member.getPlayer(), "filter.flood", "{time}", spam.getFloodSpamTime(member));
					event.setCancelled(true);
					return;
				}
				if (spam.isRepititionSpam(member, message))
				{
					locale.sendLocale(member.getPlayer(), "filter.repitition");
					event.setCancelled(true);
					return;
				}
				
				//Prechecks permission and filters out 
				event.setMessage(spam.filterSpam(member, message));
				//Record the time of message
				spam.setLastMessage(member, message);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(PluginMessageEvent event)
	{
		if (event.getTag().equals(PluginChannel.CHAT))
		{
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
			Member member = members.getMember(player);
			String chat = new String(event.getData());
			
			//Send PM
			if (member.getFocusedMemberId() != null)
			{
				Member targetMember = member.getFocusedMember();
				
				if (targetMember != null)
				{
					targetMember.sendPrivateMessage(member, chat);
				}
				else
				{
					Channel channel = member.getFocusedChannel();
					
					//Get offline display name and channel name to properly tell user the target left
					String targetName = members.getOfflineMember(member.getFocusedMemberId()).getDisplayName();
					String channelName = (channel != null ? channel.getDisplayName() : "");
					
					locale.sendLocale(member.getPlayer(), "pm.target-left", "{target}", targetName
											                    , "{channel}", channelName);
					
					member.setFocusedMember(null);
					member.setLastMember(null);
				}

				return;
			}
			//Send channel message
			else if (member.getFocusedChannel() != null)
			{
				Channel channel = member.getFocusedChannel();
				if (channel.hasMember(member))
				{
					channel.sendMessage(member, chat);
					
					return;
				}
			}
			
			locale.sendLocale(player, "error.no-channel");
		}
	}
	
	@EventHandler
	public void onPlayerSpy(PrivateMessageEvent event)
	{	
		if (!event.isCancelled())
		{
			Member sender = event.getSender();
			Member target = event.getTarget();
			
			//Cancel if either of them have no-spy permissions for their target/sender
			if (!sender.getPlayer().hasPermission("heavenchat.nospy." + target.getUsername())
			 && !target.getPlayer().hasPermission("heavenchat.nospy." + sender.getUsername()))
			{
				for (Member member : members.getOnlineMembers())
				{
					//Ensures if permission was removed they won't continue to spy
					if (member.isSpying() && !sender.equals(member) && !target.equals(member))
					{
						member.sendSpyMessage(sender, target, event.getMessage());
					}
				}
			}
		}
	}
	
	/*
	 * Logs every message players send in the log files.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChannelLog(ChannelMessageEvent event)
	{
		if (!event.isCancelled())
		{
			LogsManager logs = ChatPlugin.getLogsManager();
			
			String format = event.getChannel().getFormat();
			Member sender = event.getSender();
			String server = ChatPlugin.getConfigManager().getString("server-prefixes." + sender.getServer());
			
			logs.logMessage(format, "{server}", server, "{color}", "", "{sender}", sender.getDisplayName(), 
					"{message}", event.getMessage());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPMLog(PrivateMessageEvent event)
	{
		if (!event.isCancelled())
		{
			ConfigManager config = ChatPlugin.getConfigManager();
			LogsManager logs = ChatPlugin.getLogsManager();
			
			String format = config.getString("pm-formats.pm-spy");
			Member sender = event.getSender();
			Member receiver = event.getTarget();
			
			if (!sender.getPlayer().hasPermission("heavenchat.nospy." + receiver.getUsername())
					 && !receiver.getPlayer().hasPermission("heavenchat.nospy." + sender.getUsername()))
			{
				logs.logMessage("[PM] " + format, "{color}", "", "{sender}", sender.getDisplayName(), "{receiver}", receiver.getDisplayName(), 
						"{message}", event.getMessage());
			}
		}
	}
	
	/*
	 * Makes colored chat work only with the right permission.
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onColoredChat(ChannelMessageEvent event)
	{
		ProxiedPlayer player = event.getSender().getPlayer();
		if (!event.isCancelled() &&  player != null)
		{
			boolean color = player.hasPermission("heavenchat.chat.color");
			boolean bold = player.hasPermission("heavenchat.chat.bold");
			boolean italic = player.hasPermission("heavenchat.chat.italic");
			boolean underline = player.hasPermission("heavenchat.chat.underline");
			boolean strikethrough = player.hasPermission("heavenchat.chat.strikethrough");
			boolean magic = player.hasPermission("heavenchat.chat.magic");
			boolean hex = player.hasPermission("heavenchat.chat.hex");

			event.setMessage(ChatManager.format(event.getMessage(), color, bold, italic, underline, strikethrough,
					magic, hex));
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onColoredPM(PrivateMessageEvent event)
	{
		ProxiedPlayer player = event.getSender().getPlayer();
		if (!event.isCancelled() && player != null)
		{
			boolean color = player.hasPermission("heavenchat.chat.color");
			boolean bold = player.hasPermission("heavenchat.chat.bold");
			boolean italic = player.hasPermission("heavenchat.chat.italic");
			boolean underline = player.hasPermission("heavenchat.chat.underline");
			boolean strikethrough = player.hasPermission("heavenchat.chat.strikethrough");
			boolean magic = player.hasPermission("heavenchat.chat.magic");
			boolean hex = player.hasPermission("heavenchat.chat.hex");

			event.setMessage(ChatManager.format(event.getMessage(), color, bold, italic, underline, strikethrough,
					magic, hex));
		}
	}
	
	/*
	 * Fill message with appropriate color per rank.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onColoredRankChat(ChannelMessageEvent event)
	{
		if (!event.isCancelled())
		{
			if (!event.getChannel().hasRankOverride())
			{
				return;
			}

			ProxiedPlayer player = event.getSender().getPlayer();
			for (String option : config.getStringList("rank-colors"))
			{
				String[] keyRank = option.split(" ");
				if (player.hasPermission("heavenchat.rankcolor." + keyRank[0]))
				{
					event.setMessage(keyRank[1] + event.getMessage());
					return;
				}
			}
		}
	}
	
	/*
	 * Ping players when they receive a message to them
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPingChat(ChannelMessageReceiveEvent event)
	{
		if (!event.isCancelled() && config.getBoolean("ping.enabled"))
		{
			if (config.getBoolean("highlight.mention") && event.getTarget().getPlayer().hasPermission("heavenchat.ping.chat"))
			{
				Member target = event.getTarget();
				
				String rawMessage = ChatManager.stripNonAlphaNumeric(ChatManager.stripColors(event.getMessage().toLowerCase()));
				String rawNick = ChatManager.stripNonAlphaNumeric(ChatManager.stripColors(target.getNickname().toLowerCase()));
				String rawUser = ChatManager.stripNonAlphaNumeric(ChatManager.stripColors(target.getUsername().toLowerCase()));
				
				if ((!rawNick.equals("") & rawMessage.contains(rawNick)) || rawMessage.contains(rawUser))
				{
					event.setMessage(config.getString("highlight.color") + event.getMessage());
					new SendPingTask(target.getPlayer()).run();
					return;
				}
				
				for (String keyword : config.getStringList("highlight.keywords"))
				{
					if (rawMessage.contains(keyword.toLowerCase()))
					{
						event.setMessage(config.getString("highlight.color") + event.getMessage());
						new SendPingTask(target.getPlayer()).run();
						return;
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPingSpy(SpyMessageEvent event)
	{
		if (!event.isCancelled() && config.getBoolean("ping.enabled"))
		{
			if (config.getBoolean("highlight.mention") && event.getTarget().getPlayer().hasPermission("heavenchat.ping.chat"))
			{
				Member spy = event.getSpy();
				
				String rawMessage = ChatManager.stripNonAlphaNumeric(ChatManager.stripColors(event.getMessage().toLowerCase()));
				String rawNick = ChatManager.stripNonAlphaNumeric(ChatManager.stripColors(spy.getNickname().toLowerCase()));
				String rawUser = ChatManager.stripNonAlphaNumeric(ChatManager.stripColors(spy.getUsername().toLowerCase()));
				
				if ((!rawNick.equals("") & rawMessage.contains(rawNick)) || rawMessage.contains(rawUser))
				{
					event.setMessage(config.getString("highlight.color") + event.getMessage());
					new SendPingTask(spy.getPlayer()).run();
					return;
				}
				
				for (String keyword : config.getStringList("highlight.keywords"))
				{
					if (rawMessage.contains(keyword.toLowerCase()))
					{
						event.setMessage(config.getString("highlight.color") + event.getMessage());
						new SendPingTask(spy.getPlayer()).run();
						return;
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPingPM(PrivateMessageEvent event)
	{
		if (!event.isCancelled() && config.getBoolean("ping.enabled"))
		{
			ProxiedPlayer sender = event.getSender().getPlayer();
			ProxiedPlayer target = event.getTarget().getPlayer();
			
			if (sender.hasPermission("heavenchat.ping.pm"))
				new SendPingTask(sender).run();
			if (target.hasPermission("heavenchat.ping.pm"))
				new SendPingTask(target).run();
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onChannelNickClear(ChannelMessageEvent event)
	{
		Member member = event.getSender();
		ProxiedPlayer player = member.getPlayer();
		if (player == null)
		{
			return;
		}

		if (!player.hasPermission("heavenchat.nickname") && !member.getNickname().isEmpty())
		{
			member.setNickname("");
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPMNickClear(PrivateMessageEvent event)
	{
		Member member = event.getSender();
		ProxiedPlayer player = member.getPlayer();
		if (player == null)
		{
			return;
		}

		if (!player.hasPermission("heavenchat.nickname") && !member.getNickname().isEmpty())
		{
			member.setNickname("");
		}
	}

	/*
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerTabComplete(TabCompleteEvent event)
	{
		List<String> commands = config.getStringList("commands");
		int firstSpace = event.getCursor().charAt(' ');
		String command = event.getCursor().toLowerCase().substring(0, firstSpace);

		if (commands.contains(command))
		{
			String partialPlayerName = event.getCursor().toLowerCase();
			int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');
			if (lastSpaceIndex >= 0)
			{
				partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
			}

			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
			{
				if (player.getName().toLowerCase().startsWith(partialPlayerName)
						&& !event.getSuggestions().contains(player.getName()))
				{
					event.getSuggestions().add(player.getName());
				}
			}
		}
	}
	 */
}
