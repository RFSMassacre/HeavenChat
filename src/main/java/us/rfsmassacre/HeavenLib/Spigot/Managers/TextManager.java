package us.rfsmassacre.HeavenLib.Spigot.Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import us.rfsmassacre.HeavenLib.Spigot.BaseManagers.Manager;

public class TextManager extends Manager 
{
	protected String fileName;
	protected ArrayList<String> lines;
	
	public TextManager(JavaPlugin instance, String fileName)
	{
		super(instance);
		this.fileName = fileName;
		this.lines = loadTextFile();
	}
	
	public ArrayList<String> getTextLines()
	{
		return this.lines;
	}
	
	private ArrayList<String> loadTextFile()
	{
		try
		{
			InputStream is = instance.getResource(fileName);
			BufferedReader bfReader = new BufferedReader(new InputStreamReader(is));
			
			ArrayList<String> lines = new ArrayList<String>();
			String line;
			
			while (!(line = bfReader.readLine()).equals("END"))
			{
				lines.add(line);
			}
			
			return lines;
		}
		catch (IOException exception)
		{
			//Print error on console neatly
			exception.printStackTrace();
		}
		
		return null;
	}
}
