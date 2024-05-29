package naomi.sara.newpaintingapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
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
    PlayMusic playMusicFast;
    PlayMusic playMusicMedium;
    PlayMusic playMusicSlow;

    int chosenBrush;
    int chosenBackground;
    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        controlFeedback = new ControlFeedback();

        playMusicFast = new PlayMusic();
        playMusicMedium = new PlayMusic();
        playMusicSlow = new PlayMusic();

        chosenBrush = 0;
        chosenBackground = 0;

        controlFeedback.initializeFeedback(playMusicFast, playMusicMedium, playMusicSlow, context,
                chosenBrush, chosenBackground, "haptics");
        //SystemClock.sleep(100);
    }

    @Test
    public void initializeFeedback() {
        /*controlFeedback.initializeFeedback(playMusicFast, playMusicMedium, playMusicSlow, context,
                chosenBrush, chosenBackground, "haptics");*/
        //SystemClock.sleep(1000);
        SystemClock.sleep(1000);
        boolean isPlaying = playMusicFast.isPlaying() && playMusicMedium.isPlaying() &&
                playMusicSlow.isPlaying();
        assertTrue(isPlaying);
    }

    @Test
    public void velocityFeedback() {
        SystemClock.sleep(100);
        controlFeedback.velocityFeedback(0.06, playMusicFast, playMusicMedium, playMusicSlow);
        boolean fastIsPlaying = playMusicFast.getVolume() == 1 && playMusicMedium.getVolume() == 0
                && playMusicSlow.getVolume() == 0;
        assertTrue(fastIsPlaying);
    }

}