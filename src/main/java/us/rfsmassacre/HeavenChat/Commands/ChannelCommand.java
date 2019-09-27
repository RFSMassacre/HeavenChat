package us.rfsmassacre.HeavenChat.Commands;

import java.util.ArrayList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Managers.ChannelManager;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class ChannelCommand extends HeavenCommand
{	
	private ChannelManager channels;
	private MemberManager members;
	
	public ChannelCommand() 
	{
		super("channel", "ch");
		
		channels = ChatPlugin.getChannelManager();
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new CurrentListCommand(this);
		subCommands.add(this.new AvailableListCommand(this));
		subCommands.add(this.new JoinCommand(this));
		subCommands.add(this.new LeaveCommand(this));
		subCommands.add(this.new AddCommand(this));
		subCommands.add(this.new KickCommand(this));
		subCommands.add(this.new FocusCommand(this));
		subCommands.add(this.new HelpCommand(this));
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "channel.invalid-args");
	}
	
	/*
	 * Current Channels List
	 */
	private class CurrentListCommand extends SubCommand
	{
		public CurrentListCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!isConsole(sender))
			{
				ArrayList<String> currentChannels = new ArrayList<String>();
				
				Member member = members.getMember((ProxiedPlayer)sender);
				for (Channel channel : channels.getAllChannels())
				{
					if (channel.hasMember(member))
						currentChannels.add(channel.getDisplayName());
				}
				
				locale.sendLocale(sender, "channel.current-list", "{channels}", String.join("&f, ", currentChannels));
				return;
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
	
	/*
	 * Available Channels List
	 */
	private class AvailableListCommand extends SubCommand
	{
		public AvailableListCommand(ProxyCommand command) 
		{
			super(command, "list");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!isConsole(sender))
			{
				ArrayList<String> availableChannels = new ArrayList<String>();
				
				Member member = members.getMember((ProxiedPlayer)sender);
				for (Channel channel : channels.getAllChannels())
				{
					if (channel.canJoin(member))
						availableChannels.add(channel.getDisplayName());
				}
				
				locale.sendLocale(member.getPlayer(), "channel.available-list", "{channels}", String.join("&f, ", availableChannels));
				return;
			}
			
			//Send console error
			locale.sendLocale(sender, "invalid.console");
		}
	}
	
	/*
	 * Channel Join
	 */
	private class JoinCommand extends SubCommand
	{
		public JoinCommand(ProxyCommand command) 
		{
			super(command, "join");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!isConsole(sender))
			{
				if (args.length >= 2)
				{
					Channel channel = channels.getChannel(args[1]);
					Member member = members.getMember((ProxiedPlayer)sender);
					
					if (channel != null)
					{
						if (channel.canJoin(member))
						{
							//Add to channel
							channel.addMember(member);
							member.setFocusedChannel(channel);
							member.setFocusedMember(null);
							locale.sendLocale(sender, "channel.joined", "{channel}", channel.getDisplayName());
							return;
						}
						
						//No perm error
						locale.sendLocale(sender, "channel.no-perm", "{channel}", channel.getDisplayName());
						return;
					}
					
					//Send channel not found error
					locale.sendLocale(sender, "channel.not-found", "{arg}", args[1]);
					return;
				}
				
				//Invalid arg error
				locale.sendLocale(sender, "error.invalid-args");
				return;
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
	
	/*
	 * Channel Leave
	 */
	private class LeaveCommand extends SubCommand
	{
		public LeaveCommand(ProxyCommand command) 
		{
			super(command, "leave");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!isConsole(sender))
			{
				if (args.length >= 2)
				{
					Channel channel = channels.getChannel(args[1]);
					Member member = members.getMember((ProxiedPlayer)sender);
					
					if (channel != null)
					{
						if (channel.canLeave(member))
						{
							//Add to channel
							channel.removeMember(member);
							locale.sendLocale(sender, "channel.left", "{channel}", channel.getDisplayName());
							
							//Clear focused channel only if it's the one member is leaving
							if (channel.equals(member.getFocusedChannel()))
								member.setFocusedChannel(null);
							
							return;
						}
						
						//No perm error
						locale.sendLocale(sender, "channel.no-perm", "{channel}", channel.getDisplayName());
						return;
					}
					
					//Send channel not found error
					locale.sendLocale(sender, "channel.not-found", "{arg}", args[1]);
					return;
				}
				
				//Invalid arg error
				locale.sendLocale(sender, "error.invalid-args");
				return;
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
	
	/*
	 * Channel Kick
	 */
	private class KickCommand extends SubCommand
	{
		public KickCommand(ProxyCommand command) 
		{
			super(command, "kick");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (args.length >= 2)
			{
				Channel channel = channels.getChannel(args[2]);
				String memberName = args[1];
				Member member = members.findMember(memberName);
				
				if (channel == null)
				{
					//Send channel not found error
					locale.sendLocale(sender, "channel.not-found", "{arg}", args[1]);
					return;
				}
				
				if (member == null)
				{
					//Member not found error
					locale.sendLocale(sender, "channel.member-not-found", "{arg}", memberName);
					return;
				}
				
				if (!channel.hasMember(member))
				{
					//Give not in channel error
					locale.sendLocale(sender, "channel.not-in-channel", "{member}", member.getDisplayName(), 
							   "{channel}", channel.getDisplayName());
					return;
				}
				
				if (channel.canLeave(member))
				{
					//Remove from channel
					channel.removeMember(member, true, true);
					locale.sendLocale(member.getPlayer(), "channel.kicked", "{channel}", channel.getDisplayName());
					
					//Clear focused channel only if it's the one member is leaving
					if (channel.equals(member.getFocusedChannel()))
						member.setFocusedChannel(null);
					
					return;
				}
				
				//No perm error
				locale.sendLocale(sender, "channel.no-perm", "{channel}", channel.getDisplayName());
				return;
			}
			
			//Invalid arg error
			locale.sendLocale(sender, "error.invalid-args");
			return;
		}
	}
	
	/*
	 * Channel Add
	 */
	private class AddCommand extends SubCommand
	{
		public AddCommand(ProxyCommand command) 
		{
			super(command, "add");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (args.length >= 3)
			{
				Channel channel = channels.getChannel(args[2]);
				String memberName = args[1];
				Member member = members.findMember(memberName);
				
				if (channel == null)
				{
					//Send channel not found error
					locale.sendLocale(sender, "channel.not-found", "{arg}", args[1]);
					return;
				}
				if (member == null)
				{
					//Member not found error
					locale.sendLocale(sender, "channel.member-not-found", "{arg}", memberName);
					return;
				}
				
				if (channel.hasMember(member))
				{
					//Give already in channel error
					locale.sendLocale(sender, "channel.already-in-channel", "{member}", member.getDisplayName(), 
							   "{channel}", channel.getDisplayName());
					return;
				}
				
				if (channel.canJoin(member))
				{
					//Add to channel
					channel.addMember(member);
					locale.sendLocale(member.getPlayer(), "channel.added", "{channel}", channel.getDisplayName());
					
					member.setFocusedChannel(channel);
					member.setFocusedMember(null);
					return;
				}
				
				
				//No perm error
				locale.sendLocale(sender, "target-channel.no-perm", "{target}", member.getDisplayName(), 
						 									   "{channel}", channel.getDisplayName());
				return;
			}
			
			//Invalid arg error
			locale.sendLocale(sender, "error.invalid-args");
			return;
		}
	}
	
	/*
	 * Channel Focus Command
	 */
	private class FocusCommand extends SubCommand
	{
		public FocusCommand(ProxyCommand command) 
		{
			super(command, "focus");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (args.length >= 3)
			{
				Channel channel = channels.getChannel(args[2]);
				String memberName = args[1];
				Member member = members.findMember(memberName);
				
				if (channel == null)
				{
					//Send channel not found error
					locale.sendLocale(sender, "channel.not-found", "{arg}", args[1]);
					return;
				}
				if (member == null)
				{
					//Member not found error
					locale.sendLocale(sender, "channel.member-not-found", "{arg}", memberName);
					return;
				}
				
				if (channel.hasMember(member))
				{
					//Set their focused channel to this one
					if (!member.getFocusedChannel().equals(channel))
					{
						member.setFocusedChannel(channel);
						
						locale.sendLocale(member.getPlayer(), "channel.focused", "{channel}", channel.getDisplayName());
						
						locale.sendLocale(sender, "channel.focuse-target", "{target}", member.getDisplayName(), 
								   								"{channel}", channel.getDisplayName());
						return;
					}
					
					//Already focused error
					locale.sendLocale(sender, "channel.already-focused-target", "{target}", member.getDisplayName(), 
																"{channel}", channel.getDisplayName());
					return;
				}
				
				
				//No perm error
				locale.sendLocale(sender, "channel.target-no-perm", "{target}", member.getDisplayName(), 
						 									   "{channel}", channel.getDisplayName());
				return;
			}
			
			//Invalid arg error
			locale.sendLocale(sender, "error.invalid-args");
			return;
		}
	}
	
	/*
	 * Channel List
	 */
	private class ListCommand extends SubCommand
	{

		public ListCommand(ProxyCommand command) 
		{
			super(command, "list");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (args.length >= 3)
			{
				
			}
			
			//Invalid arg error
			locale.sendLocale(sender, "error.invalid-args");
			return;
		}
	}
	
	/*
	 * Channel Help
	 */
	private class HelpCommand extends SubCommand
	{
		public HelpCommand(ProxyCommand command) 
		{
			super(command, "help");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			locale.sendLocale(sender, "help.channel");
		}
	}
}
