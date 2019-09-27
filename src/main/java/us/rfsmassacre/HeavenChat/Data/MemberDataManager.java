package us.rfsmassacre.HeavenChat.Data;

import java.io.IOException;
import java.util.UUID;

import net.md_5.bungee.config.Configuration;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.ChannelManager;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.BaseManagers.DataManager;

public class MemberDataManager extends DataManager
{
	public MemberDataManager() 
	{
		super(ChatPlugin.getInstance(), "players");
	}

	@Override
	protected void storeData(Object object, Configuration data) throws IOException 
	{
		if (!(object instanceof Member))
			throw new IOException();
		
		Member member = (Member)object;
		
		data.set("uuid", member.getPlayerId().toString());
		data.set("prefix", member.getPrefix());
		data.set("username", member.getUsername());
		data.set("suffix", member.getSuffix());
		data.set("nickname", member.getNickname());
		data.set("filtered", member.isFiltered());
		data.set("ignored-uuids", member.getIgnoredStringIds());
		data.set("focused-channel", member.getFocusedChannel() != null ? member.getFocusedChannel().getName() : null);
		data.set("spying", member.isSpying());
	}

	@Override
	protected Member loadData(Configuration data) throws IOException 
	{
		ChannelManager channels = ChatPlugin.getChannelManager();
		Member member = new Member();
		
		member.setPlayerId(UUID.fromString(data.getString("uuid")));
		member.setPrefix(data.getString("prefix"));
		member.setSuffix(data.getString("suffix"));
		member.setUsername(data.getString("username"));
		member.setNickname(data.getString("nickname"));
		member.setFiltered(data.getBoolean("filtered"));
		member.setIgnoredStringIds(data.getStringList("ignored-uuids"));
		member.setFocusedChannel(channels.getChannel(data.getString("focused-channel")));
		member.setSpying(data.getBoolean("spying"));
		
		return member;
	}
}
