package ru.ocelotjungle.bbyaplugin_gc.commands.dirty;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_13_R2.ArgumentEntity;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntitySelector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.ocelotjungle.bbyaplugin_gc.commands.Exceptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ArgumentEntityWrapper {
    public static ArgumentEntity argumentPlayer(final boolean singleOnly) {
        if(singleOnly) {
            return ArgumentEntity.c();
        } else {
            return ArgumentEntity.d();
        }
    }

    public static List<Player> getResultPlayers(
            final CommandContext<ContextData> context,
            final String argumentName) throws CommandSyntaxException {

        // Copied from ArgumentEntity.f(CommandContext<CommandListenerWrapper>, String) method
        Collection<EntityPlayer> playersVanilla =
                context.getArgument(argumentName, EntitySelector.class)
                        .d(context.getSource().getCommandListenerWrapper());

        if(playersVanilla.size() <= 0) {
            throw Exceptions.noPlayersFound.create();
        }

        List<Player> playersBukkit = new ArrayList<>();

        for (EntityPlayer playerVanilla : playersVanilla) {
            playersBukkit.add(Bukkit.getPlayer(playerVanilla.getUniqueID()));
        }

        return playersBukkit;
    }

    public static Player getResultPlayer(
            final CommandContext<ContextData> context,
            final String argumentName) throws CommandSyntaxException {

        List<Player> players = getResultPlayers(context, argumentName);
        if(players.size() > 1) {
            throw Exceptions.tooManyPlayersFound.create();
        }

        return players.get(0);
    }
}
