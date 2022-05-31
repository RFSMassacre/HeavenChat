package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class FilterCommand extends HeavenCommand
{
	private MemberManager members;
	
	public FilterCommand() 
	{
		super("togglefilter", "tf");
		
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new ToggleCommand(this);
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "error.invalid-args");
	}
	
	private class ToggleCommand extends SubCommand
	{
		public ToggleCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onRun(CommandSender sender, String[] args)
		{
			if (!isConsole(sender))
			{
				Member member = members.getMember((ProxiedPlayer)sender);
				
				if (member.isFiltered())
				{
					member.setFiltered(false);
					locale.sendLocale(sender, "filter.profanity.disabled");
				}
				else
				{
					member.setFiltered(true);
					locale.sendLocale(sender, "filter.profanity.enabled");
				}
				
				return;
			}
			
			//Console error
			locale.sendLocale(sender, "error.console");
		}
	}
}
