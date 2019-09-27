package us.rfsmassacre.HeavenChat.Members;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Events.ChannelMessageReceiveEvent;
import us.rfsmassacre.HeavenChat.Events.PrivateMessageEvent;
import us.rfsmassacre.HeavenChat.Events.SpyMessageEvent;
import us.rfsmassacre.HeavenChat.Managers.ProfanityManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ConfigManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public class Member 
{	
	private ProxiedPlayer player;
	
	private UUID playerId;
	private String prefix;
	private String suffix;
	private String username;
	private String nickname;
	
	private String server;
	
	private UUID focusedMemberId;
	private UUID lastMemberId;
	private String focusedChannelName;
	private boolean afk;
	private long lastMovement;
	private boolean filtered;
	
	private ArrayList<UUID> ignoredPlayerIds;
	private ArrayList<UUID> proximityPlayerIds;
	private boolean spying;
	
	public Member()
	{
		setPrefix("");
		setSuffix("");
		setUsername("");
		setNickname("");
		setServer("");
		setAFK(false);
		setFiltered(true);
		setLastMovement(System.currentTimeMillis());
		setIgnoredPlayerIds(new ArrayList<UUID>());
		setProximityPlayerIds(new ArrayList<UUID>());
		setSpying(false);
	}
	public Member(ProxiedPlayer player)
	{
		setPrefix("");
		setSuffix("");
		setUsername(player.getName());
		setNickname("");
		setServer("");
		setAFK(false);
		setFiltered(true);
		setLastMovement(System.currentTimeMillis());
		setIgnoredPlayerIds(new ArrayList<UUID>());
		setProximityPlayerIds(new ArrayList<UUID>());
		setSpying(false);
		setPlayer(player);	
	}

	/*
	 * Setters and Getters
	 */
	public ProxiedPlayer getPlayer() 
	{
		return player;
	}
	public void setPlayer(ProxiedPlayer player) 
	{
		this.player = player;
		
		setPlayerId(player.getUniqueId());
		setUsername(player.getName());
		setServer(player.getServer().getInfo().getName());
	}
	
	/*
	 * Intended to be used for offline players as if they were online.
	 */
	public void setServer(String server)
	{
		this.server = server;
	}
	public String getServer()
	{
		return server;
	}
	
	public UUID getPlayerId() 
	{
		return playerId;
	}
	public void setPlayerId(UUID playerId) 
	{
		this.playerId = playerId;
	}
	
	public String getPrefix()
	{
		return prefix;
	}
	public void setPrefix(String prefix) 
	{
		this.prefix = prefix;
	}
	
	public String getSuffix()
	{
		return suffix;
	}
	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}
	
	public String getUsername() 
	{
		return username;
	}
	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	public String getNickname() 
	{
		return nickname;
	}
	public void setNickname(String nickname) 
	{
		this.nickname = nickname;
	}
	
	public String getDisplayName()
	{
		ConfigManager config = ChatPlugin.getConfigManager();
		
		String displayName = config.getString("display-name");
		String nickPrefix = config.getString("nicknames.prefix");
		
		displayName = displayName.replace("{prefix}", prefix);
		displayName = displayName.replace("{name}", !nickname.equals("") ? nickPrefix + nickname : username);
		displayName = displayName.replace("{suffix}", suffix);
		
		return displayName;
	}
	
	public UUID getFocusedMemberId()
	{
		return focusedMemberId;
	}
	public Member getFocusedMember() 
	{
		return focusedMemberId != null ? ChatPlugin.getMemberManager().getMember(focusedMemberId) : null;
	}
	public void setFocusedMember(Member focusedMember) 
	{
		this.focusedMemberId = (focusedMember != null ? focusedMember.getPlayerId() : null);
	}
	
	public UUID getLastMemberId()
	{
		return lastMemberId;
	}
	public Member getLastMember() 
	{
		return lastMemberId != null ? ChatPlugin.getMemberManager().getMember(lastMemberId) : null;
	}
	public void setLastMember(Member lastMember) 
	{
		this.lastMemberId = (lastMember != null ? lastMember.getPlayerId() : null);
	}
	
	//This is so all players retrieve the latest instance of that channel
	public Channel getFocusedChannel() 
	{
		return focusedChannelName != null ? ChatPlugin.getChannelManager().getChannel(focusedChannelName) : null;
	}
	public void setFocusedChannel(Channel focusedChannel) 
	{
		this.focusedChannelName = (focusedChannel != null ? focusedChannel.getName() : null);
	}
	
	public boolean isAFK() 
	{
		return afk;
	}
	public void setAFK(boolean afk) 
	{
		this.afk = afk;
	}
	
	public long getLastMovement() 
	{
		return lastMovement;
	}
	public void setLastMovement(long lastMovement) 
	{
		this.lastMovement = lastMovement;
	}
	
	public boolean isFiltered() 
	{
		return filtered;
	}
	public void setFiltered(boolean filtered) 
	{
		this.filtered = filtered;
	}
	
	public ArrayList<UUID> getIgnoredPlayerIds() 
	{
		return ignoredPlayerIds;
	}
	public ArrayList<String> getIgnoredStringIds()
	{
		ArrayList<String> ignoredStringIds = new ArrayList<String>();
		for (UUID ignoredPlayerId : ignoredPlayerIds)
		{
			ignoredStringIds.add(ignoredPlayerId.toString());
		}
		return ignoredStringIds;
	}
	public void setIgnoredPlayerIds(ArrayList<UUID> ignoredPlayerIds) 
	{
		this.ignoredPlayerIds = ignoredPlayerIds;
	}
	public void setIgnoredStringIds(List<String> list)
	{
		setIgnoredPlayerIds(new ArrayList<UUID>());
		for (String ignoredStringId : list)
		{
			ignoredPlayerIds.add(UUID.fromString(ignoredStringId));
		}
	}
	public boolean hasIgnored(Member member)
	{
		return ignoredPlayerIds.contains(member.getPlayerId());
	}
	public void addIgnoredMember(Member member)
	{
		if (!hasIgnored(member))
			ignoredPlayerIds.add(member.getPlayerId());
	}
	public void removeIgnoredMember(Member member)
	{
		if (hasIgnored(member))
			ignoredPlayerIds.remove(member.getPlayerId());
	}
	
	public ArrayList<UUID> getProximityPlayerIds() 
	{
		return proximityPlayerIds;
	}
	public void setProximityPlayerIds(ArrayList<UUID> proximityPlayerIds) 
	{
		this.proximityPlayerIds = proximityPlayerIds;
	}
	
	public boolean isSpying() 
	{
		return spying;
	}
	public void setSpying(boolean spying) 
	{
		this.spying = spying;
	}
	
	/*
	 * Functions
	 */
	public boolean sendChannelMessage(Channel channel, Member sender, String message)
	{
		ChannelMessageReceiveEvent event = new ChannelMessageReceiveEvent(channel, sender, this, message);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		
		if (!event.isCancelled() && !hasIgnored(sender))
		{
			ConfigManager config = ChatPlugin.getConfigManager();
			LocaleManager locale = ChatPlugin.getLocaleManager();
			ProfanityManager profanity = ChatPlugin.getProfanityManager();
			
			String server = config.getString("servers." + sender.getServer() + ".prefix") != null ? 
					config.getString("servers." + sender.getServer() + ".prefix") : "";
					
		    String color = config.getString("servers." + sender.getServer() + ".color") != null ? 
					config.getString("servers." + sender.getServer() + ".color") : "";
			
					locale.sendMessage(player, channel.getFormat(), "{sender}", sender.getDisplayName(),
					"{prefix}", sender.getPrefix(), "{suffix}", sender.getSuffix(),
					"{server}", server, "{message}", (filtered ? profanity.censorSwears(event.getMessage()) : event.getMessage()),
					"{color}", color);
			
			return true;
		}
		
		return false;
	}
	public boolean sendPrivateMessage(Member sender, String message)
	{
		PrivateMessageEvent event = new PrivateMessageEvent(sender, this, message);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		
		if (!event.isCancelled() && !hasIgnored(sender))
		{
			ConfigManager config = ChatPlugin.getConfigManager();
			LocaleManager locale = ChatPlugin.getLocaleManager();
			ProfanityManager profanity = ChatPlugin.getProfanityManager();
			
			String pmSend = config.getString("pm-formats.pm-send");
			String pmReceive = config.getString("pm-formats.pm-receive");
			
			locale.sendMessage(sender.getPlayer(), pmSend, "{sender}", sender.getDisplayName(), "{receiver}", getDisplayName(), 
					"{message}", (sender.isFiltered() ? profanity.censorSwears(event.getMessage()) : event.getMessage()));
			locale.sendMessage(player, pmReceive, "{sender}", sender.getDisplayName(), "{receiver}", getDisplayName(), 
					"{message}", (filtered ? profanity.censorSwears(event.getMessage()) : event.getMessage()));
			
			setLastMember(sender);
			
			return true;
		}
		
		return false;
	}
	public boolean sendSpyMessage(Member sender, Member receiver, String message)
	{
		SpyMessageEvent event = new SpyMessageEvent(this, sender, receiver, message);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		
		if (!event.isCancelled() && player.hasPermission("heavenchat.socialspy"))
		{
			ConfigManager config = ChatPlugin.getConfigManager();
			LocaleManager locale = ChatPlugin.getLocaleManager();
			ProfanityManager profanity = ChatPlugin.getProfanityManager();
			
			String pmSpy = config.getString("pm-formats.pm-spy");
			
			locale.sendMessage(player, pmSpy, "{sender}", sender.getDisplayName(), "{receiver}", receiver.getDisplayName(), 
					"{message}", (this.isFiltered() ? profanity.censorSwears(event.getMessage()) : event.getMessage()));
			
			return true;
		}
		
		return false;
	}
}
