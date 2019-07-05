package ru.ocelotjungle.bbyaplugin_gc.commands.dirty;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_14_R1.ArgumentEntity;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.EntitySelector;
import ru.ocelotjungle.bbyaplugin_gc.Main;
import ru.ocelotjungle.bbyaplugin_gc.commands.Exceptions;

import java.lang.reflect.Field;
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

    public static List<String> getPlayerNames(
            final CommandContext<ContextData> context,
            final String argumentName) throws CommandSyntaxException {

        // See ArgumentEntity.f(CommandContext<CommandListenerWrapper>, String) method
        EntitySelector selector = context.getArgument(argumentName, EntitySelector.class);
        Collection<EntityPlayer> playersVanilla = selector.d(context.getSource().getCommandListenerWrapper());
        if(playersVanilla.size() > 0) {
            List<String> players = new ArrayList<>();

            for (EntityPlayer playerVanilla : playersVanilla) {
                players.add(playerVanilla.getName());
            }

            return players;
        } else {
            List<String> players = new ArrayList<>();

            // Try to see if the selector is a nickname
            // EntitySelector.j field contains nickname if specified or null otherwise (as of 1.13.2)
            try {
                Field nameField = EntitySelector.class.getDeclaredField("j");
                boolean wasAccessible = nameField.isAccessible();
                nameField.setAccessible(true);
                String name = (String) nameField.get(selector);
                nameField.setAccessible(wasAccessible);

                if(name != null) {
                    players.add(name);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                Main.logger().warning(e.getMessage());
                throw Exceptions.noPlayersFound.create();
            }

            if(players.size() <= 0) {
                throw Exceptions.noPlayersFound.create();
            }

            return players;
        }
    }

    public static String getPlayerName(
            final CommandContext<ContextData> context,
            final String argumentName) throws CommandSyntaxException {

        List<String> players = getPlayerNames(context, argumentName);
        if(players.size() > 1) {
            throw Exceptions.tooManyPlayersFound.create();
        }

        return players.get(0);
    }
}
