package org.example.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.lavaplayer.GuildMusicManager;
import org.example.lavaplayer.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PauseCommand implements ICommand {
    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDescription() {
        return "Pauses the current song";
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

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        guildMusicManager.getScheduler().getPlayer().setPaused(guildMusicManager.getScheduler().getPlayer().isPaused());
        event.reply("Paused the current song").queue();
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {
        // No auto completion
        event.replyChoices(Collections.emptyList()).queue();
    }
}
