package naomi.sara.newpaintingapp;

import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
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
public class ControlFeedbackInstrumentedTest {
    ControlFeedback controlFeedback;
    PlayMusic playMusic;

    PlayHaptics playFast;
    PlayHaptics playMedium;
    PlayHaptics playSlow;

    int chosenBrush;
    int chosenBackground;
    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        controlFeedback = new ControlFeedback();

        playMusic = new PlayMusic();
        chosenBrush = 0;
        chosenBackground = 0;
        controlFeedback.initializeMusic(playMusic, context, chosenBrush, chosenBackground);

        playFast = new PlayHaptics();
        playMedium = new PlayHaptics();
        playSlow = new PlayHaptics();
        playFast.init(context, R.raw.sound_round_canvas_f);
        playMedium.init(context, R.raw.sound_round_canvas_m);
        playMedium.init(context, R.raw.sound_round_canvas_s);

    }

    @Test
    public void initializeMusic() {
        // Need to wait a bit for MediaPLayer to be prepared
        SystemClock.sleep(200);
        assertTrue(playMusic.isPlaying());
    }

    @Test
    public void velocityFeedback() {
        controlFeedback.velocityFeedback(0.06, playFast, playMedium, playSlow);
        boolean volume = playFast.getVolume() == 1.0f && playMedium.getVolume() == 0.0f  &&
                playMedium.getVolume() == 0.0f;
        assertEquals(true, volume);
    }
}