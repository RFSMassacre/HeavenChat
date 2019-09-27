package us.rfsmassacre.HeavenLib.BungeeCord.Managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.md_5.bungee.api.plugin.Plugin;
import us.rfsmassacre.HeavenLib.BungeeCord.BaseManagers.Manager;

public class TextManager extends Manager
{
	private File folder;
	
	public TextManager(Plugin instance) 
	{
		super(instance);
		this.folder = instance.getDataFolder();
	}
	public TextManager(Plugin instance, String folderName) 
	{
		super(instance);
		this.folder = new File(instance.getDataFolder() + "/" + folderName);
		
		//Create folder if not found
		if (!folder.exists())
			folder.mkdir();
	}
	
	private File getFile(String fileName)
	{
		return new File(folder, (fileName.endsWith(".txt") ? fileName : fileName + ".txt"));
	}
	public boolean fileExists(String fileName)
	{
		return getFile(fileName).exists();
	}
	private boolean createFile(String fileName)
	{
		File file = getFile(fileName);
		try 
		{
			return file.createNewFile();
		} 
		catch (IOException exception) 
		{
			exception.printStackTrace();
		}
		
		return false;
	}
	
	public ArrayList<String> loadFile(String fileName)
	{
		try
		{
			BufferedReader bfReader = new BufferedReader(new FileReader(getFile(fileName)));

			ArrayList<String> lines = new ArrayList<String>();
			String line;
			
			while ((line = bfReader.readLine()) != null)
			{
				lines.add(line);
			}
			
			bfReader.close();
			return lines;
		}
		catch (IOException exception)
		{
			//Print error on console neatly
			exception.printStackTrace();
		}
		
		return null;
	}
	public ArrayList<String> loadLocalFile(String fileName)
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
			
			is.close();
			bfReader.close();
			return lines;
		}
		catch (IOException exception)
		{
			//Print error on console neatly
			exception.printStackTrace();
		}
		
		return null;
	}
	
	public void writeFile(String fileName, String text)
	{
		File file = getFile(fileName);
		if (!file.exists())
			createFile(fileName);
		
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true))))
		{
			out.println(text);
			out.close();
		}
		catch (IOException error)
		{
			
		}
	}
	
	public void copyFile(String fileName)
	{
		for (String line : loadLocalFile(fileName))
		{
			writeFile(fileName, line);
		}
	}
}
