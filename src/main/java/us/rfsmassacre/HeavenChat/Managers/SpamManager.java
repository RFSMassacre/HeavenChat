package us.rfsmassacre.HeavenChat.Managers;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ConfigManager;

public class SpamManager 
{
	//Number of milliseconds in these quantities
	private static final int MINUTE = 60000;
	private static final int SECOND = 1000;
	
	/*
	 * Class to hold both the message and time in one place.
	 */
	private class Message
	{
		public Long time;
		public String message;
		
		public Message(Long time, String message)
		{
			this.time = time;
			this.message = message;
		}
	}
	
	private HashMap<Member, Message> messageTime;
	private HashMap<Member, Long> afkTime;
	private ConfigManager config;
	
	public SpamManager()
	{
		messageTime = new HashMap<Member, Message>();
		config = ChatPlugin.getConfigManager();
	}
	public void setLastMessage(Member member, String message)
	{
		messageTime.put(member, new Message(System.currentTimeMillis(), message));
	}
	private Message getLastMessage(Member member)
	{
		return messageTime.get(member);
	}
	public void setLastAFK(Member member)
	{
		afkTime.put(member, System.currentTimeMillis());
	}
	private long getLastAFK(Member member)
	{
		return afkTime.get(member);
	}
	
	/*
	 * Hard Filters - Meant to cancel the message completely
	 */
	public String getFloodSpamTime(Member member)
	{
		if (config.getBoolean("filters.flood-filter.enabled"))
		{
			int cooldown = config.getInt("filters.flood-filter.message-cooldown") * SECOND;
			float time = (System.currentTimeMillis() - getLastMessage(member).time);
			float timeRemaining = cooldown - time;
			
			return String.format("%.1f", (timeRemaining / SECOND));
		}
		
		return "0";
	}
	public boolean isFloodSpam(Member member)
	{
		if (config.getBoolean("filters.flood-filter.enabled") && getLastMessage(member) != null 
	    && !member.getPlayer().hasPermission("heavenchat.spam.flood"))
		{
			int cooldown = config.getInt("filters.flood-filter.message-cooldown") * SECOND;
			long time = System.currentTimeMillis() - getLastMessage(member).time;
			if (time <= cooldown)
				return true;
		}
		
		return false;
	}
	public String getAFKSpamTime(Member member)
	{
		if (config.getBoolean("filters.flood-filter.enabled"))
		{
			int cooldown = config.getInt("filters.flood-filter.afk-cooldown") * MINUTE;
			long time = (System.currentTimeMillis() - getLastAFK(member));
			long timeRemaining = cooldown - time;
			
			return String.format("%.1f", (timeRemaining / MINUTE));
		}
		
		return "0";
	}
	public boolean isAFKSpam(Member member)
	{
		if (config.getBoolean("filters.flood-filter.enabled") &&
		   !member.getPlayer().hasPermission("heavenchat.spam.afk"))
		{
			int cooldown = config.getInt("filters.flood-filter.afk-cooldown") * MINUTE;
			long time = System.currentTimeMillis() - getLastAFK(member);
			if (time <= cooldown)
				return true;
		}
		
		return false;
	}
	private int getHammingDistance(String message, String target)
	{
		String first = message;
		String second = target;
		int hamming = 0;

		for (int index = 0; index < first.length() && index < second.length(); index++)
		{
			if (first.charAt(index) != second.charAt(index))
				hamming++;
		}
		
		return hamming + (first.length() > second.length() ? first.length() - second.length() : second.length() - first.length());
	}
	public boolean isRepititionSpam(Member member, String message)
	{
		if (config.getBoolean("filters.repitition-filter.enabled") && getLastMessage(member) != null 
	    && !member.getPlayer().hasPermission("heavenchat.spam.repitition"))
		{
			String lastMessage = getLastMessage(member).message.replaceAll(" ", "").toLowerCase();
			String thisMessage = message.replaceAll(" ", "").toLowerCase();
			
			double leveshtein = StringUtils.getLevenshteinDistance(lastMessage, thisMessage);
			double hamming = getHammingDistance(lastMessage, thisMessage);
			double limit = (lastMessage.length() > thisMessage.length() ? lastMessage.length() : thisMessage.length());
			double percent = (1 - ((hamming + leveshtein) / 2) / limit) * 100;
			
			return percent >= config.getInt("filters.repitition-filter.block-percent");
		}
		
		return false;
	}
	
	/*
	 * Soft Filters - Meant to filter out the unwanted but continue
	 * the message.
	 */
	public String filterSpam(Member member, String message)
	{
		return filterCapSpam(member, filterLengthSpam(member, filterCharacterSpam(member, message)));
	}
	private String filterCharacterSpam(Member member, String message)
	{
		if (config.getBoolean("filters.character-filter.enabled") &&
	       !member.getPlayer().hasPermission("heavenchat.spam.character"))
		{
			return message.replaceAll("(.+?)\\1+", "$1$1");
		}
		
		return message;
	}
	private String filterLengthSpam(Member member, String message)
	{
		if (config.getBoolean("filters.word-length-filter.enabled") &&
		   !member.getPlayer().hasPermission("heavenchat.spam.length"))
		{
			int limit = config.getInt("filters.word-length-filter.limit");
			
			String[] parts = message.split(" ");
			for (int part = 0; part < parts.length; part++)
			{
				if (parts[part].length() > limit)
					parts[part] = parts[part].substring(0, limit);
					
			}
			
			return String.join(" ", parts);
		}
		
		return message;
	}
	private String filterCapSpam(Member member, String message)
	{
		if (config.getBoolean("filters.caps-filter.enabled") &&
		   !member.getPlayer().hasPermission("heavenchat.spam.caps"))
		{
			int limit = config.getInt("filters.caps-filter.limit");
			
			int caps = 0;
			for (char character : message.toCharArray())
			{
				if (Character.isUpperCase(character))
					caps++;
			}
			
			if (caps > limit)
			{
				String firstChar = message.substring(0, 1).toUpperCase();
				String lastChars = message.substring(1).toLowerCase();
				
				return firstChar + lastChars;
			}
		}
		
		return message;
	}
}
