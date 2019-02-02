package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getResultPlayer;

public class CommandSetLevel extends Command {
    public CommandSetLevel(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
                literal("setlevel").then(argument("target", argumentPlayer(true))
                    .then(argument("level", integer(0, 255)).executes((ctx) -> {
                        CommandSender sender = ctx.getSource().getSender();
                        Player player = getResultPlayer(ctx, "target");
                        int level = getInteger(ctx, "level");

                        String playerName = player.getName();
                        String playerNameLowercase = playerName.toLowerCase();
                        int playerInfo = fromHex(playersCfg.getString("players." + playerNameLowercase)) & 0xFFFFFF;

                        int maxLevel = guildsCfg.contains(((playerInfo>>1*8)&0xFF) + ".maxLevel") ?
                                guildsCfg.getInt(((playerInfo>>1*8)&0xFF) + ".maxLevel") : 255;

                        if(level > maxLevel) {
                            throw Exceptions.wrongGuildLevel.create();
                        }

                        playersCfg.set("players." + playerNameLowercase, toHex(playerInfo&0xFFFF00 | level));
                        sender.sendMessage(format("You set guild level of player %s to %d.", playerName, level));

                        saveCfgs();
                        initCfgsToScoreboard(server.getPlayer(playerNameLowercase), true);

                        return 0;
                    })))
        );
    }

    @Override
    public String getDescription() {
        return "set the player's guild level";
    }
}
