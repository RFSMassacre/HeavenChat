package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Channels.Channel;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Managers.LocaleManager;

public class FocusCommand extends Command
{
	private Channel channel;
	private String permission;
	
	public FocusCommand(Channel channel) 
	{
		super(channel.getShortcut(), null);
		
		this.channel = channel;
		this.permission = "heavenchat.channel." + channel.getName();
	}

	@Override
	public void execute(CommandSender sender, String[] args) 
	{
		LocaleManager locale = ChatPlugin.getLocaleManager();
		
		if (sender instanceof ProxiedPlayer)
		{
			MemberManager members = ChatPlugin.getMemberManager();
			
			ProxiedPlayer player = (ProxiedPlayer)sender;
			Member member = members.getMember(player);
			
			if (!player.hasPermission(permission))
			{
				locale.sendLocale(player, "channel.no-perm", "{channel}", channel.getDisplayName());
				return;
			}
			
			if (channel.hasMember(member))
			{
				//Focus to this channel
				if (args.length == 0)
				{
					if (member.getFocusedMember() != null || member.getFocusedChannel() == null || 
					   !member.getFocusedChannel().equals(channel))
					{
						member.setFocusedChannel(channel);
						member.setFocusedMember(null);
						
						locale.sendLocale(player, "channel.focused", "{channel}", channel.getDisplayName());
					}
					else
					{
						locale.sendLocale(player, "channel.already-focused", "{channel}", channel.getDisplayName());
					}
				}
				//Send message to this channel
				else
				{
					String message = String.join(" ", args);
					channel.sendMessage(member, message);
				}
			}
			else
			{
				//Not in channel error
				locale.sendLocale(player, "channel.not-in-channel", "{channel}", channel.getDisplayName());
			}
		}
		else
		{
			//Console error
			locale.sendLocale(sender, "error.console");
		}
	}
}
