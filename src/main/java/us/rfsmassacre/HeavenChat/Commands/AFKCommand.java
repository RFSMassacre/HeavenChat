package us.rfsmassacre.HeavenChat.Commands;

import java.util.HashMap;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ConfigManager;

public class AFKCommand extends HeavenCommand
{
	private ConfigManager config;
	private MemberManager members;
	private HashMap<Member, Long> afkTime;
	
	public AFKCommand() 
	{
		super("afk", "away");
		
		config = ChatPlugin.getConfigManager();
		members = ChatPlugin.getMemberManager();
		afkTime = new HashMap<Member, Long>();
		
		mainCommand = this.new AFKToggleCommand(this);
	}
	
	private long getTime(Member member)
	{
		return afkTime.get(member) != null ? afkTime.get(member) : System.currentTimeMillis();
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "error.invalid-args");
	}
	
	private class AFKToggleCommand extends SubCommand
	{
		public AFKToggleCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!(isConsole(sender)))
			{
				Member member = members.getMember((ProxiedPlayer)sender);
				int timeLeft = (int)(((config.getInt("afk.cooldown") * 1000) - (System.currentTimeMillis() - getTime(member))) / 1000);
				
				if (timeLeft <= 0 || timeLeft == config.getInt("afk.cooldown"))
				{
					if (!member.isAFK())
					{
						member.setAFK(true);
						locale.broadcastLocale(false, "afk.away", "{player}", member.getDisplayName());
					}
					else
					{
						member.setAFK(false);
						locale.broadcastLocale(false, "afk.returned", "{player}", member.getDisplayName());
					}
					
					afkTime.put(member, System.currentTimeMillis());
					return;
				}
				else
				{
					locale.sendLocale(sender, "afk.cooldown", "{seconds}", Integer.toString(timeLeft));
					return;
				}
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
}
