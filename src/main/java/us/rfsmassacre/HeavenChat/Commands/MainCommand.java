package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MenuManager;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class MainCommand extends HeavenCommand
{
	public MainCommand() 
	{
		super("heavenchat", "hc");
		
		mainCommand = this.new HelpCommand(this);
		
		subCommands.add(this.new ReloadCommand(this));
		subCommands.add(this.new PurgeCommand(this));
		subCommands.add(this.new VersionCommand(this));
		subCommands.add(this.new HelpCommand(this));
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "error.invalid-args");
	}

	/*
	 * Version Command
	 */
	private class VersionCommand extends SubCommand
	{
		public VersionCommand(ProxyCommand command) 
		{
			super(command, "version");
			
			this.permission = "heavenchat.admin";
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args)
		{
			String version = ChatPlugin.getInstance().getDescription().getVersion();
			locale.sendLocale(sender, "admin.info", "{version}", version);
		}
	}
	
	/*
	 * Reload Command
	 */
	private class ReloadCommand extends SubCommand
	{
		public ReloadCommand(ProxyCommand command) 
		{
			super(command, "reload");
			
			this.permission = "heavenchat.admin";
		}
		
		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			//Reloads config and locale
			ChatPlugin.getConfigManager().reloadFiles();
			ChatPlugin.getLocaleManager().reloadFiles();
			ChatPlugin.getMenuManager().reloadFiles();
			
			//Reloads channel settings (list of members may roll back.)
			ChatPlugin.getChannelManager().clearAllChannels();
			ChatPlugin.getChannelManager().loadAllChannels();
			
			locale.sendLocale(sender, "admin.reload");
		}
	}
	
	/*
	 * Purge Command
	 */
	private class PurgeCommand extends SubCommand
	{
		public PurgeCommand(ProxyCommand command) 
		{
			super(command, "purge");
			
			this.permission = "heavenchat.admin";
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			ChatPlugin.getMemberManager().purgeBrokenMembers();
			locale.sendLocale(sender, "admin.purge");
		}
	}
	
	/*
	 * Help Command
	 */
	private class HelpCommand extends SubCommand
	{
		public HelpCommand(ProxyCommand command) 
		{
			super(command, "help");
			
			this.permission = "heavenchat.help";
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			MenuManager menu = ChatPlugin.getMenuManager();
			
			if (args.length >= 2)
			{
				try
				{
					int page = Integer.parseInt(args[1]);
					locale.sendMessage(sender, menu.getHelpMenu(page));
					return;
				}
				catch (NumberFormatException exception)
				{
					//Falls back on first page
				}
			}
			
			locale.sendMessage(sender, menu.getHelpMenu(1));
		}
	}
}
