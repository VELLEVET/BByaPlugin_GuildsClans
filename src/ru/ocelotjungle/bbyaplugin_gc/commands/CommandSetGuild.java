package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.entity.Player;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;
import ru.ocelotjungle.bbyaplugin_gc.commands.arguments.GuildArgumentType;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getResultPlayer;

public class CommandSetGuild extends Command {

    public CommandSetGuild(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
            literal("setguild").then(argument("target", argumentPlayer(true))
                    .then(argument("guild", GuildArgumentType.argumentGuild()).executes((ctx) -> {
                        Player player = getResultPlayer(ctx, "target");
                        byte guildId = GuildArgumentType.getGuild(ctx, "guild");

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
