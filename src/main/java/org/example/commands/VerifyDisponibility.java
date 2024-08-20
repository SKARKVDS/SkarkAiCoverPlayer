package org.example.commands;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class VerifyDisponibility {
    public boolean isMemberInVoiceChannel(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel to play music").queue();
            return false;
        }

        Member selfMember = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) {
            // join
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        }else {
            // move
            if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                event.reply("You need to be in the same voice channel as me to play music").queue();
                return false;
            }
        }
        return true;
    }
}
