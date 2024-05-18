package others;
import java.io.File;
import java.util.ArrayList;

import static others.Constants.Others.MUSIC_DIRECTORY;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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
            playMusic(MUSIC_DIRECTORY + "/" + musicToPlay.get(i));
            i++;
        } catch (Exception e) {
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


    public static void playMusic(String filepath) {
        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                currentClip = AudioSystem.getClip();
                currentClip.open(audioInput);
                currentClip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void setVolume(float db) {
        FloatControl gainControl = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(db);
    }
}
