package us.rfsmassacre.HeavenChat.Events;

import net.md_5.bungee.api.plugin.Event;

import us.rfsmassacre.HeavenChat.Members.Member;

public class MemberLeaveEvent extends Event
{
	private Member member;

	public MemberLeaveEvent(Member member)
	{
		setMember(member);
	}
	public MemberLeaveEvent(Member member, boolean firstLogin)
	{
		setMember(member);
	}
	
	private void setMember(Member member)
	{
		this.member = member;
	}
	public Member getMember() 
	{
		return member;
	}
}
