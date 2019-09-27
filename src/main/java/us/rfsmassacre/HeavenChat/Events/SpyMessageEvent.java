package us.rfsmassacre.HeavenChat.Events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import us.rfsmassacre.HeavenChat.Members.Member;

public class SpyMessageEvent extends Event implements Cancellable
{
	private boolean cancelled;
	private Member spy;
	private Member sender;
	private Member target;
	private String message;
	
	public SpyMessageEvent(Member spy, Member sender, Member target, String message)
	{
		setCancelled(false);
		setSpy(spy);
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
	
	public Member getSpy() 
	{
		return spy;
	}
	private void setSpy(Member spy) 
	{
		this.spy = spy;
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
