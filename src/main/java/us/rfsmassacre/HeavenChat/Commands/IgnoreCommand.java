package us.rfsmassacre.HeavenChat.Commands;

import java.util.ArrayList;
import java.util.UUID;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class IgnoreCommand extends HeavenCommand
{
	private MemberManager members;
	
	public IgnoreCommand() 
	{
		super("ignore", "block");
		
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new ListCommand(this);
		
		subCommands.add(this.new HelpCommand(this));
		subCommands.add(this.new ListCommand(this));
		subCommands.add(this.new IgnoreMemberCommand(this));
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "ignore.invalid-args");
	}
	
	private class IgnoreMemberCommand extends SubCommand
	{
		public IgnoreMemberCommand(ProxyCommand command) 
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
					Member target = members.findOnlineMember(args[0]);
					
					if (target != null)
					{
						if (member.equals(target))
						{
							locale.sendLocale(sender, "ignore.cant-self");
						}
						else if (target.getPlayer().hasPermission("heavenchat.ignore.immune"))
						{
							locale.sendLocale(sender, "ignore.cant-ignore", "{target}", target.getDisplayName());
						}
						else if (!member.hasIgnored(target))
						{
							member.addIgnoredMember(target);
							locale.sendLocale(sender, "ignore.added", "{target}", target.getDisplayName());
						}
						else
						{
							locale.sendLocale(sender, "ignore.already-added", "{target}", target.getDisplayName());
						}
					}
					else
					{
						locale.sendLocale(sender, "error.not-online", "{arg}", args[0]);
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
	
	/*
	 * Ignore Help
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
			locale.sendLocale(sender, "help.ignore");
		}
	}
	
	/*
	 * Ignore List
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
			Member member = members.getMember((ProxiedPlayer)sender);
			ArrayList<UUID> ignoredIds = member.getIgnoredPlayerIds();
			ArrayList<String> displayNames = new ArrayList<String>();
			
			for (UUID playerId : ignoredIds)
			{
				Member ignoredMember = members.getMember(playerId);
				if (ignoredMember == null)
					ignoredMember = members.getOfflineMember(playerId);
				
				displayNames.add(ignoredMember.getDisplayName());
			}
			
			if (displayNames.isEmpty())
				locale.sendLocale(sender, "ignore.empty");
			else
				locale.sendLocale(sender, "ignore.list", "{list}", String.join("&7, ", displayNames));
		}
	}
}
