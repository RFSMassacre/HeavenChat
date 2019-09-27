package us.rfsmassacre.HeavenChat.Events;

import net.md_5.bungee.api.plugin.Event;

import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Members.Member;

public class ChannelJoinEvent extends Event
{
	private Member member;
	private Channel channel;
	
	public ChannelJoinEvent(Member member, Channel channel)
	{
		setMember(member);
		setChannel(channel);
	}

	public Member getMember() 
	{
		return member;
	}
	private void setMember(Member member) 
	{
		this.member = member;
	}

	public Channel getChannel() 
	{
		return channel;
	}
	private void setChannel(Channel channel) 
	{
		this.channel = channel;
	}
}
