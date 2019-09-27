package us.rfsmassacre.HeavenChat.Events;

import net.md_5.bungee.api.plugin.Event;

import us.rfsmassacre.HeavenChat.Members.Member;

public class MemberJoinEvent extends Event
{
	private Member member;
	private boolean firstLogin;

	public MemberJoinEvent(Member member)
	{
		setMember(member);
		setFirstLogin(false);
	}
	public MemberJoinEvent(Member member, boolean firstLogin)
	{
		setMember(member);
		setFirstLogin(firstLogin);
	}
	
	private void setMember(Member member)
	{
		this.member = member;
	}
	public Member getMember() 
	{
		return member;
	}

	private void setFirstLogin(boolean firstLogin) 
	{
		this.firstLogin = firstLogin;
	}
	public boolean isFirstLogin() 
	{
		return firstLogin;
	}
}
