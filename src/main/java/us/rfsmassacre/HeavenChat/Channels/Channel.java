package us.rfsmassacre.HeavenChat.Channels;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Commands.FocusCommand;
import us.rfsmassacre.HeavenChat.Events.ChannelJoinEvent;
import us.rfsmassacre.HeavenChat.Events.ChannelLeaveEvent;
import us.rfsmassacre.HeavenChat.Events.ChannelMessageEvent;
import us.rfsmassacre.HeavenChat.Members.Member;

public class Channel
{
	public static enum ChannelType
	{
		GLOBAL, SERVER, LOCAL;
		
		public static ChannelType fromString(String name)
		{
			for (ChannelType type : ChannelType.values())
			{
				if (type.toString().equalsIgnoreCase(name))
					return type;
			}
			
			return null;
		}
	}
	
	private String name; //Name of channel
	private String displayName; //Display name of channel
	private ChannelType type;
	private boolean forwarded;
	
	private String shortcut; //Shortcut to join or focus on this channel
	private String format; //Format of the text
	
	private ArrayList<UUID> memberIds; //List of members that are involved with this channel
	private FocusCommand command; //Command for this channel
	
	public Channel()
	{
		setName("");
		setDisplayName("");
		setShortcut("");
		setFormat("");
		setForwarded(true);
		setMemberIds(new ArrayList<UUID>());
	}

	/*
	 * Setters and Getters
	 */
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getDisplayName() 
	{
		return displayName;
	}
	public void setDisplayName(String displayName) 
	{
		this.displayName = displayName;
	}

	public ChannelType getType() 
	{
		return type;
	}
	public void setType(ChannelType type) 
	{
		this.type = type;
	}
	
	public boolean isForwarded() 
	{
		return forwarded;
	}
	public void setForwarded(boolean forwarded) 
	{
		this.forwarded = forwarded;
	}

	public String getShortcut() 
	{
		return shortcut;
	}
	public void setShortcut(String shortcut) 
	{
		this.shortcut = shortcut;
	}

	public String getFormat() 
	{
		return format;
	}
	public void setFormat(String format) 
	{
		this.format = format;
	}
	
	public ArrayList<UUID> getMemberIds() 
	{
		return memberIds;
	}
	public ArrayList<String> getMemberStringIds()
	{
		//Easy method to store string values of member IDs
		ArrayList<String> stringIds = new ArrayList<String>();
		for (UUID memberId : memberIds)
		{
			stringIds.add(memberId.toString());
		}
		return stringIds;
	}
	public void setMemberIds(ArrayList<UUID> memberIds) 
	{
		this.memberIds = memberIds;
	}
	public void setMemberStringIds(List<String> memberStringIds)
	{
		//Easy method to load and convert string values of member IDs
		this.memberIds = new ArrayList<UUID>();
		for (String memberStringId : memberStringIds)
		{
			memberIds.add(UUID.fromString(memberStringId));
		}
	}
	public void addMember(Member member, boolean callEvent)
	{	
		if (!hasMember(member))
		{
			memberIds.add(member.getPlayerId());
			
			if (callEvent)
			{
				ChannelJoinEvent event = new ChannelJoinEvent(member, this);
				ProxyServer.getInstance().getPluginManager().callEvent(event);
			}
		}
	}
	public void addMember(Member member)
	{
		addMember(member, true);
	}
	public void removeMember(Member member, boolean kicked, boolean callEvent)
	{	
		if (hasMember(member))
		{
			memberIds.remove(member.getPlayerId());
			
			if (callEvent)
			{
				ChannelLeaveEvent event = new ChannelLeaveEvent(member, this, kicked);
				ProxyServer.getInstance().getPluginManager().callEvent(event);
			}
		}
	}
	public void removeMember(Member member, boolean callEvent)
	{
		removeMember(member, false, callEvent);
	}
	public void removeMember(Member member)
	{
		removeMember(member, false, true);
	}
	public boolean hasMember(Member member)
	{
		return memberIds.contains(member.getPlayerId());
	}
	public ArrayList<Member> getOnlineMembers()
	{
		ArrayList<Member> onlineMembers = new ArrayList<Member>();
		for (UUID memberId : memberIds)
		{
			Member member = ChatPlugin.getMemberManager().getMember(memberId);
			if (member != null)
				onlineMembers.add(member);
		}
		return onlineMembers;
	}
	public void clearMembers()
	{
		memberIds.clear();
	}
	
	public FocusCommand getCommand()
	{
		return command;
	}
	private void createCommand()
	{
		this.command = new FocusCommand(this);
	}
	//Intended to be used on reload
	public void registerCommand()
	{
		createCommand();
		ProxyServer.getInstance().getPluginManager().registerCommand(ChatPlugin.getInstance(), this.command);
	}
	public void unregisterCommand()
	{
		ProxyServer.getInstance().getPluginManager().unregisterCommand(this.command);
	}
	
	/*
	 * Functions
	 */
	private void sendGlobalMessage(Member sender, String message)
	{
		for (Member member : getOnlineMembers())
		{
			member.sendChannelMessage(this, sender, message);
		}
	}
	private void sendServerMessage(Member sender, String message)
	{
		for (ProxiedPlayer player : sender.getPlayer().getServer().getInfo().getPlayers())
		{
			Member member = ChatPlugin.getMemberManager().getMember(player);
			if (member != null && hasMember(member))
			{
				member.sendChannelMessage(this, sender, message);
			}
		}
	}
	private void sendLocalMessage(Member sender, String message)
	{
		for (UUID playerId : sender.getProximityPlayerIds())
		{
			Member member = ChatPlugin.getMemberManager().getMember(playerId);
			if (member != null && hasMember(member))
			{
				member.sendChannelMessage(this, sender, message);
			}
		}
	}
	public void sendMessage(Member sender, String message)
	{
		//Does not send if another plugin cancels this event
		ChannelMessageEvent event = new ChannelMessageEvent(this, sender, message);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		
		if (!event.isCancelled())
		{
			//Automatically add member to the channel if they aren't part of it
			if (!hasMember(sender))
				addMember(sender, false);
			
			switch (type)
			{
				case GLOBAL:
					sendGlobalMessage(sender, event.getMessage());
					break;
				case SERVER:
					sendServerMessage(sender, event.getMessage());
					break;
				case LOCAL:
					sendLocalMessage(sender, event.getMessage());
					break;
			}
		}
	}
	
	public boolean canJoin(Member member)
	{
		return member.getPlayer().hasPermission("heavenchat.channel." + name);
	}
	public boolean canLeave(Member member)
	{
		return member.getPlayer().hasPermission("heavenchat.leave." + name);
	}
	
	/*
	 * Validator
	 */
	public static boolean isValid(Channel channel)
	{
		if (channel.getName() == null || channel.getName().equals(""))
			return false;
		else if (channel.getDisplayName() == null || channel.getDisplayName().equals(""))
			return false;
		else if (channel.getFormat() == null || channel.getFormat().equals(""))
			return false;
		else if (channel.getShortcut() == null || channel.getShortcut().equals(""))
			return false;
		else
			return true;
	}
}
