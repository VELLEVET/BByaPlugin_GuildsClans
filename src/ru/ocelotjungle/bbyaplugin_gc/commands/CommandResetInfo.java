package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;

import java.util.Map;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Main.scboard;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getPlayerName;

public class CommandResetInfo extends Command {
    public CommandResetInfo(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
                literal("resetinfo").then(argument("target", argumentPlayer(true)).executes((ctx) -> {
                    CommandSender sender = ctx.getSource().getSender();
                    String playerName = getPlayerName(ctx, "target");
                    String playerNameLowercase = playerName.toLowerCase();

                    int playerInfo = fromHex(playersCfg.getString("players." + playerNameLowercase)) & 0xFFFFFF;
                    if (playerInfo != 0) {
                        playersCfg.set("players." + playerNameLowercase, "0x0");
                        sender.sendMessage(format("You set %s's info to 0.", playerName));
                    } else {
                        playersCfg.set("players." + playerNameLowercase, null);
                        sender.sendMessage(format("You reset %s's info.", playerName));
                    }

                    scboard.getObjective("ClanID").getScore(playerNameLowercase).setScore(0);

                    for (Map.Entry<String, Object> entry2 : guildsCfg.getValues(false).entrySet()) {
                        scboard.getObjective(format("T_%s", guildsCfg.getString(entry2.getKey() + ".engName")))
                                .getScore(playerNameLowercase).setScore(0);
                    }

                    saveCfgs();
                    initCfgsToScoreboard(server.getPlayer(playerName), true);

                    return 0;
                }))
        );
    }

    @Override
    public String getDescription() {
        return "reset the player's guild and clan";
    }
}
