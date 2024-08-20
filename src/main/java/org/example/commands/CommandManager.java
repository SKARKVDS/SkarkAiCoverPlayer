package org.example.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.infrastructures.IFileLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager(IFileLoader filesMusicLoader) {
        addCommand(new PlayCoverCommand(filesMusicLoader));
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new PauseCommand());
        addCommand(new ListArtistsCommand(filesMusicLoader));
        addCommand(new ListSongsArtistCommand(filesMusicLoader));
        addCommand(new ClearCommand());
        addCommand(new RepeatCommand());
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (ICommand cmd : commands) {
                guild.upsertCommand(cmd.getName(), cmd.getDescription()).addOptions(cmd.getOptions()).queue();
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        commands.stream().filter((cmd) -> cmd.getName().equals(event.getName())).forEach((cmd) -> cmd.execute(event));
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        commands.stream().filter((cmd) -> cmd.getName().equals(event.getName())).forEach((cmd) -> cmd.autoComplete(event));
    }

    private void addCommand(ICommand cmd) {
        commands.add(cmd);
    }
}
