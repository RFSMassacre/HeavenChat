package us.rfsmassacre.HeavenLib.Spigot.Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import us.rfsmassacre.HeavenLib.Spigot.BaseManagers.Manager;

public class ChatManager extends Manager
{
	public ChatManager(JavaPlugin instance)
	{
		super(instance);
	}

	/**
	 * Format string with colors, bolds, italics, underlines, or magic characters.
	 * @param string String to format.
	 * @return Formatted string.
	 */
	public static String format(String string)
	{
		return format(string, true, true, true, true, true, true, true);
	}

	public static String undoFormat(String string)
	{
		return string.replace("§", "&");
	}

	/**
	 * Format string with colors, bolds, italics, underlines, or magic characters if enabled.
	 * @param string String to format.
	 * @param color Color.
	 * @param bold Bold.
	 * @param italic Italic.
	 * @param underline Underline.
	 * @param strikethrough Strikethrough.
	 * @param magic Magic.
	 * @return Formatted string with only enabled parts.
	 */
	public static String format(String string, boolean color, boolean bold, boolean italic, boolean underline,
								boolean strikethrough, boolean magic, boolean hex)
	{
		if (!color)
		{
			string = string.replaceAll("\\§[1-9]", "");
			string = string.replaceAll("\\§[a-f]", "");
			string = string.replaceAll("\\&[1-9]", "");
			string = string.replaceAll("\\&[a-f]", "");
			string = string.replaceAll("\\§[A-F]", "");
			string = string.replaceAll("\\&[A-F]", "");
			string = string.replaceAll("\\§(r|R)", "");
			string = string.replaceAll("\\&(r|R)", "");
		}
		if (!bold)
		{
			string = string.replaceAll("\\§(l|L)", "");
			string = string.replaceAll("\\&(l|L)", "");
		}
		if (!italic)
		{
			string = string.replaceAll("\\§(o|O)", "");
			string = string.replaceAll("\\&(o|O)", "");
		}
		if (!underline)
		{
			string = string.replaceAll("\\§(n|N)", "");
			string = string.replaceAll("\\&(n|N)", "");
		}
		if (!strikethrough)
		{
			string = string.replaceAll("\\§(m|M)", "");
			string = string.replaceAll("\\&(m|M)", "");
		}
		if (!magic)
		{
			string = string.replaceAll("\\§(k|K)", "");
			string = string.replaceAll("\\&(k|K)", "");
		}

		if (!hex)
		{
			string = string.replaceAll("\\§(#)", "");
			string = string.replaceAll("\\&(#)", "");
		}
		else
		{
			Pattern HEX_PATTERN = Pattern.compile("(\\§|\\&)(#[A-Fa-f0-9]{6})");
			Matcher matcher = HEX_PATTERN.matcher(string);
			while (matcher.find())
			{
				string = string.replace(matcher.group(), "" + net.md_5.bungee.api.ChatColor.of(matcher.group()
						.replaceAll("(\\§|\\&)", "")));
			}
		}

		return ChatColor.translateAlternateColorCodes('&', string);
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
