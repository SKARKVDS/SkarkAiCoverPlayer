package org.example.infrastructures;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesMusicLoader implements IFileLoader {
    private final String initialPath;
    private Map<String, String> musicMap;
    private final Map<String, String[]> artistAndMusicMap;
    public FilesMusicLoader() {
        this.initialPath = "src/main/resources";
        this.artistAndMusicMap = new HashMap<>();
        loadAllMusic();
    }

    public String getSongFromFile(String getSong) {
        return musicMap.get(getSong);
    }

    public String[] loadArtists() {
        return artistAndMusicMap.keySet().toArray(new String[0]);
    }

    public String[] loadMusic(String artist) {
        return artistAndMusicMap.get(artist);
    }

    public void loadAllMusic() {
        musicMap = new HashMap<>();
        File directory = new File(initialPath);
        File[] directories = directory.listFiles(File::isDirectory);
        List<String> musicOfArtists;
        if (directories != null) {
            for (File dir : directories) {
                String artist = dir.getName();
                musicOfArtists = new ArrayList<>();
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        String fileNameWithoutExtension = file.getName().substring(0, file.getName().lastIndexOf('.'));
                        musicMap.put(fileNameWithoutExtension, file.getAbsolutePath());
                        musicOfArtists.add(fileNameWithoutExtension);
                    }
                    artistAndMusicMap.put(artist, musicOfArtists.toArray(new String[0]));
                }
            }
        }
    }
}
