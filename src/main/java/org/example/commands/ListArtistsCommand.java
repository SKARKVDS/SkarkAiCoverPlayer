package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.infrastructures.IFileLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListArtistsCommand implements ICommand {
    private final IFileLoader filesMusicLoader;
    public ListArtistsCommand(IFileLoader filesMusicLoader) {
        this.filesMusicLoader = filesMusicLoader;
    }

    @Override
    public String getName() {
        return "list-artists";
    }

    @Override
    public String getDescription() {
        return "Lists all the artists that are stored locally and that can be played";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        VerifyDisponibility verifyDisponibility = new VerifyDisponibility();
        if(!verifyDisponibility.isMemberInVoiceChannel(event)) {
            return;
        }
        String[] artists = filesMusicLoader.loadArtists();
        if (artists.length==0) {
            event.reply("No artists found").queue();
            return;
        }
        event.reply("List of artists: " + Arrays.stream(filesMusicLoader.loadArtists()).toList()).queue();
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {
        // no need to implement this method
    }
}
