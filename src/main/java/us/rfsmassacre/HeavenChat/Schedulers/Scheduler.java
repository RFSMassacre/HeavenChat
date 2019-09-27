package us.rfsmassacre.HeavenChat.Schedulers;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import us.rfsmassacre.HeavenChat.ChatPlugin;

public abstract class Scheduler 
{
	private ScheduledTask task;
	private int interval;
	private int delay;
	
	public Scheduler(int interval, int delay)
	{ 
		this.interval = interval;
		this.delay = delay;
		
		registerTask();
	}
	
	public void registerTask()
	{
		if (task == null)
		{
			task = ProxyServer.getInstance().getScheduler().schedule(ChatPlugin.getInstance(), new Runnable() 
			{
				@Override
				public void run()
				{
					runTask();
				}
			}, delay, interval, TimeUnit.SECONDS);
		}
	}
	public void unregisterTask()
	{
		task.cancel();
	}
	
	public abstract void runTask();
}
