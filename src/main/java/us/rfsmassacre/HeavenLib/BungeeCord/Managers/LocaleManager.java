package us.rfsmassacre.HeavenLib.BungeeCord.Managers;

import java.util.List;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import us.rfsmassacre.HeavenLib.BungeeCord.BaseManagers.ResourceManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ChatManager;

public class LocaleManager extends ResourceManager
{
	/*
	 * Manually saving the config files this way makes it
	 * consistent and simple to load from the default config
	 * saved in the jar in the event the user deleted the value
	 * from the new config file.
	 */
	public LocaleManager(Plugin instance)
	{
		super(instance, "locale.yml");
	}
	
	//LOCALE.YML
	//Gets file or default file when needed
	public String getMessage(String key)
	{
		return ChatManager.format(file.getString(key, defaultFile.getString(key)));
	}
	public List<String> getMessageList(String key)
	{
		List<String> stringList = file.getStringList(key);
		if (stringList == null)
			stringList = defaultFile.getStringList(key);
		
		return stringList;
	}
	
	/*
	 * Replace the placer holder with its variable.
	 */
	private String replaceHolders(String locale, String[] replacers)
	{
		String message = locale;
		
		for (int slot = 0; slot < replacers.length; slot += 2)
		{
			message = message.replace(replacers[slot], replacers[slot + 1]);
		}
		
		return message;
	}
	
	public void sendMessage(CommandSender sender, String message, String...replacers)
	{
		sender.sendMessage(ChatManager.toTextComponent(ChatManager.format(replaceHolders(message, replacers))));
	}
	public void sendLocale(CommandSender sender, String key, String...replacers)
	{
		sendMessage(sender, getMessage("prefix") + getMessage(key), replacers);
	}
	 
	public void broadcastMessage(String message, String...replacers)
	{
		ProxyServer.getInstance().broadcast(ChatManager.toTextComponent(replaceHolders(message, replacers)));
	}
	public void broadcastLocale(boolean prefix, String key, String...replacers)
	{
		ProxyServer.getInstance().broadcast(ChatManager.toTextComponent(replaceHolders(prefix ? getMessage("prefix") + getMessage(key) : "" + getMessage(key), replacers)));
	}
	public void broadcastLocale(String key, String...replacers)
	{
		broadcastLocale(true, key, replacers);
	}
}