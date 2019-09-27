package us.rfsmassacre.HeavenLib.BungeeCord.Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import us.rfsmassacre.HeavenLib.BungeeCord.BaseManagers.Manager;

public class ChatManager extends Manager
{
	public ChatManager(Plugin instance)
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
	public static String stripNonAlphaNumeric(String string)
	{
		return string.replaceAll("[^A-Za-z0-9]", "");
	}
	
	public static TextComponent toTextComponent(String string)
	{
		return new TextComponent(TextComponent.fromLegacyText(format(string)));
	}
	
	public static TextComponent toTextButton(String button, String hover)
	{
		TextComponent text = new TextComponent(TextComponent.fromLegacyText(button));
		HoverEvent hoverEvent = new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText(format(hover)));
		text.setHoverEvent(hoverEvent);
		
		if (hover.equals(""))
			text.setHoverEvent(null);
		
		return text;
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
			InputStream is = instance.getResourceAsStream(fileName);
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
