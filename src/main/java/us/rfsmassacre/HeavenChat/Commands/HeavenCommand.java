package us.rfsmassacre.HeavenChat.Commands;

import java.util.ArrayList;

import net.md_5.bungee.api.CommandSender;

import us.rfsmassacre.HeavenChat.ChatPlugin;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public abstract class HeavenCommand extends ProxyCommand
{
	protected LocaleManager locale;
	
	public HeavenCommand(String name, String... aliases) 
	{
		super(name, aliases);
		
		locale = ChatPlugin.getLocaleManager();
		permission = "heavenchat." + name;
		
		subCommands = new ArrayList<SubCommand>();
	}

	@Override
	protected void onCommandFail(CommandSender sender)
	{
		locale.sendLocale(sender, "error.no-perm");
	}
}
