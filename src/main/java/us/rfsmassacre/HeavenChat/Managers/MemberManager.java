package us.rfsmassacre.HeavenChat.Managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.Data.MemberDataManager;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.ChatManager;

public class MemberManager 
{
	private HashMap<ProxiedPlayer, Member> members;
	private MemberDataManager memberData;
	
	public MemberManager()
	{
		members = new HashMap<ProxiedPlayer, Member>();
		memberData = new MemberDataManager();
	}
	
	public Member getMember(ProxiedPlayer player)
	{
		return members.get(player);
	}
	public Member getMember(UUID playerId)
	{
		return members.get(ProxyServer.getInstance().getPlayer(playerId));
	}
	public Member getOfflineMember(UUID playerId)
	{
		return (Member)memberData.loadFromFile(playerId.toString());
	}
	
	public ArrayList<Member> getOnlineMembers()
	{
		return new ArrayList<Member>(members.values());
	}
	public ArrayList<Member> getOfflineMembers()
	{
		ArrayList<Member> offlineMembers = new ArrayList<Member>();
		
		for (File file : memberData.listFiles())
		{
			//If member is not online, add them to the list
			Member member = (Member)memberData.loadFromFile(file);
			if (ProxyServer.getInstance().getPlayer(member.getPlayerId()) == null)
				offlineMembers.add(member);
		}
		
		return offlineMembers;
	}
	public void purgeBrokenMembers()
	{
		for (File file : memberData.listFiles())
		{
			//If member is not online, add them to the list
			try
			{
				memberData.loadFromFile(file);
			}
			catch (Exception exception)
			{
				//Delete this data because it's simply broken
				file.delete();
			}
		}
	}
	//Intended for finding members by username
	public Member findMember(String username)
	{
		for (Member member : getOnlineMembers())
		{
			//Strip all nonalphanumeric characters and color codes for proper comparison
			String rawMemberUser = ChatManager.stripColors(member.getUsername()).replaceAll("[^A-Za-z0-9 ]", "");
			String rawUser = ChatManager.stripColors(username).replaceAll("[^A-Za-z0-9 ]", "");
			
			if (rawMemberUser.equalsIgnoreCase(rawUser))
				return member;
		}
		
		for (Member member : getOfflineMembers())
		{
			//Strip all nonalphanumeric characters and color codes for proper comparison
			String rawMemberUser = ChatManager.stripColors(member.getUsername()).replaceAll("[^A-Za-z0-9 ]", "");
			String rawUser = ChatManager.stripColors(username).replaceAll("[^A-Za-z0-9 ]", "");
			
			if (rawMemberUser.equalsIgnoreCase(rawUser))
				return member;
		}
		
		return null;
	}
	public Member findOnlineMember(String username)
	{
		for (Member member : getOnlineMembers())
		{
			//Strip all nonalphanumeric characters and color codes for proper comparison
			String rawMemberUser = ChatManager.stripColors(member.getUsername()).replaceAll("[^A-Za-z0-9 ]", "");
			String rawUser = ChatManager.stripColors(username).replaceAll("[^A-Za-z0-9 ]", "");
			
			if (rawMemberUser.equalsIgnoreCase(rawUser))
				return member;
		}
		
		return null;
	}
	//Intended for finding members by nickname
	public Member matchNickname(String nickname)
	{
		for (Member member : getOnlineMembers())
		{
			//Strip all nonalphanumeric characters and color codes for proper comparison
			String rawMemberNick = ChatManager.stripColors(member.getNickname()).replaceAll("[^A-Za-z0-9 ]", "");
			String rawNick = ChatManager.stripColors(nickname).replaceAll("[^A-Za-z0-9 ]", "");
			
			if (!member.getNickname().equals("") && rawMemberNick.equalsIgnoreCase(rawNick))
				return member;
		}
		
		for (Member member : getOfflineMembers())
		{
			//Strip all nonalphanumeric characters and color codes for proper comparison
			String rawMemberNick = ChatManager.stripColors(member.getNickname()).replaceAll("[^A-Za-z0-9 ]", "");
			String rawNick = ChatManager.stripColors(nickname).replaceAll("[^A-Za-z0-9 ]", "");
			
			if (!member.getNickname().equals("") && rawMemberNick.equalsIgnoreCase(rawNick))
				return member;
		}
		
		return null;
	}
	
	public void addMember(Member member)
	{
		members.put(member.getPlayer(), member);
	}
	public void removeMember(Member member)
	{
		members.remove(member.getPlayer());
	}
	
	public void saveMember(Member member)
	{
		memberData.saveToFile(member, member.getPlayerId().toString());
	}
	public Member loadMember(ProxiedPlayer player)
	{
		//Load data or create new member if no data found
		Member member = getOfflineMember(player.getUniqueId());
		if (member != null)
		{
			member.setPlayer(player);
			addMember(member);
			return member;
		}
		
		return null;
	}
	
	public void saveAllMembers()
	{
		for (Member member : members.values())
		{
			saveMember(member);
		}
	}
	
	public boolean isNicknameTaken(String nickname)
	{
		String rawNickname = ChatManager.stripColors(nickname);
		for (Member member : getOnlineMembers())
		{
			String rawTargetname = ChatManager.stripColors(member.getNickname());
			if (rawNickname.equalsIgnoreCase(rawTargetname))
				return true;
		}
		for (Member member : getOfflineMembers())
		{
			String rawTargetname = ChatManager.stripColors(member.getNickname());
			if (rawNickname.equalsIgnoreCase(rawTargetname))
				return true;
		}
		
		return false;
	}
}
