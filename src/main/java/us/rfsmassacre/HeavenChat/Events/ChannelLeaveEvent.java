package us.rfsmassacre.HeavenChat.Events;

import net.md_5.bungee.api.plugin.Event;

import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Members.Member;

public class ChannelLeaveEvent extends Event
{
	private Member member;
	private Channel channel;
	private boolean kicked;
	
	public ChannelLeaveEvent(Member member, Channel channel, boolean kicked)
	{
		setMember(member);
		setChannel(channel);
		setKicked(false);
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

	public boolean isKicked() 
	{
		return kicked;
	}
	public void setKicked(boolean kicked) 
	{
		this.kicked = kicked;
	}
}
