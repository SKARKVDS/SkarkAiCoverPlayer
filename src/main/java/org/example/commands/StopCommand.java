package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.lavaplayer.GuildMusicManager;
import org.example.lavaplayer.PlayerManager;
import org.example.lavaplayer.TrackScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StopCommand implements ICommand {
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stops the current song";
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
        TrackScheduler scheduler = guildMusicManager.getScheduler();
        scheduler.getQueue().clear();
        scheduler.getPlayer().stopTrack();
        event.reply("**Stopped the player and cleared the queue**").queue();
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {
        event.replyChoices(Collections.emptyList()).queue();
    }
}
