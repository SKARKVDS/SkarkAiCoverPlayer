import org.example.infrastructures.IFileLoader;
import org.example.infrastructures.FilesMusicLoader;
import org.junit.Before;
import org.junit.Test;

public class FileLoaderTests {
    private IFileLoader fileLoader;

    @Before
    public void setUp() {
        fileLoader = new FilesMusicLoader();
    }

    @Test
    public void testLoadArtists() {
        // Given
        String[] artists;
        // When
        artists = fileLoader.loadArtists();
        // Then
        assert(artists.length > 0);
    }

    @Test
    public void testLoadMusic() {
        // Given
        String[] musics;
        // When
        musics = fileLoader.loadMusic("francis");
        // Then
        assert(musics.length > 0);
    }
}
