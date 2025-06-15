package org.example.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.*;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    private boolean loop = false;

    private static final long TIMEOUT_MINUTES = 10; // Change ici pour la durée d'attente
    private final Guild guild;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> disconnectTask;
    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.guild = guild;
        queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        // Player was paused
        player.setPaused(!player.isPaused());
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        // Player was resumed
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // A track started playing
        cancelDisconnect();
    }

    public void nextTrack() {
        this.player.startTrack(this.queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(loop){
            player.startTrack(track.makeClone(), false);
        }else {
            nextTrack();
        }

        if (queue.isEmpty() && !player.isPaused() && (player.getPlayingTrack() == null)) {
            startDisconnectCountdown();
        }
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }

    public void queue(AudioTrack track) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
        if (!player.startTrack(track, true)) {
            queue.add(track);
        }
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public boolean isRepeat(){
        return loop;
    }

    public void setRepeat(boolean loop){
        this.loop = loop;
    }

    private void startDisconnectCountdown() {
        cancelDisconnect(); // Pour éviter plusieurs timers
        disconnectTask = scheduler.schedule(() -> {
            if (guild.getAudioManager().isConnected()) {
                // Joue le son d'au revoir
                String soundPath = "/var/apps/SKARKAICOVERFEEDER/wwwroot/byesound/bye.wav";
                PlayerManager.get().play(guild, soundPath);

                // Planifie la déconnexion 5 secondes après
                scheduler.schedule(() -> {
                    if (guild.getAudioManager().isConnected()) {
                        guild.getAudioManager().closeAudioConnection();
                    }
                }, 10, TimeUnit.SECONDS); // 5 secondes d'attente pour le son
            }
        }, TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    private void cancelDisconnect() {
        if (disconnectTask != null && !disconnectTask.isDone()) {
            disconnectTask.cancel(false);
        }
    }
}
