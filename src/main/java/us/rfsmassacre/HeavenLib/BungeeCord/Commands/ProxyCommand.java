package us.rfsmassacre.HeavenLib.BungeeCord.Commands;

import java.util.ArrayList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public abstract class ProxyCommand extends Command
{
	/*
	 * SpigotCommand is structured to avoid using long
	 * if-else chains and instead sets up a list of
	 * sub-commands to cycle through when running.
	 * 
	 * If the sub-command equals the argument it calls
	 * for, then it runs the function to execute.
	 */
	protected SubCommand mainCommand;
	protected ArrayList<SubCommand> subCommands;
	
	protected String permission;
	
	public ProxyCommand(String name, String... aliases) 
	{
		super(name, null, aliases);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{	
		if (mainCommand == null)
		{
			//All commands MUST have a main sub-command.
			return;
		}
		else if (subCommands.isEmpty() || args.length == 0)
		{
			//If no arguments are given, always run the main sub-command.
			mainCommand.execute(sender, args);
			return;
		}
		else
		{
			//If arguments are given, cycle through the right one.
			//If none found, it'll give an error defined.
			for (SubCommand subCommand : subCommands)
			{
				if (subCommand.equals(args[0]))
				{
					subCommand.execute(sender, args);
					return;
				}
			}
		}
		
		onInvalidArgs(sender);
	}
	
	/*
	 * Define what to run when player has invalid arguments.
	 */
	protected abstract void onInvalidArgs(CommandSender sender);
	
	/*
	 * Define what to run when player doesn't have permission.
	 */
	protected abstract void onCommandFail(CommandSender sender);
	
	/*
	 * SubCommand
	 */
	protected static abstract class SubCommand
	{
		private ProxyCommand command;
		protected String name;
		protected String permission;
		
		public SubCommand(ProxyCommand command, String name)
		{
			this.command = command;
			this.name = name;
			this.permission = name.isEmpty() ? command.permission : command.permission + "." + name;
		}
		
		public boolean isConsole(CommandSender sender)
		{
			return !(sender instanceof ProxiedPlayer);
		}
		
		public boolean equals(String commandName)
		{
			return name.equalsIgnoreCase(commandName) || name.equals("");
		}
		
		public void execute(CommandSender sender, String[] args)
		{
			if (sender.hasPermission(this.permission))
				onCommandRun(sender, args);
			else
				command.onCommandFail(sender);
		}
		
		/*
		 * Define what to run when player has permission.
		 */
		protected abstract void onCommandRun(CommandSender sender, String[] args);
	}
}
