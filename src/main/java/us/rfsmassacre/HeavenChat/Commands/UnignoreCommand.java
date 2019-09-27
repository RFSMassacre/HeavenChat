package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class UnignoreCommand extends HeavenCommand
{
	private MemberManager members;
	
	public UnignoreCommand() 
	{
		super("unignore", "unblock");
		
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new UnignoreMemberCommand(this);
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "ignore.invalid-args");
	}
	
	private class UnignoreMemberCommand extends SubCommand
	{
		public UnignoreMemberCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!isConsole(sender))
			{
				if (args.length >= 1)
				{
					Member member = members.getMember((ProxiedPlayer)sender);
					Member target = members.findMember(args[0]);
					
					if (target != null)
					{
						if (member.equals(target))
						{
							locale.sendLocale(sender, "ignore.cant-self");
						}
						else if (member.hasIgnored(target))
						{
							member.removeIgnoredMember(target);
							locale.sendLocale(sender, "ignore.removed", "{target}", target.getDisplayName());
						}
						else
						{
							locale.sendLocale(sender, "ignore.not-ignored", "{target}", target.getDisplayName());
						}
					}
					else
					{
						locale.sendLocale(sender, "error.not-found", "{arg}", args[0]);
					}
					
					return;
				}
				
				//Invalid args
				locale.sendLocale(sender, "ignore.invalid-args");
				return;
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
}
