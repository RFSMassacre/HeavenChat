package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class SpyCommand extends HeavenCommand
{
	private MemberManager members;
	
	public SpyCommand() 
	{
		super("socialspy", "ss");
		
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new SpyToggleCommand(this);
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "error.invalid-args");
	}
	
	private class SpyToggleCommand extends SubCommand
	{
		public SpyToggleCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{	
			if (!(isConsole(sender)))
			{
				Member member = members.getMember((ProxiedPlayer)sender);
				
				if (!member.isSpying())
				{
					member.setSpying(true);
					locale.sendLocale(sender, "social-spy.enabled");
				}
				else
				{
					member.setSpying(false);
					locale.sendLocale(sender, "social-spy.disabled");
				}
				
				return;
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
}
