package us.rfsmassacre.HeavenChat.Managers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ChatManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.TextManager;

public class LogsManager 
{
	private TextManager text;
	
	public LogsManager()
	{
		text = new TextManager(ChatPlugin.getInstance(), "logs");
	}
	
	/*
	 * Replaces 
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
	
	public void logMessage(String message, String...replacers)
	{
		if (ChatPlugin.getConfigManager().getBoolean("enable-chat-logging"))
		{	
			//Log message to file named today's date
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			
			String fileName = dateFormat.format(date) + ".txt";
			String fileMessage = "[" + timeFormat.format(date) + "] " + ChatManager.stripColors(replaceHolders(message, replacers));
			
			text.writeFile(fileName, fileMessage);
		}
	}
}
