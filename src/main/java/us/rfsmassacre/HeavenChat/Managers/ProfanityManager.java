package us.rfsmassacre.HeavenChat.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.lang.StringUtils;

import us.rfsmassacre.HeavenChat.ChatPlugin;

import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ConfigManager;

public class ProfanityManager 
{
	private class Swear
	{
		public String swear;
		public int firstIndex;
		public int lastIndex;
		
		public Swear(String swear, int firstIndex, int lastIndex)
		{
			this.swear = swear;
			this.firstIndex = firstIndex;
			this.lastIndex = lastIndex;
		}
	}
	
	private HashMap<String, String> leetSpeak;
	private List<String> swears;
	private DoubleMetaphone phone;
	private ConfigManager config;
	
	public ProfanityManager()
	{
		config = ChatPlugin.getConfigManager();
		leetSpeak = new HashMap<String, String>();
		swears = config.getStringList("blocked-words");
		phone = new DoubleMetaphone();
		
		//Leet Speak definitions
		leetSpeak.put("1", "i");
		leetSpeak.put("!", "i");
		leetSpeak.put("3", "e");
		leetSpeak.put("4", "a");
		leetSpeak.put("@", "a");
		leetSpeak.put("5", "s");
		leetSpeak.put("7", "t");
		leetSpeak.put("0", "o");
		leetSpeak.put("9", "g");
	}
	
	public String fromLeetSpeak(String message)
	{
		String filteredMessage = message;
		for (String key : leetSpeak.keySet())
		{
			filteredMessage = filteredMessage.replaceAll(key, leetSpeak.get(key));
		}
		
		return filteredMessage;
	}
	public String removeNonAlphabet(String message)
	{
		return message.replaceAll("[^A-Za-z ]", "");
	}
	public String replaceSwear(String message, int firstIndex, int lastIndex)
	{
		String stringCensor = config.getString("filters.profanity-filter.censor");
		char censor = (!stringCensor.equals("") ? stringCensor.charAt(0) : '*');
		
		char[] chars = message.toCharArray();
		for (int index = firstIndex; index < lastIndex + 1; index++)
		{
			if (chars[index] != ' ')
			chars[index] = censor;
		}
		
		return new String(chars);
	}
	
	public boolean containsSwear(String message)
	{
		String rawMessage = removeNonAlphabet(fromLeetSpeak(message)).toLowerCase();

		for (Swear coord : extractIndexes(rawMessage))
		{
			String possibleSwear = rawMessage.substring(coord.firstIndex, coord.lastIndex + 1).replaceAll(" ", "");
			double percent = getPercentage(possibleSwear, coord.swear);
			
			if (phone.isDoubleMetaphoneEqual(possibleSwear, coord.swear) && percent > config.getInt("filters.profanity-filter.block-percent"))
				return true;
		}
		
		return false;
	}
	public String censorSwears(String message)
	{
		String rawMessage = fromLeetSpeak(message).toLowerCase();
		String finalMessage = message;
		
		for (Swear coord : extractIndexes(rawMessage))
		{
			String possibleSwear = rawMessage.substring(coord.firstIndex, coord.lastIndex + 1);
			double percent = getPercentage(possibleSwear, coord.swear);
			
			if (phone.isDoubleMetaphoneEqual(removeNonAlphabet(possibleSwear).replace(" ", ""), coord.swear) && percent > config.getInt("filters.profanity-filter.block-percent"))
			{
				finalMessage = replaceSwear(finalMessage, coord.firstIndex, coord.lastIndex);
			}
		}
		
		return finalMessage;
	}
	private ArrayList<Swear> extractIndexes(String message)
	{
		String rawMessage = fromLeetSpeak(message);
		ArrayList<Swear> coords = new ArrayList<Swear>();
		for (String swear : swears)
		{
			char firstChar = swear.charAt(0);
			char lastChar = swear.charAt(swear.length() - 1);
			
			int firstIndex = -1;
			int lastIndex = -1;
			
			for (int index = 0; index < rawMessage.length(); index++)
			{
				if (firstChar == rawMessage.charAt(index))
				{
					firstIndex = index;
				}
				
				if (lastChar == rawMessage.charAt(index) && index > firstIndex)
				{
					lastIndex = index;
					
					if (firstIndex != -1 && lastIndex != -1)
						coords.add(new Swear(swear, firstIndex, lastIndex));
				}
			}
				
		}
		
		return coords;
	}
	private double getPercentage(String message, String swear)
	{
		double difference = StringUtils.getLevenshteinDistance(message, swear);
		double limit = (swear.length() > message.length() ? swear.length() : message.length());
		double percent = (1 - (difference / limit)) * 100;
		
		return percent;
	}
}
