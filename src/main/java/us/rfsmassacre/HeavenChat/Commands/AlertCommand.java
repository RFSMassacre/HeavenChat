package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ChatManager;

public class AlertCommand extends HeavenCommand
{
	public AlertCommand() 
	{
		super("alert", "broadcast", "announce");
		
		mainCommand = this.new MessageCommand(this);
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "error.invalid-args");
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
			if (isConsole(sender))
			{
				//Broadcast as is, without a prefix
				ProxyServer.getInstance().broadcast(ChatManager.toTextComponent(String.join(" ", args)));
			}
			else
			{
				//Broadcast with a prefix
				locale.broadcastMessage(String.join(" ", args));
			}
		}
	}
}
