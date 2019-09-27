package us.rfsmassacre.HeavenChat.Managers;

import java.util.ArrayList;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.TextManager;

public class MenuManager 
{
	private final String[] fileNames = {"list.txt", "help1.txt", "help2.txt"};
	private final String[] formats = new String[3];
	
	private MemberManager members;
	private TextManager text;
	
	public MenuManager()
	{
		//Should handle text files in the data folder
		members = ChatPlugin.getMemberManager();
		text = new TextManager(ChatPlugin.getInstance());
		
		reloadFiles();
	}
	
	public String getListMenu()
	{
		String list = formats[0]; //First format is always of list.txt
		
		ArrayList<String> staffNames = new ArrayList<String>();
		for (Member member : members.getOnlineMembers())
		{
			if (member.getPlayer().hasPermission("heavenchat.staff"))
				staffNames.add(member.getDisplayName());
		}
		list = list.replace("{list:staff}", String.join("&f, ", staffNames));
		list = list.replace("{count:global}", Integer.toString(members.getOnlineMembers().size()));
		
		for (ServerInfo server : ProxyServer.getInstance().getServers().values())
		{
			ArrayList<String> playerNames = new ArrayList<String>();
			for (ProxiedPlayer player : server.getPlayers())
			{
				if (!player.hasPermission("heavenchat.list.hidden"))
					playerNames.add(members.getMember(player).getDisplayName());
			}
			list = list.replace("{count:" + server.getName() + "}", Integer.toString(server.getPlayers().size()));
			list = list.replace("{list:" + server.getName() + "}", String.join("&f, ", playerNames));
		}
		
		return list;
	}
	
	public String getHelpMenu(int page)
	{
		if (page <= 1)
			return formats[1];
		else
			return formats[2];
	}
	
	public void reloadFiles()
	{
		for (int index = 0; index < fileNames.length; index++)
		{
			if (!text.fileExists(fileNames[index]))
				text.copyFile(fileNames[index]);
			
			if (text.loadFile(fileNames[index]) != null && !text.loadFile(fileNames[index]).isEmpty())
			{
				formats[index] = String.join("\n", text.loadFile(fileNames[index]));
			}
			else
			{
				formats[index] = String.join("\n", text.loadLocalFile(fileNames[index]));
			}
		}
	}
}
