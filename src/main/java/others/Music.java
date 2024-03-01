package others;
import java.io.File;
import java.util.ArrayList;

import static others.Constants.Others.MUSIC_DIRECTORY;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {
    private static Clip currentClip;
    private static int i = 0;

    public static void update() {
        if (currentClip == null
            || currentClip.getMicrosecondLength() == currentClip.getMicrosecondPosition()) {
            playNext();
        }
    }

    public static void playNext() {
        File file = new File(MUSIC_DIRECTORY);
        ArrayList<String> musicToPlay = getFilePathOfAllInDirectory(file);
        try {
            if (i >= musicToPlay.size()) {
                i = 0;
            }
            System.out.println("Playing " + musicToPlay.get(i));
            currentClip = playMusic(MUSIC_DIRECTORY + "/" + musicToPlay.get(i));
            i++;
        } catch (Exception e) {
            return;
        }
    }

    public static ArrayList<String> getFilePathOfAllInDirectory(final File folder) {
        ArrayList<String> musics = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                for (String music : getFilePathOfAllInDirectory(fileEntry)) {
                    musics.add(music);
                }
            } else {
                musics.add(fileEntry.getName());
            }
        }
        return musics;
    }


    public static Clip playMusic(String filepath) {
        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                return clip;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }
}
