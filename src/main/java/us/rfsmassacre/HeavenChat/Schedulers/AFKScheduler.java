package us.rfsmassacre.HeavenChat.Schedulers;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public class AFKScheduler extends Scheduler
{
	public AFKScheduler() 
	{
		//1 second interval, 1 second delay
		super(1, 1);
	}

	@Override
	public void runTask() 
	{
		LocaleManager locale = ChatPlugin.getLocaleManager();
		
		int timeOut = ChatPlugin.getConfigManager().getInt("afk.timeout") * 60000;
		for (Member member : ChatPlugin.getMemberManager().getOnlineMembers())
		{
			if (!member.isAFK() && (System.currentTimeMillis() - member.getLastMovement() >= timeOut))
			{
				member.setAFK(true);
				locale.broadcastLocale(false, "afk.away", "{player}", member.getDisplayName());
			}
		}
	}
}
