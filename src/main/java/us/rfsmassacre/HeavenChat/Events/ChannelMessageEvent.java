package us.rfsmassacre.HeavenChat.Events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Members.Member;

public class ChannelMessageEvent extends Event implements Cancellable
{
	private boolean cancelled;
	private Channel channel;
	private Member sender;
	private String message;
	
	public ChannelMessageEvent(Channel channel, Member sender, String message)
	{
		setCancelled(false);
		setChannel(channel);
		setSender(sender);
		setMessage(message);
	}

	@Override
	public boolean isCancelled() 
	{
		return cancelled;
	}
	@Override
	public void setCancelled(boolean cancelled) 
	{
		this.cancelled = cancelled;
	}

	public Channel getChannel() 
	{
		return channel;
	}
	private void setChannel(Channel channel) 
	{
		this.channel = channel;
	}

	public Member getSender() 
	{
		return sender;
	}
	private void setSender(Member sender) 
	{
		this.sender = sender;
	}

	public String getMessage() 
	{
		return message;
	}
	public void setMessage(String message) 
	{
		this.message = message;
	}
}
