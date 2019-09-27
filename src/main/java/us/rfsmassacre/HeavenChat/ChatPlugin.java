package us.rfsmassacre.HeavenChat;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Commands.AFKCommand;
import us.rfsmassacre.HeavenChat.Commands.MainCommand;
import us.rfsmassacre.HeavenChat.Commands.AlertCommand;
import us.rfsmassacre.HeavenChat.Commands.ChannelCommand;
import us.rfsmassacre.HeavenChat.Commands.FilterCommand;
import us.rfsmassacre.HeavenChat.Commands.IgnoreCommand;
import us.rfsmassacre.HeavenChat.Commands.ListCommand;
import us.rfsmassacre.HeavenChat.Commands.NickCommand;
import us.rfsmassacre.HeavenChat.Commands.PMCommand;
import us.rfsmassacre.HeavenChat.Commands.RealNameCommand;
import us.rfsmassacre.HeavenChat.Commands.ReplyCommand;
import us.rfsmassacre.HeavenChat.Commands.SpyCommand;
import us.rfsmassacre.HeavenChat.Commands.UnignoreCommand;
import us.rfsmassacre.HeavenChat.Listeners.ProxyProximityListener;
import us.rfsmassacre.HeavenChat.Listeners.ProxyAFKListener;
import us.rfsmassacre.HeavenChat.Listeners.ProxyChannelListener;
import us.rfsmassacre.HeavenChat.Listeners.ProxyChatListener;
import us.rfsmassacre.HeavenChat.Listeners.ProxyLoginListener;
import us.rfsmassacre.HeavenChat.Listeners.ProxyNameListener;
import us.rfsmassacre.HeavenChat.Managers.ChannelManager;
import us.rfsmassacre.HeavenChat.Managers.MenuManager;
import us.rfsmassacre.HeavenChat.Managers.LogsManager;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Managers.ProfanityManager;
import us.rfsmassacre.HeavenChat.Managers.SpamManager;
import us.rfsmassacre.HeavenChat.Schedulers.AFKScheduler;
import us.rfsmassacre.HeavenChat.Schedulers.ChannelScheduler;
import us.rfsmassacre.HeavenChat.Tasks.SendConfigTask;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ConfigManager;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public class ChatPlugin extends Plugin
{
	private static ChatPlugin instance;
	
	private ConfigManager config;
	private LocaleManager locale;
	
	private MemberManager members;
	private ChannelManager channels;
	private ProfanityManager profanity;
	private SpamManager spam;
	private LogsManager logs;
	
	private MenuManager list;
	
	private AFKScheduler afkScheduler;
	private ChannelScheduler channelScheduler;
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		//Create data folder
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		
		config = new ConfigManager(this);
		locale = new LocaleManager(this);
		
		members = new MemberManager();
		channels = new ChannelManager();
		profanity = new ProfanityManager();
		spam = new SpamManager();
		logs = new LogsManager();
		list = new MenuManager();
		
		afkScheduler = new AFKScheduler();
		channelScheduler = new ChannelScheduler();
		
		//Register plugin message channels
		getProxy().registerChannel(PluginChannel.LOGIN);
		getProxy().registerChannel(PluginChannel.AFK);
		getProxy().registerChannel(PluginChannel.PREFIX);
		getProxy().registerChannel(PluginChannel.SUFFIX);
		getProxy().registerChannel(PluginChannel.CHAT);
		getProxy().registerChannel(PluginChannel.PROXIMITY);
		getProxy().registerChannel(PluginChannel.PING);
		getProxy().registerChannel(PluginChannel.CONFIG);
		
		//Register listeners
		getProxy().getPluginManager().registerListener(this, new ProxyLoginListener());
		getProxy().getPluginManager().registerListener(this, new ProxyChatListener());
		getProxy().getPluginManager().registerListener(this, new ProxyAFKListener());
		getProxy().getPluginManager().registerListener(this, new ProxyProximityListener());
		getProxy().getPluginManager().registerListener(this, new ProxyNameListener());
		getProxy().getPluginManager().registerListener(this, new ProxyChannelListener());
		
		//Register commands
		getProxy().getPluginManager().registerCommand(this, new MainCommand());
		getProxy().getPluginManager().registerCommand(this, new ChannelCommand());
		getProxy().getPluginManager().registerCommand(this, new PMCommand());
		getProxy().getPluginManager().registerCommand(this, new ReplyCommand());
		getProxy().getPluginManager().registerCommand(this, new FilterCommand());
		getProxy().getPluginManager().registerCommand(this, new SpyCommand());
		getProxy().getPluginManager().registerCommand(this, new AlertCommand());
		getProxy().getPluginManager().registerCommand(this, new IgnoreCommand());
		getProxy().getPluginManager().registerCommand(this, new UnignoreCommand());
		getProxy().getPluginManager().registerCommand(this, new NickCommand());
		getProxy().getPluginManager().registerCommand(this, new AFKCommand());
		getProxy().getPluginManager().registerCommand(this, new ListCommand());
		getProxy().getPluginManager().registerCommand(this, new RealNameCommand());
		
		//Forward config values to sub-servers
		getProxy().getScheduler().schedule(this, new Runnable()
		{
			@Override
			public void run() 
			{
				//Run only at the first online player
				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
				{
					new SendConfigTask(player, "PROXIMITY_RANGE", config.getInt("local-range")).run();
					new SendConfigTask(player, "PING_SOUND", config.getString("ping.sound")).run();
					new SendConfigTask(player, "PING_VOLUME", config.getDouble("ping.volume")).run();
					new SendConfigTask(player, "PING_PITCH", config.getDouble("ping.pitch")).run();
					break;
				}
			}
	
		}, 1, 1, TimeUnit.SECONDS);
		
		//Reload settings one second after start up to override channel commands.
		getProxy().getScheduler().schedule(this, new Runnable()
		{
			@Override
			public void run()
			{
				//Reloads channel settings (list of members may roll back.)
				ChatPlugin.getChannelManager().clearAllChannels();
				ChatPlugin.getChannelManager().loadAllChannels();
			}
		}, 15, TimeUnit.SECONDS);
	}
	
	@Override
	public void onDisable()
	{
		//Unregister plugin message channels
		getProxy().unregisterChannel(PluginChannel.LOGIN);
		getProxy().unregisterChannel(PluginChannel.AFK);
		getProxy().unregisterChannel(PluginChannel.PREFIX);
		getProxy().unregisterChannel(PluginChannel.CHAT);
		getProxy().unregisterChannel(PluginChannel.PROXIMITY);
		getProxy().unregisterChannel(PluginChannel.PING);
		getProxy().unregisterChannel(PluginChannel.CONFIG);
		
		//Save all channels and members
		channels.saveAllChannels();
		members.saveAllMembers();
		
		afkScheduler.unregisterTask();
		channelScheduler.unregisterTask();
	}
	
	public static ChatPlugin getInstance()
	{
		return instance;
	}
	
	/*
	 * HeavenLib Managers
	 */
	public static ConfigManager getConfigManager()
	{
		return instance.config;
	}
	public static LocaleManager getLocaleManager()
	{
		return instance.locale;
	}
	
	/*
	 * HeavenChat Managers
	 */
	public static MemberManager getMemberManager()
	{
		return instance.members;
	}
	public static ChannelManager getChannelManager()
	{
		return instance.channels;
	}
	public static ProfanityManager getProfanityManager()
	{
		return instance.profanity;
	}
	public static SpamManager getSpamManager()
	{
		return instance.spam;
	}
	public static LogsManager getLogsManager()
	{
		return instance.logs;
	}
	public static MenuManager getMenuManager()
	{
		return instance.list;
	}
}
