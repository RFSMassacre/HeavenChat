package us.rfsmassacre.HeavenChat.Schedulers;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Managers.ChannelManager;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public class ChannelScheduler extends Scheduler
{
	public ChannelScheduler() 
	{
		super(1, 1);
	}

	@Override
	public void runTask() 
	{
		LocaleManager locale = ChatPlugin.getLocaleManager();
		MemberManager members = ChatPlugin.getMemberManager();
		ChannelManager channels = ChatPlugin.getChannelManager();
		
		for (Member member : members.getOnlineMembers())
		{
			for (Channel channel : channels.getAllChannels())
			{
				if (channel.hasMember(member) && !channel.canJoin(member))
				{
					//Remove member from channel if they no longer have permissions to join
					channel.removeMember(member, true, true);
					locale.sendLocale(member.getPlayer(), "channel.kicked", "{channel}", channel.getDisplayName());
				}
			}
		}
	}
}
