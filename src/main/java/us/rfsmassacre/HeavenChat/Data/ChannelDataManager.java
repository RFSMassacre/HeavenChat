package us.rfsmassacre.HeavenChat.Data;

import java.io.IOException;

import net.md_5.bungee.config.Configuration;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Channels.Channel.ChannelType;
import us.rfsmassacre.HeavenLib.BungeeCord.BaseManagers.DataManager;

public class ChannelDataManager extends DataManager
{
	public ChannelDataManager() 
	{
		super(ChatPlugin.getInstance(), "channels");
	}

	@Override
	protected void storeData(Object object, Configuration data) throws IOException 
	{
		if (!(object instanceof Channel))
			throw new IOException();
		
		Channel channel = (Channel)object;
		
		data.set("name", channel.getName());
		data.set("display-name", channel.getDisplayName());
		data.set("shortcut", channel.getShortcut());
		data.set("type", channel.getType().toString());
		data.set("format", channel.getFormat());
		data.set("forwarded", channel.isForwarded());
		data.set("member-ids", channel.getMemberStringIds());
	}

	@Override
	protected Channel loadData(Configuration data) throws IOException 
	{
		Channel channel = new Channel();
		
		channel.setName(data.getString("name"));
		channel.setDisplayName(data.getString("display-name"));
		channel.setShortcut(data.getString("shortcut"));
		channel.setType(ChannelType.fromString(data.getString("type")));
		channel.setFormat(data.getString("format"));
		channel.setForwarded(data.getBoolean("forwarded"));
		channel.setMemberStringIds(data.getStringList("member-ids"));
		
		return channel;
	}
}
