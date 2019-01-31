package ru.ocelotjungle.bbyaplugin_gc.commands.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_13_R2.ArgumentEntity;
import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class ArgumentEntityWrapper {
    public static ArgumentEntity getArgumentPlayer(final boolean singleOnly) {
        if(singleOnly) {
            return ArgumentEntity.c();
        } else {
            return ArgumentEntity.d();
        }
    }

    public static List<Player> getResultPlayers(
            final CommandContext<CommandListenerWrapper> context,
            final String argumentName) throws CommandSyntaxException {

        Collection<EntityPlayer> playersVanilla = ArgumentEntity.f(context, argumentName);
        List<Player> playersBukkit = new ArrayList<>();

        for (EntityPlayer playerVanilla : playersVanilla) {
            playersBukkit.add(Bukkit.getPlayer(playerVanilla.getUniqueID()));
        }

        return playersBukkit;
    }
}
