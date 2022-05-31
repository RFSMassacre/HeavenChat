package us.rfsmassacre.HeavenChat.Commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import us.rfsmassacre.HeavenChat.Channels.PluginChannel;
import us.rfsmassacre.HeavenChat.Members.Member;
import us.rfsmassacre.HeavenLib.BungeeCord.Commands.ProxyCommand;

public class ExecuteCommand extends HeavenCommand
{
    public ExecuteCommand()
    {
        super("execute");

        mainCommand = new SendCommand(this);
    }

    @Override
    protected void onInvalidArgs(CommandSender sender)
    {
        locale.sendLocale(sender, "execute.invalid-args");
    }

    private class SendCommand extends SubCommand
    {
        public SendCommand(ProxyCommand command)
        {
            super(command, "");
        }

        @Override
        protected void onRun(CommandSender sender, String[] args)
        {
            if (args.length >= 2)
            {
                String serverName = args[0];
                String command = args[1];
                for (int slot = 2; slot < args.length; slot++)
                {
                    command += " " + args[slot];
                }

                ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);
                server.sendData(PluginChannel.COMMAND, command.getBytes());
                return;
            }

            //Invalid args
            locale.sendLocale(sender, "execute.invalid-args");
            return;
        }
    }
}
