package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MenuManager;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class ListCommand extends HeavenCommand
{
	public ListCommand() 
	{
		super("list");
		
		mainCommand = this.new ListAllPlayersCommand(this);
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "error.invalid-args");
	}
	
	private class ListAllPlayersCommand extends SubCommand
	{
		public ListAllPlayersCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onCommandRun(CommandSender sender, String[] args) 
		{
			MenuManager list = ChatPlugin.getMenuManager();
			locale.sendMessage(sender, list.getListMenu());
		}
	}
}
