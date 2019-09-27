package us.rfsmassacre.HeavenChat.Managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Channels.Channel.ChannelType;
import us.rfsmassacre.HeavenChat.Data.ChannelDataManager;

public class ChannelManager 
{
	private HashMap<String, Channel> channels;
	private ChannelDataManager channelData;
	
	public ChannelManager()
	{
		channels = new HashMap<String, Channel>();
		channelData = new ChannelDataManager();
		
		loadAllChannels();
	}
	
	public Channel getChannel(String name)
	{
		return channels.get(name.toLowerCase());
	}
	
	public ArrayList<Channel> getAllChannels()
	{
		return new ArrayList<Channel>(channels.values());
	}
	
	public void addChannel(Channel channel)
	{
		channels.put(channel.getName().toLowerCase(), channel);
		channel.registerCommand();
	}
	public void removeChannel(Channel channel)
	{
		channels.remove(channel.getName().toLowerCase());
		channel.unregisterCommand();
	}
	
	public void saveChannel(Channel channel)
	{
		channelData.saveToFile(channel, channel.getName().toLowerCase());
	}
	
	public void clearAllChannels()
	{
		for (Channel channel : channels.values())
		{
			channel.unregisterCommand();
		}
		
		channels.clear();
	}
	public void loadAllChannels()
	{
		if (channelData.listFiles().length > 0)
		{
			for (File file : channelData.listFiles())
			{
				Channel channel = (Channel)channelData.loadFromFile(file);
				if (Channel.isValid(channel))
					addChannel(channel);
			}
		}
		else
		{
			//DEFAULT CHANNELS
			Channel global = new Channel();
			global.setName("global");
			global.setDisplayName("&eGlobal Channel");
			global.setType(ChannelType.GLOBAL);
			global.setForwarded(true);
			global.setFormat("&6[&eGlobal&6] &f{sender}&r &6&l> &r{message}");
			global.setShortcut("g");
			
			Channel staff = new Channel();
			staff.setName("staff");
			staff.setDisplayName("&bStaff Channel");
			staff.setType(ChannelType.GLOBAL);
			staff.setForwarded(false);
			staff.setFormat("&3[&bStaff&3] &f{sender}&r &3&l> &b{message}");
			staff.setShortcut("st");
			
			Channel server = new Channel();
			server.setName("server");
			server.setDisplayName("&aServer Channel");
			server.setType(ChannelType.SERVER);
			server.setForwarded(true);
			server.setFormat("{server}&f{sender}&r &4&l> &r{message}");
			server.setShortcut("s");
			
			Channel local = new Channel();
			local.setName("local");
			local.setDisplayName("&7Local Channel");
			local.setType(ChannelType.LOCAL);
			local.setForwarded(true);
			local.setFormat("&8[&6Local&8] &f{sender}&r &8&l> &7{message}");
			local.setShortcut("l");
			
			addChannel(global);
			addChannel(staff);
			addChannel(server);
			addChannel(local);
			
			channelData.saveToFile(global, global.getName());
			channelData.saveToFile(staff, staff.getName());
			channelData.saveToFile(server, server.getName());
			channelData.saveToFile(local, local.getName());
		}
	}
	public void saveAllChannels()
	{
		for (Channel channel : channels.values())
		{
			saveChannel(channel);
		}
	}
}
