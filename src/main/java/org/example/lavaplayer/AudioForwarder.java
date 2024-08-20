package org.example.lavaplayer;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

/**
 * This is a wrapper around AudioPlayer which makes it behave as an AudioSendHandler for JDA. As JDA calls canProvide
 * before every call to provide20MsAudio(), we pull the frame in canProvide() and use the frame we already pulled in
 * provide20MsAudio().
 */
public class AudioForwarder implements AudioSendHandler
{
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final MutableAudioFrame frame = new MutableAudioFrame();

    /**
     * @param audioPlayer Audio player to wrap.
     */
    public AudioForwarder(AudioPlayer audioPlayer)
    {
        this.audioPlayer = audioPlayer;
        this.frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide()
    {
        return audioPlayer.provide(frame);
    }

    @Override
    public ByteBuffer provide20MsAudio()
    {
        return buffer.flip();
    }

    @Override
    public boolean isOpus()
    {
        return true;
    }
}
