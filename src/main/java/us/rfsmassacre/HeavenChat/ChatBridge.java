package us.rfsmassacre.HeavenChat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;

import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Listeners.ServerAFKListener;
import us.rfsmassacre.HeavenChat.Listeners.ServerChatListener;
import us.rfsmassacre.HeavenChat.Listeners.ServerConfigListener;
import us.rfsmassacre.HeavenChat.Listeners.ServerLoginListener;
import us.rfsmassacre.HeavenChat.Listeners.ServerPingListener;
import us.rfsmassacre.HeavenChat.Managers.OptionManager;
import us.rfsmassacre.HeavenChat.Tasks.SendPrefixTask;
import us.rfsmassacre.HeavenChat.Tasks.SendProximityTask;
import us.rfsmassacre.HeavenChat.Tasks.SendSuffixTask;

public class ChatBridge extends JavaPlugin
{
	private static ChatBridge instance;
	private static OptionManager options;
	
	private static Chat chat;
	
	@Override
	public void onEnable()
	{
		instance = this;
		options = new OptionManager();
		
		//Register plugin message channels
		getServer().getMessenger().registerIncomingPluginChannel(this, PluginChannel.PING, new ServerPingListener());
		getServer().getMessenger().registerIncomingPluginChannel(this, PluginChannel.CONFIG, new ServerConfigListener());
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, PluginChannel.LOGIN);
		getServer().getMessenger().registerOutgoingPluginChannel(this, PluginChannel.AFK);
		getServer().getMessenger().registerOutgoingPluginChannel(this, PluginChannel.PREFIX);
		getServer().getMessenger().registerOutgoingPluginChannel(this, PluginChannel.SUFFIX);
		getServer().getMessenger().registerOutgoingPluginChannel(this, PluginChannel.CHAT);
		getServer().getMessenger().registerOutgoingPluginChannel(this, PluginChannel.PROXIMITY);
		
		//Register listeners
		getServer().getPluginManager().registerEvents(new ServerLoginListener(), this);
		getServer().getPluginManager().registerEvents(new ServerChatListener(), this);
		getServer().getPluginManager().registerEvents(new ServerAFKListener(), this);
		
		//Set up Vault dependency
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		if (rsp != null)
			chat = rsp.getProvider();
		
		//Set up schedulers
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() 
		{
            @Override
            public void run() 
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                	new SendPrefixTask(player).runTask(ChatBridge.getInstance());
                	new SendSuffixTask(player).runTask(ChatBridge.getInstance());
                	new SendProximityTask(player).runTask(ChatBridge.getInstance());
                }
            }
	    }, 0L, 5L);
	}
	
	@Override
	public void onDisable()
	{
		//Unregister plugin message channels
		getServer().getMessenger().unregisterOutgoingPluginChannel(this);
	}
	
	public static ChatBridge getInstance()
	{
		return instance;
	}
	
	public static OptionManager getOptionManager()
	{
		return options;
	}
	
	public static Chat getVaultChat()
	{
		return chat;
	}
}
