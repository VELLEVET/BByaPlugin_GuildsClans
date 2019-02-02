package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.clansCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getResultPlayer;

public class CommandGetClan extends Command {
    public CommandGetClan(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
                literal("getclan").then(argument("target", argumentPlayer(true)).executes((ctx) -> {
                    CommandSender sender = ctx.getSource().getSender();
                    Player player = getResultPlayer(ctx, "target");

                    String playerName = player.getName();
                    String playerNameLowercase = playerName.toLowerCase();

                    int clan = (fromHex(playersCfg.getString("players." + playerNameLowercase))>>2*8) & 0xFF;
                    sender.sendMessage(format("%s's clan is (%s; %d).", playerName, clansCfg.getString(clan + ".label"), clan));

                    return 0;
                }))
        );
    }

    @Override
    public String getDescription() {
        return "get the player's clan";
    }
}
