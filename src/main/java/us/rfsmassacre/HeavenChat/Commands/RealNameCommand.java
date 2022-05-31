package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class RealNameCommand extends HeavenCommand
{
	private MemberManager members;
	
	public RealNameCommand() 
	{
		super("realname");
		
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new ShowNameCommand(this);
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "realname.invalid-args");
	}
	
	private class ShowNameCommand extends SubCommand
	{
		public ShowNameCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onRun(CommandSender sender, String[] args)
		{
			if (args.length >= 1)
			{
				Member matchedMember = members.matchNickname(args[0]);
				
				if (matchedMember != null)
				{
					locale.sendLocale(sender, "realname.current-nick", "{nickname}", matchedMember.getNickname(),
							"{target}", matchedMember.getPrefix() + matchedMember.getUsername());
				}
				else
				{
					locale.sendLocale(sender, "realname.no-nick", "{arg}", args[0]);
				}
				
				return;
			}
			else
			{
				locale.sendLocale(sender, "realname.invalid-args");
				return;
			}
		}
	}
}
