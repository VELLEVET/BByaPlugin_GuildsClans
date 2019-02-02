package ru.ocelotjungle.bbyaplugin_gc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ocelotjungle.bbyaplugin_gc.commands.*;
import ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ContextData;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;

public class CommandManager implements CommandExecutor, TabCompleter {
    public static final String COMMAND_GROUP = "bbyaplugingc";

    private CommandDispatcher<ContextData> dispatcher;
    private List<ru.ocelotjungle.bbyaplugin_gc.commands.Command> commands;
    private CommandNode<ContextData> mainNode;

    public CommandManager(JavaPlugin plugin) {
        this.dispatcher = new CommandDispatcher<>();
        this.commands = new LinkedList<>();

        this.mainNode = dispatcher.register(
                literal("gc")
        );
        dispatcher.register(literal(COMMAND_GROUP).redirect(this.mainNode));

        this.commands.add(new CommandHelp(this));
        this.commands.add(new CommandReload(this));
        this.commands.add(new CommandResetInfo(this));
        this.commands.add(new CommandSetGuild(this));
        this.commands.add(new CommandGetGuild(this));
        this.commands.add(new CommandGoToBuild(this));
        this.commands.add(new CommandFullLevelUp(this));
        this.commands.add(new CommandSetClan(this));
        this.commands.add(new CommandGetClan(this));

        PluginCommand command = plugin.getCommand(COMMAND_GROUP);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String fullCommand = getFullCommand(label, args);
        Main.logger().fine("Got command: " + fullCommand);
        try {
            dispatcher.execute(fullCommand, getContextData(sender));
            return true;
        } catch (CommandSyntaxException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String fullCommand = getFullCommand(label, args);
        List<String> result = new LinkedList<>();

        try {
            Suggestions suggestions = dispatcher.getCompletionSuggestions(
                    dispatcher.parse(fullCommand, getContextData(sender))
            ).get();

            for(Suggestion s : suggestions.getList()) {
                result.add(s.getText());
            }
        } catch (InterruptedException | ExecutionException ignored) { }

        return result;
    }

    public CommandNode<ContextData> register(ArgumentBuilder<ContextData, ?> command) {
        CommandNode<ContextData> result = command.build();

        this.mainNode.addChild(result);

        return result;
    }

    public List<ru.ocelotjungle.bbyaplugin_gc.commands.Command> getCommands() {
        return commands;
    }

    public CommandDispatcher<ContextData> getDispatcher() {
        return dispatcher;
    }

    private ContextData getContextData(CommandSender sender) {
        return new ContextData(sender);
    }

    private String getFullCommand(final String label, final String[] args) {
        return (label + " " + String.join(" ", args)).trim();
    }
}
