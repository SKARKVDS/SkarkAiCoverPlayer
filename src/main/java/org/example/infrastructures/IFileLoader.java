package org.example.infrastructures;

import java.util.List;

public interface IFileLoader {
    String getSongFromFile(String getSong);
    String[] loadArtists();
    String[] loadMusic(String artist);
}
