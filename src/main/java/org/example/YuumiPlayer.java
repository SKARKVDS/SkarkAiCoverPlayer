package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.example.infrastructures.Token;
import org.example.commands.CommandManager;
import org.example.infrastructures.IFileLoader;
import org.example.infrastructures.FilesMusicLoader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class YuumiPlayer {
    public static void main(String[] args) {
        Token token = new Token();

        String path;

        if (args.length == 0) {
            path = "/var/apps/SKARKAICOVERFEEDER/wwwroot/musics";
        }else {
            path = args[0];
        }

        IFileLoader filesMusicLoader = new FilesMusicLoader(path);
        CommandManager commandManager = new CommandManager(filesMusicLoader);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                filesMusicLoader.refreshMusicList();
                System.out.println("Musics refreshed!");
            } catch (Exception e) {
                System.err.println("Error during music refresh: " + e.getMessage());
            }
        }, 0, 2, TimeUnit.MINUTES);

        JDA jda = JDABuilder.createDefault(token.TOKEN())
                .addEventListeners(commandManager)
                .setActivity(Activity.watching("du porno sur Yuumi"))
                .setStatus(OnlineStatus.ONLINE)
                .build();
    }
}