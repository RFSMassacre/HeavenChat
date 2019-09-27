package us.rfsmassacre.HeavenLib.BungeeCord.Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.md_5.bungee.api.plugin.Plugin;

import us.rfsmassacre.HeavenLib.BungeeCord.BaseManagers.Manager;

public class MenuManager extends Manager 
{
	protected String fileName;
	protected ArrayList<String> lines;
	
	public MenuManager(Plugin instance, String fileName)
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
			InputStream is = instance.getResourceAsStream(fileName);
			BufferedReader bfReader = new BufferedReader(new InputStreamReader(is));

			ArrayList<String> lines = new ArrayList<String>();
			String line;
			
			while ((line = bfReader.readLine()) != null)
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
