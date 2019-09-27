package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class ReplyCommand extends HeavenCommand
{	
	private MemberManager members;
	
	public ReplyCommand() 
	{
		super("reply", "r");
		
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new MessageCommand(this);
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "pm.invalid-args");
	}
	
	private class MessageCommand extends SubCommand
	{
		public MessageCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!isConsole(sender))
			{
				Member member = members.getMember((ProxiedPlayer)sender);
				
				//Send not chatting error
				if (member.getLastMemberId() == null)
				{
					locale.sendLocale(sender, "pm.not-chatting");
					return;
				}
				
				
				Member target = member.getLastMember();
				//Send not there error
				if (target == null)
				{
					Channel channel = member.getFocusedChannel();
					
					//Get offline display name and channel name to properly tell user the target left
					String targetName = members.getOfflineMember(member.getLastMemberId()).getDisplayName();
					String channelName = (channel != null ? channel.getDisplayName() : "");
					
					locale.sendLocale(member.getPlayer(), "pm.target-left", "{target}", targetName
											                    , "{channel}", channelName);
					
					member.setFocusedMember(null);
					member.setLastMember(null);
					
					return;
				}
				
				//Send self error
				if (target.equals(member))
				{
					locale.sendLocale(sender, "pm.self");
					return;
				}
				
				//Send ignored error
				if (target.hasIgnored(member))
				{
					locale.sendLocale(sender, "pm.ignored", "{target}", target.getDisplayName());
					return;
				}
				
				//Focus last member
				if (args.length == 0)
				{
					member.setFocusedMember(target);
					locale.sendLocale(sender, "pm.chatting", "{target}", target.getDisplayName());
					return;
				}
				//Send message to last member
				else
				{	
					target.sendPrivateMessage(member, String.join(" ", args));
					return;
				}
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
}
