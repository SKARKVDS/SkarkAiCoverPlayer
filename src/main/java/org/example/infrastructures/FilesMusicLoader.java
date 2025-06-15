package org.example.infrastructures;

import java.io.File;
import java.util.*;

public class FilesMusicLoader implements IFileLoader {
    private final String initialPath;
    public Map<String, String> musicMap;
    private final Map<String, String[]> artistAndMusicMap;
    public FilesMusicLoader(String path) {
        this.initialPath = path;
        this.artistAndMusicMap = new HashMap<>();
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

    @Override
    public Map<String, String> getMusicMap() {
        if (musicMap == null || musicMap.isEmpty()) {
            this.loadAllMusic();
        }
        return musicMap;
    }

    @Override
    public void refreshMusicList() {
        this.loadAllMusic();
    }

    private void loadAllMusic() {
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
