package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.guildsCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getPlayerName;

public class CommandGetGuild extends Command {
    public CommandGetGuild(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
            literal("getguild").then(argument("target", argumentPlayer(true)).executes((ctx) -> {
                CommandSender sender = ctx.getSource().getSender();

                String playerName = getPlayerName(ctx, "target");
                String playerNameLowercase = playerName.toLowerCase();

                int guild = (fromHex(playersCfg.getString("players." + playerNameLowercase))>>1*8) & 0xFF;
                sender.sendMessage(format("%s's guild is (%s; %d).",
                        playerName, guildsCfg.getString(guild + ".engName"), guild));

                return 0;
            }))
        );
    }

    @Override
    public String getDescription() {
        return null;
    }
}
