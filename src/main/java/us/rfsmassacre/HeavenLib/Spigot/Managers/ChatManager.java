package us.rfsmassacre.HeavenLib.Spigot.Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import us.rfsmassacre.HeavenLib.Spigot.BaseManagers.Manager;

import org.bukkit.ChatColor;

public class ChatManager extends Manager
{
	public ChatManager(JavaPlugin instance)
	{
		super(instance);
	}
	
	public static String format(String string)
	{
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	public static String stripColors(String string)
	{
		return ChatColor.stripColor(format(string));
	}
	
	//Load Text File for Menus
	/*
	 * This reads each line of a plain txt file found in the
	 * resource folder within the JAR and returns it as one 
	 * single String. It'll stop at the key END.
	 */
	public String loadTextFile(String fileName)
	{
		try
		{
			InputStream is = instance.getResource(fileName);
			BufferedReader bfReader = new BufferedReader(new InputStreamReader(is));
			
			ArrayList<String> lines = new ArrayList<String>();
			String line;
			
			while (!(line = bfReader.readLine()).equals("END"))
			{
				lines.add(line);
			}
			
			return String.join("\n", lines);
		}
		catch (IOException error)
		{
			//Do nothing
			error.printStackTrace();
			return null;
		}
	}
}
