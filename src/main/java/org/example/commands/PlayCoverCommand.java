package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.infrastructures.IFileLoader;
import org.example.lavaplayer.PlayerManager;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class PlayCoverCommand implements ICommand {
    private final IFileLoader filesMusicLoader;

    public PlayCoverCommand(IFileLoader filesMusicLoader) {
        this.filesMusicLoader = filesMusicLoader;
    }

    @Override
    public String getName() {
        return "playcover";
    }

    @Override
    public String getDescription() {
        return "Plays a song";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
            new OptionData(STRING, "artist", "The artist name", true, true),
            new OptionData(STRING, "song", "The song to play", true, true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        VerifyDisponibility verifyDisponibility = new VerifyDisponibility();
        if(!verifyDisponibility.isMemberInVoiceChannel(event)) {
            return;
        }

        String song = "";
        try {
            song = filesMusicLoader.getSongFromFile(event.getOption("song").getAsString());
            PlayerManager playerManager = PlayerManager.get();
            playerManager.play(event.getGuild(), song);
            event.reply("Playing song...").queue();
        } catch (Exception e) {
            event.reply("An error occurred while trying to play the song : "+song).queue();
        }
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {
        artistCompletion(event, filesMusicLoader);
        if (!event.getOption("artist").getAsString().equals("") && event.getFocusedOption().getName().equals("song")) {
            String[] musicsCompletion = filesMusicLoader.loadMusic(event.getOption("artist").getAsString());
            if (musicsCompletion != null) {
                List<Command.Choice> options = Stream.of(musicsCompletion)
                        .filter(song -> song.startsWith(event.getFocusedOption().getValue()))
                        .map(song -> new Command.Choice(song, song))
                        .collect(Collectors.toList());
                event.replyChoices(options).queue();
            }
        }
    }

    static void artistCompletion(CommandAutoCompleteInteractionEvent event, IFileLoader filesMusicLoader) {
        if (event.getOption("artist") != null && event.getFocusedOption().getName().equals("artist")) {
            String[] artists = filesMusicLoader.loadArtists();
            List<Command.Choice> options = Stream.of(artists)
                    .filter(artist -> artist.startsWith(event.getFocusedOption().getValue()))
                    .map(artist -> new Command.Choice(artist, artist))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }
}
