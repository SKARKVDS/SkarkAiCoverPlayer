package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.infrastructures.IFileLoader;

import java.util.Arrays;
import java.util.List;

public class ListSongsArtistCommand implements ICommand {
    private final IFileLoader filesMusicLoader;

    public ListSongsArtistCommand(IFileLoader filesMusicLoader) {
        this.filesMusicLoader = filesMusicLoader;
    }

    @Override
    public String getName() {
        return "list-songs";
    }

    @Override
    public String getDescription() {
        return "Lists all the songs that are stored locally and that can be played";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
            new OptionData(OptionType.STRING, "artist", "The artist to list songs from", true, true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        VerifyDisponibility verifyDisponibility = new VerifyDisponibility();
        if(!verifyDisponibility.isMemberInVoiceChannel(event)) {
            return;
        }
        event.reply("List of songs: " + Arrays.toString(filesMusicLoader.loadMusic(event.getOption("artist").getAsString()))).queue();
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {
        PlayCoverCommand.artistCompletion(event, filesMusicLoader);
    }


}
