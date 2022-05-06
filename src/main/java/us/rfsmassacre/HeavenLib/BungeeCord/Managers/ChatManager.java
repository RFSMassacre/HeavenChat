package us.rfsmassacre.HeavenLib.BungeeCord.Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
				string = string.replace(matcher.group(), "" + ChatColor.of(matcher.group()
						.replaceAll("(\\§|\\&)", "")));
			}
		}

		return ChatColor.translateAlternateColorCodes('&', string);
	}

	/**
	 * Strips away the format given from format(String).
	 * @param string String to strip.
	 * @return Stripped string.
	 */
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
			
			ArrayList<String> lines = new ArrayList<>();
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
