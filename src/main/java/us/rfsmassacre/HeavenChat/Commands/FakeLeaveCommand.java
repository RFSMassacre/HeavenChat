package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.rfsmassacre.HeavenChat.ChatPlugin;
import us.rfsmassacre.HeavenChat.Managers.MemberManager;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class FakeLeaveCommand extends HeavenCommand
{
    private MemberManager memberManager;

    public FakeLeaveCommand()
    {
        super("fakeleave");

        this.memberManager = ChatPlugin.getMemberManager();
        this.mainCommand = new LeaveCommand(this);
    }

    @Override
    protected void onInvalidArgs(CommandSender sender)
    {

    }

    private class LeaveCommand extends SubCommand
    {
        public LeaveCommand(ProxyCommand command)
        {
            super(command, "");
        }

        @Override
        protected void onRun(CommandSender sender, String[] args)
        {
            if (!isConsole(sender))
            {
                Member member = memberManager.getMember((ProxiedPlayer)sender);
                locale.broadcastLocale(false, "login.logout-message", "{player}",
                        member.getDisplayName());
                return;
            }

            locale.sendLocale(sender, "error.console");
        }
    }
}
