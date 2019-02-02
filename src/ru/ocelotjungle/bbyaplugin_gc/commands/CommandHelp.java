package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;
import ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ContextData;

import java.util.LinkedList;
import java.util.List;

import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;

public class CommandHelp extends ru.ocelotjungle.bbyaplugin_gc.commands.Command {
    private static final String HEADER = ChatColor.DARK_PURPLE + "<------------ Help for Guilds&Clans ------------>";
    private static final String COMMAND = ChatColor.GREEN + "/";
    private static final String DESCRIPTION = ChatColor.DARK_GREEN + " - ";

    public CommandHelp(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
                literal("help").executes((ctx) -> {
                    printHelp(ctx.getSource());
                    return 0;
                })
        );

        // Register some aliases for the help command
        commandManager.getDispatcher().register(
                literal(CommandManager.COMMAND_GROUP).executes((ctx) -> {
                    printHelp(ctx.getSource());
                    return 0;
                })
        );

        commandManager.getDispatcher().register(
                literal("gc").executes((ctx) -> {
                    printHelp(ctx.getSource());
                    return 0;
                })
        );
    }

    private void printHelp(ContextData context) {
        CommandSender sender = context.getSender();

        List<String> message = new LinkedList<>();
        message.add(HEADER);

        for(Command cmd : commandManager.getCommands()) {
            String cmdUsage = String.join(" ", commandManager.getDispatcher().getPath(cmd.getNode()));
            String cmdDescription = cmd.getDescription();
            message.add(COMMAND + cmdUsage + DESCRIPTION + cmdDescription);
        }

        message.add(HEADER);
        sender.sendMessage(message.toArray(new String[0]));
    }

    @Override
    public String getDescription() {
        return "get help";
    }
}
