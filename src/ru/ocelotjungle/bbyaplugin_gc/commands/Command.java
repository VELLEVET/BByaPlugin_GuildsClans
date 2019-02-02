package ru.ocelotjungle.bbyaplugin_gc.commands;

import com.mojang.brigadier.tree.CommandNode;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;
import ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ContextData;

public abstract class Command {
    protected CommandManager commandManager;
    protected CommandNode<ContextData> mainNode;

    public Command(CommandManager commandManager) {
        this.commandManager = commandManager;

        this.init();
    }

    protected abstract void init();

    public CommandNode<ContextData> getNode() {
        return mainNode;
    }

    public abstract String getDescription();
}
