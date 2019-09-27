package us.rfsmassacre.HeavenChat.Managers;

import java.util.HashMap;

import org.bukkit.Sound;

public class OptionManager 
{
	private HashMap<String, String> options;
	
	public OptionManager()
	{
		options = new HashMap<String, String>();
		
		setInt("PROXIMITY_RANGE", 128);
		
		for (Sound sound : Sound.values())
		{
			if (sound.toString().contains("ITEM_PICKUP"))
			{
				setString("PING_SOUND", sound.toString());
				return;
			}
		}
		
		setDouble("PING_VOLUME", 1.0);
		setDouble("PING_PITCH", 0.4);
	}
	
	public int getInt(String key)
	{
		try
		{
			return Integer.parseInt(options.get(key));
		}
		catch (Exception exception)
		{
			return 0;
		}
	}
	public double getDouble(String key)
	{
		try
		{
			return Double.parseDouble(options.get(key));
		}
		catch (Exception exception)
		{
			return 0.0;
		}
	}
	public boolean getBoolean(String key)
	{
		try
		{
			return Boolean.parseBoolean(options.get(key));
		}
		catch (Exception exception)
		{
			return false;
		}
	}
	public float getFloat(String key)
	{
		try
		{
			return Float.parseFloat(options.get(key));
		}
		catch (Exception exception)
		{
			return 0.0F;
		}
	}
	public String getString(String key)
	{
		return options.get(key);
	}
	
	public void setInt(String key, int value)
	{
		options.put(key, Integer.toString(value));
	}
	public void setDouble(String key, double value)
	{
		options.put(key, Double.toString(value));
	}
	public void setFloat(String key, float value)
	{
		options.put(key, Float.toString(value));
	}
	public void setBoolean(String key, boolean value)
	{
		options.put(key, Boolean.toString(value));
	}
	public void setString(String key, String string)
	{
		options.put(key, string);
	}
}
