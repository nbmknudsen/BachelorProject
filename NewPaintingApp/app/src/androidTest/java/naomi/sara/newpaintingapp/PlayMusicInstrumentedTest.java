package naomi.sara.newpaintingapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.MediaPlayer;
import android.os.SystemClock;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for the PlayHaptics class, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class PlayMusicInstrumentedTest {

    PlayMusic playMusic;
    MediaPlayer mp;
    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        playMusic = new PlayMusic();
        /*playMusic.init(context, R.raw.haptics_round_canvas_f);
        playMusic.startPlaying(context);*/
        playMusic.init(context, R.raw.haptics_round_canvas_f);
    }

    @Test
    public void startPlaying() {
        playMusic.startPlaying(context);
        // Need to wait a bit for MediaPlayer to be prepared
        SystemClock.sleep(100);
        assertTrue(playMusic.isPlaying());
    }

    @Test
    public void setVolume() {
        playMusic.startPlaying(context);
        // Need to wait a bit for MediaPlayer to be prepared
        SystemClock.sleep(100);
        playMusic.setVolume(0.0f,0.0f);
        assertEquals(0.0f, playMusic.getVolume(), 0.0);
    }

    @Test
    public void pausePlaying() {
        playMusic.startPlaying(context);
        // Need to wait a bit for MediaPlayer to be prepared
        SystemClock.sleep(100);
        playMusic.pausePlaying();
        assertFalse(playMusic.isPlaying());
    }

    @Test
    public void stopPlaying() {
        playMusic.startPlaying(context);
        // Need to wait a bit for MediaPlayer to be prepared
        SystemClock.sleep(100);
        playMusic.stopPlaying();
        assertFalse(playMusic.isPlaying());
    }
}