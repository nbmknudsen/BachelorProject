package naomi.sara.newpaintingapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.media.SoundPool;
import android.os.SystemClock;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for the ControlFeedback class, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class PlayMusicInstrumentedTest {
    PlayMusic playMusic;

    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        playMusic = new PlayMusic();
        playMusic.init(context, R.raw.sound_round_canvas_f, R.raw.sound_round_canvas_m,
                R.raw.sound_round_canvas_s);
    }


    @Test
    public void startPlaying() {
        playMusic.startPlaying(context);
        // Need to wait a bit for SoundPool to have loaded
        SystemClock.sleep(500);
        assertTrue(playMusic.isPlaying());
    }

    @Test
    public void setVolume() {
        playMusic.setVolume(0.06);
        int volume = playMusic.getVolume("fast");
        assertEquals(1, volume);
    }

    @Test
    public void pausePlaying() {
        playMusic.startPlaying(context);
        // Need to wait a bit for SoundPool to have loaded
        SystemClock.sleep(500);
        playMusic.pausePlaying();
        assertFalse(playMusic.isPlaying());
    }

    @Test
    public void stopPlaying() {
        playMusic.startPlaying(context);
        SoundPool oldSoundPool = playMusic.getSoundPool();
        // Need to wait a bit for SoundPool to have loaded
        SystemClock.sleep(500);
        playMusic.stopPlaying();
        assertNotEquals(oldSoundPool, playMusic.getSoundPool());
    }
}