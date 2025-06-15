package org.example.infrastructures;

import java.util.List;
import java.util.Map;

public interface IFileLoader {
    String getSongFromFile(String getSong);
    String[] loadArtists();
    String[] loadMusic(String artist);
    Map<String, String> getMusicMap();
    void refreshMusicList();
}
