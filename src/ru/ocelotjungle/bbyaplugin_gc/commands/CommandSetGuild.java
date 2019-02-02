package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.entity.Player;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;
import ru.ocelotjungle.bbyaplugin_gc.commands.arguments.GuildArgumentType;

import java.util.List;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getResultPlayers;

public class CommandSetGuild extends Command {

    public CommandSetGuild(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
            literal("setguild").then(argument("target", argumentPlayer(true))
                    .then(argument("guild", GuildArgumentType.argument()).executes((ctx) -> {
                        List<Player> players = getResultPlayers(ctx, "target");
                        byte guildId = GuildArgumentType.get(ctx, "guild");

                        if(players.size() != 1) {
                            throw Exceptions.noPlayersFound.create();
                        }

                        Player player = players.get(0);
                        String playerName = player.getName();
                        String playerNameLowercase = playerName.toLowerCase();
                        int playerInfo = fromHex(playersCfg.getString("players." + playerNameLowercase)) & 0xFFFFFF;

                        if (playersCfg.contains("players." + playerNameLowercase)) {
                            playersCfg.set("players." + playerNameLowercase, toHex(guildId<<8 | playerInfo&0xFF00FF));
                        } else {
                            playersCfg.set("players." + playerNameLowercase, toHex(guildId<<8));
                        }

                        saveCfgs();
                        initCfgsToScoreboard(player, true);

                        ctx.getSource().getSender().sendMessage(format("You set guild (%s; %s) for player %s.",
                                guildsCfg.getString(String.valueOf(guildId) + ".engName"), guildId, playerName));

                        return 0;
                    }))
            )
        );
    }

    @Override
    public String getDescription() {
        return "set the player's guild";
    }
}
