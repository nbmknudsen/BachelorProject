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

    PlayHaptics playHapticsFast;
    PlayHaptics playHapticsMedium;
    PlayHaptics playHapticsSlow;
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

        controlFeedback.initializeMusic(playMusicFast, playMusicMedium, playMusicSlow, context,
                chosenBrush, chosenBackground);
        //SystemClock.sleep(100);
    }

    /*@Test
    public void initializeMusic() {
        *//*controlFeedback.initializeMusic(playMusicFast, playMusicMedium, playMusicSlow, context,
                chosenBrush, chosenBackground);*//*
        //SystemClock.sleep(1000);
        SystemClock.sleep(10000);
        boolean isPlaying = playMusicFast.isPlaying() && playMusicMedium.isPlaying() &&
                playMusicSlow.isPlaying();
        System.out.printf("Fast: %b, medium: %b, slow: %b%n", playMusicFast.isPlaying(), playMusicMedium.isPlaying(), playMusicSlow.isPlaying());
        assertTrue(isPlaying);
    }*/

    @Test
    public void velocityMusicFast() {
        SystemClock.sleep(100);
        controlFeedback.velocityMusic(0.06, playMusicFast, playMusicMedium, playMusicSlow);
        boolean fastIsPlaying = playMusicFast.getVolume() == 1 && playMusicMedium.getVolume() == 0
                && playMusicSlow.getVolume() == 0;
        assertTrue(fastIsPlaying);
    }

    @Test
    public void velocityMusicMedium() {
        SystemClock.sleep(100);
        controlFeedback.velocityMusic(0.03, playMusicFast, playMusicMedium, playMusicSlow);
        boolean mediumIsPlaying = playMusicFast.getVolume() == 0 && playMusicMedium.getVolume() == 1
                && playMusicSlow.getVolume() == 0;
        assertTrue(mediumIsPlaying);
    }

    @Test
    public void velocityMusicSlow() {
        SystemClock.sleep(100);
        controlFeedback.velocityMusic(0.015, playMusicFast, playMusicMedium, playMusicSlow);
        boolean slowIsPlaying = playMusicFast.getVolume() == 0 && playMusicMedium.getVolume() == 0
                && playMusicSlow.getVolume() == 1;
        assertTrue(slowIsPlaying);
    }
}