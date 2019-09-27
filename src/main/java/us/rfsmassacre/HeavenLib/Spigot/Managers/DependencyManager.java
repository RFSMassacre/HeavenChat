package us.rfsmassacre.HeavenLib.Spigot.Managers;

import org.bukkit.plugin.java.JavaPlugin;

import us.rfsmassacre.HeavenLib.Spigot.BaseManagers.Manager;

public class DependencyManager extends Manager
{
	public DependencyManager(JavaPlugin instance)
	{
		super(instance);
	}
	
	public boolean hasPlugin(String pluginName)
	{
		//Easy way to check if needed plugin is enabled
		return instance.getServer().getPluginManager().isPluginEnabled(pluginName);
	}
}
