package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;

import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class PMCommand extends HeavenCommand
{
	private MemberManager members;
	
	public PMCommand() 
	{
		super("msg", "message", "pm", "tell", "w", "whisper");
		
		members = ChatPlugin.getMemberManager();
		
		mainCommand = this.new MessageCommand(this);
		
		subCommands.add(this.new HelpCommand(this));
		subCommands.add(this.new MessageCommand(this));
	}
	
	@Override
	protected void onInvalidArgs(CommandSender sender) 
	{
		locale.sendLocale(sender, "pm.invalid-args");
	}
	
	private class MessageCommand extends SubCommand
	{
		public MessageCommand(ProxyCommand command) 
		{
			super(command, "");
		}

		@Override
		protected void onRun(CommandSender sender, String[] args)
		{
			if (!isConsole(sender))
			{
				Member member = members.getMember((ProxiedPlayer)sender);
				
				if (args.length == 0)
				{
					if (member.getFocusedMember() == null)
					{
						//Send not chatting error
						locale.sendLocale(sender, "pm.not-chatting");
					}
					else
					{
						//Stop chatting with current member
						locale.sendLocale(sender, "pm.left", "{target}", member.getFocusedMember().getDisplayName());
						member.setFocusedMember(null);
					}
					
					return;
				}
				else if (args.length == 1)
				{
					ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(args[0]);
					if (targetPlayer != null)
					{
						Member target = members.getMember(targetPlayer);
						if (target.equals(member))
						{
							locale.sendLocale(sender, "pm.self");
							return;
						}
						if (!target.hasIgnored(member))
						{
							member.setFocusedMember(target);
							locale.sendLocale(sender, "pm.chatting", "{target}", target.getDisplayName());
							return;
						}
						else
						{
							//Send ignored error
							locale.sendLocale(sender, "pm.ignored", "{target}", target.getDisplayName());
							return;
						}
					}
					else
					{
						//Send not found error
						locale.sendLocale(sender, "error.not-found", "{arg}", args[0]);
						return;
					}
				}
				else
				{
					ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(args[0]);
					if (targetPlayer != null)
					{
						Member target = members.getMember(targetPlayer);
						if (target.equals(member))
						{
							locale.sendLocale(sender, "pm.self");
							return;
						}
						if (!target.hasIgnored(member))
						{
							//Convert args after the first arg as message
							String privateMessage = "";
							for (int arg = 1; arg < args.length; arg++)
							{
								if (arg != (args.length - 1))
									privateMessage += args[arg] + " ";
								else
									privateMessage += args[arg];
							}
							
							target.sendPrivateMessage(member, privateMessage);
							return;
						}
						else
						{
							//Send ignored error
							locale.sendLocale(sender, "pm.ignored", "{target}", target.getDisplayName());
							return;
						}
					}
					else
					{
						//Send not found error
						locale.sendLocale(sender, "error.not-found", "{arg}", args[0]);
						return;
					}
				}
			}
			
			//Send console error
			locale.sendLocale(sender, "error.console");
		}
	}
	
	/*
	 * PM Help
	 */
	private class HelpCommand extends SubCommand
	{
		public HelpCommand(ProxyCommand command) 
		{
			super(command, "help");
		}

		@Override
		protected void onRun(CommandSender sender, String[] args)
		{
			locale.sendLocale(sender, "help.pm");
		}
	}
}
