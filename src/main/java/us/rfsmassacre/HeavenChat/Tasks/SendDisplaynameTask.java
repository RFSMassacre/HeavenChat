package us.rfsmassacre.HeavenChat.Tasks;

import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Members.Member;

public class SendDisplaynameTask extends SendProxyTask
{
    public SendDisplaynameTask(Member member)
    {
        super(member.getPlayer(), PluginChannel.DISPLAYNAME, member.getDisplayName());
    }
}
