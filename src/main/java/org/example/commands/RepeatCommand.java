package org.example.commands;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.example.lavaplayer.GuildMusicManager;
import org.example.lavaplayer.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepeatCommand implements ICommand{
    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getDescription() {
        return "Repeats the current song";
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

        GuildMusicManager musicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        musicManager.getScheduler().setRepeat(!musicManager.getScheduler().isRepeat());
        event.reply("Repeat " + (musicManager.getScheduler().isRepeat() ? "enabled" : "disabled")).queue();
    }

    @Override
    public void autoComplete(CommandAutoCompleteInteractionEvent event) {
        event.replyChoices(Collections.emptyList()).queue();
    }
}
