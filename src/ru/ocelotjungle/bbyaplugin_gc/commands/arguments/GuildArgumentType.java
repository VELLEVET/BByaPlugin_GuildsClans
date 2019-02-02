package ru.ocelotjungle.bbyaplugin_gc.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import ru.ocelotjungle.bbyaplugin_gc.commands.Exceptions;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.guildsCfg;

public class GuildArgumentType implements ArgumentType<Byte> {
    private static final Collection<String> EXAMPLES = Arrays.asList("Dwarf", "Builder", "1");

    @Override
    public Byte parse(StringReader stringReader) throws CommandSyntaxException {
        String argumentString = stringReader.readUnquotedString();

        try {
            byte guildId = Byte.parseByte(argumentString);

            if(guildId == 0 || guildsCfg.getValues(false).containsKey(String.valueOf(guildId))) {
                return guildId;
            }
        } catch (NumberFormatException ignored) {
            for (String guildId : guildsCfg.getValues(false).keySet()) {
                String guildEngName = guildsCfg.getString(guildId + ".engName");

                if (guildEngName.equalsIgnoreCase(argumentString)) {
                    return Byte.parseByte(guildId);
                }
            }
        }

        throw Exceptions.guildNotFound.createWithContext(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        final String guildNamePart = builder.getRemaining().toLowerCase();

        return CompletableFuture.supplyAsync(() -> {
            Suggestions result = new Suggestions(context.getRange(), new LinkedList<>());

            for (String guildId : guildsCfg.getValues(false).keySet()) {
                String guildEngName = guildsCfg.getString(guildId + ".engName");

                if (guildEngName.toLowerCase().startsWith(guildNamePart)) {
                    result.getList().add(new Suggestion(context.getRange(), guildEngName));
                }
            }

            return result;
        });
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static GuildArgumentType argument() {
        return new GuildArgumentType();
    }

    public static <S> byte get(CommandContext<S> context, String name) {
        return context.getArgument(name, Byte.TYPE);
    }
}
