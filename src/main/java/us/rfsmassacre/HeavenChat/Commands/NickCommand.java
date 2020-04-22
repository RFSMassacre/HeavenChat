package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ChatManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ConfigManager;

public class NickCommand extends HeavenCommand
{
	private MemberManager members;
	
	public NickCommand() 
	{
		super("nickname", "nick");
		
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new CurrentNameCommand(this);
		
		subCommands.add(this.new HelpCommand(this));
		subCommands.add(this.new ClearNameCommand(this));	
		subCommands.add(this.new ChangeNameCommand(this));
		subCommands.add(this.new UpdateNameCommand(this));
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "nickname.invalid-args");
	}
	
	/*
	 * Show Current Nickname
	 */
	private class CurrentNameCommand extends SubCommand
	{
		public CurrentNameCommand(ProxyCommand command)
		{
			super(command, "");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!isConsole(sender))
			{
				Member member = members.getMember((ProxiedPlayer)sender);
				if (member.getNickname() != null && !member.getNickname().equals(""))
				{
					locale.sendLocale(sender, "nickname.current-nick", "{nickname}", member.getNickname());
				}
				else
				{
					locale.sendLocale(sender, "nickname.no-nick");
				}
				
				return;
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}	
	}
	
	/*
	 * Update Current Nickname
	 */
	private class UpdateNameCommand extends SubCommand
	{
		public UpdateNameCommand(ProxyCommand command) 
		{
			//Place it at the end of the list so all other possible commands go first
			super(command, "");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (!isConsole(sender) && args.length >= 1)
			{
				Member member = members.getMember((ProxiedPlayer)sender);
				String nickname = args[0] + ChatColor.RESET;
				String rawNickname = ChatManager.stripColors(nickname);
				
				ConfigManager config = ChatPlugin.getConfigManager();
				int min = config.getInt("nicknames.min-length");
				int max = config.getInt("nicknames.max-length");
				
				if (rawNickname.length() < min || rawNickname.length() > max)
				{
					//Send wrong size error
					locale.sendLocale(sender, "nickname.wrong-size", "{min}", Integer.toString(min),
							"{max}", Integer.toString(max));
					return;
				}
				
				Member matchedMember = members.matchNickname(nickname);
				if (matchedMember == null || matchedMember.equals(member))
				{
					member.setNickname(nickname);
					members.saveMember(member);
					locale.sendLocale(sender, "nickname.update-nick", "{nickname}", member.getNickname());
				}
				else
				{
					locale.sendLocale(sender, "nickname.taken", "{nickname}", matchedMember.getNickname(),
							"{player}", matchedMember.getPrefix() + matchedMember.getUsername());
				}
				
				return;
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
	
	/*
	 * Clear Current Nickname
	 */
	private class ClearNameCommand extends SubCommand
	{
		public ClearNameCommand(ProxyCommand command) 
		{
			super(command, "clear");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (args.length == 1)
			{
				if (!isConsole(sender))
				{
					Member member = members.getMember((ProxiedPlayer)sender);
					if (member.getNickname() == null || member.getNickname().equals(""))
					{
						locale.sendLocale(sender, "nickname.no-nick");
					}
					else
					{
						member.setNickname("");
						members.saveMember(member);
						locale.sendLocale(sender, "nickname.clear-nick");
					}
					
					return;
				}
				
				//Send console error
				locale.sendLocale(sender, "error.console");
			}
			else
			{
				if (sender.hasPermission("heavenchat.nickname.others"))
				{
					Member member = members.findMember(args[1]);
					if (member != null)
					{
						if (member.getNickname() == null || member.getNickname().equals(""))
						{
							locale.sendLocale(sender, "nickname.no-nick-others", "{target}", member.getDisplayName());
						}
						else
						{
							member.setNickname("");
							members.saveMember(member);
							locale.sendLocale(sender, "nickname.clear-nick-others", "{target}", member.getDisplayName());
						}
						
						return;
					}
					
					//Send target not found error
					locale.sendLocale(sender, "error.not-found", "{arg}", args[1]);
					return;
				}
				
				locale.sendLocale(sender, "error.no-perm");
			}
		}
	}
	
	/*
	 * Change Nickname - Admin Command
	 */
	private class ChangeNameCommand extends SubCommand
	{
		public ChangeNameCommand(ProxyCommand command) 
		{
			super(command, "change");
			
			//Overriding default perm to be consistent with the other perms
			this.permission = "heavenchat.nickname.others";
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			if (args.length >= 3)
			{
				Member target = members.findMember(args[1]);
				String nickname = args[2] + ChatColor.RESET;
				String rawNickname = ChatManager.stripColors(nickname);
				
				ConfigManager config = ChatPlugin.getConfigManager();
				int min = config.getInt("nicknames.min-length");
				int max = config.getInt("nicknames.max-length");
				
				if (target != null)
				{
					if (rawNickname.length() < min || rawNickname.length() > max)
					{
						//Send wrong size error
						locale.sendLocale(sender, "nickname.wrong-size", "{min}", Integer.toString(min),
								"{max}", Integer.toString(max));
						return;
					}
					
					Member matchedMember = members.matchNickname(nickname);
					if (matchedMember == null || matchedMember.equals(target))
					{
						target.setNickname(nickname);
						members.saveMember(target);
						locale.sendLocale(sender, "nickname.update-nick-others", "{target}", 
								target.getPrefix() + target.getUsername(), "{nickname}", target.getNickname());
					}
					else
					{
						locale.sendLocale(sender, "nickname.taken", "{nickname}", matchedMember.getNickname(),
								"{player}", matchedMember.getPrefix() + matchedMember.getUsername());
					}
					
					return;
				}
				
				//Send target not found error
				locale.sendLocale(sender, "error.not-found", "{arg}", args[1]);
				return;
			}
		}
	}
	
	/*
	 * Nick Help
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
			locale.sendLocale(sender, "help.nickname");
		}
	}
}
