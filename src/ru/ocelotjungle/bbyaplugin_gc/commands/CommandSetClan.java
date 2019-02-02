package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.arguments.ClanArgumentType.argumentClan;
import static ru.ocelotjungle.bbyaplugin_gc.commands.arguments.ClanArgumentType.getClan;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getResultPlayer;

public class CommandSetClan extends Command {
    public CommandSetClan(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
                literal("setclan").then(argument("target", argumentPlayer(true))
                        .then(argument("clan", argumentClan()).executes((ctx) -> {
                            CommandSender sender = ctx.getSource().getSender();
                            Player player = getResultPlayer(ctx, "target");
                            int clanId = getClan(ctx, "clan");

                            String playerName = player.getName();
                            String playerNameLowercase = playerName.toLowerCase();

                            int playerInfo = fromHex(playersCfg.getString("players." + playerNameLowercase)) & 0xFFFFFF;

                            if (playersCfg.contains("players." + playerNameLowercase)) {
                                playersCfg.set("players." + playerNameLowercase, toHex(clanId<<2*8 | playerInfo&0xFFFF));

                            } else {
                                playersCfg.set("players." + playerNameLowercase, toHex(clanId<<2*8));
                            }
                            sender.sendMessage(format("You set clan (%s; %s) for player %s.",
                                    clansCfg.getString(clanId + ".label"), clanId, playerName));

                            saveCfgs();
                            initCfgsToScoreboard(player, true);

                            return 0;
                        })))
        );
    }

    @Override
    public String getDescription() {
        return "set the player's clan";
    }
}
