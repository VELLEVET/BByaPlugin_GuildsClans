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

import static ru.ocelotjungle.bbyaplugin_gc.Configs.clansCfg;

public class ClanArgumentType implements ArgumentType<Byte> {
    private static final Collection<String> EXAMPLES = Arrays.asList("1", "SomeClan");

    @Override
    public Byte parse(StringReader stringReader) throws CommandSyntaxException {
        String argumentString = stringReader.readUnquotedString();

        try {
            byte clanId = Byte.parseByte(argumentString);

            if(clanId == 0 || clansCfg.getValues(false).containsKey(String.valueOf(clanId))) {
                return clanId;
            }
        } catch (NumberFormatException ignored) {
            for(String clanId : clansCfg.getValues(false).keySet()) {
                String clanEngName = clansCfg.getString(clanId + ".label");

                if(clanEngName.equalsIgnoreCase(argumentString)) {
                    return Byte.parseByte(clanId);
                }
            }
        }

        throw Exceptions.clanNotFound.createWithContext(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        final String clanNamePart = builder.getRemaining().toLowerCase();

        return CompletableFuture.supplyAsync(() -> {
            Suggestions result = new Suggestions(context.getRange(), new LinkedList<>());

            for (String clanId : clansCfg.getValues(false).keySet()) {
                String clanLabel = clansCfg.getString(clanId + ".label");

                if (clanLabel.toLowerCase().startsWith(clanNamePart)) {
                    result.getList().add(new Suggestion(context.getRange(), clanLabel));
                }
            }

            return result;
        });
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static ClanArgumentType argumentClan() {
        return new ClanArgumentType();
    }

    public static <S> byte getClan(CommandContext<S> context, String name) {
        return context.getArgument(name, Byte.TYPE);
    }
}
