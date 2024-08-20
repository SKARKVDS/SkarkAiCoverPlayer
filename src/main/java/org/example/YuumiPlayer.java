package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.example.infrastructures.Token;
import org.example.commands.CommandManager;
import org.example.infrastructures.IFileLoader;
import org.example.infrastructures.FilesMusicLoader;


public class YuumiPlayer {
    public static void main(String[] args) {
        Token token = new Token();

        IFileLoader filesMusicLoader = new FilesMusicLoader();
        CommandManager commandManager = new CommandManager(filesMusicLoader);

        JDA jda = JDABuilder.createDefault(token.TOKEN())
                .addEventListeners(commandManager)
                .setActivity(Activity.listening("SKARK AI COVERS"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
    }
}