package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;
import ru.ocelotjungle.bbyaplugin_gc.EffectScheduler;
import ru.ocelotjungle.bbyaplugin_gc.Main;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.reloadCfgs;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;

public class CommandReload extends Command {
    public CommandReload(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
                literal("reload").executes((ctx) -> {
                    CommandSender sender = ctx.getSource().getSender();

                    sender.sendMessage("Reloading " + Main.plugin.getName());

                    reloadCfgs();
                    checkObjectives();
                    initCfgsToScoreboard(true);
                    initEffects();

                    new EffectScheduler(Main.plugin);

                    sender.sendMessage(Main.plugin.getName() + " reloaded");

                    return 0;
                })
        );
    }

    @Override
    public String getDescription() {
        return "reload the plugin";
    }
}
