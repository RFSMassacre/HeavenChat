package us.rfsmassacre.HeavenChat.Events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Members.Member;

public class ChannelMessageReceiveEvent extends Event implements Cancellable
{
	private boolean cancelled;
	private Channel channel;
	private Member sender;
	private Member target;
	private String message;
	
	public ChannelMessageReceiveEvent(Channel channel, Member sender, Member target, String message)
	{
		setCancelled(false);
		setChannel(channel);
		setSender(sender);
		setTarget(target);
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
	
	public Member getTarget()
	{
		return target;
	}
	private void setTarget(Member target)
	{
		this.target = target;
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
