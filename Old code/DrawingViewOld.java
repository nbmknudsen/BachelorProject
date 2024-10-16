package naomi.sara.newpaintingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that creates a canvas that can be drawn on. Skeleton code is originally taken from
 * <a href="https://reintech.io/blog/implementing-drawing-app-android-custom-canvas-views">here</a>.
 * The class inherits from the View class.
 */
public class DrawingViewOld extends View {
    // Canvas and brush variables
    private Path drawPath;
    private Paint drawPaint;

    private List<Path> paths = new ArrayList<Path>();
    private List<Integer> colors = new ArrayList<Integer>();

    private List<Pair<Integer, Paint.Cap>> brushes = new ArrayList<Pair<Integer, Paint.Cap>>();

    private int currentColor = Color.BLACK;
    private Pair<Integer, Paint.Cap> currentBrush = new Pair<>(40, Paint.Cap.ROUND);

    // Resources variable needed to get backgrounds
    private final Resources res = getResources();

    // Backgrounds
    private final Drawable background = ResourcesCompat.getDrawable(res, R.drawable.g4_wood_type_silver_oak, null);
    private final Drawable background1 = ResourcesCompat.getDrawable(res, R.drawable.g2_granite_type_veneziano, null);
    private final Drawable background2 = ResourcesCompat.getDrawable(res, R.drawable.g9_textile_version2, null);
    private final Drawable background3 = ResourcesCompat.getDrawable(res, R.drawable.glass_plate_background, null);

    // Background array
    private final Drawable[] backgrounds = {background, background1, background2, background3};

    // Current background variable. Is initialized to 0 which is Wood.
    public int chosenBackground = 0;

    // Variables needed for the PlaySound class - parametric mode
    private int sampleRate;
    private int buffLength;
    private int amplitude;
    private int frequency;

    // Instance of PlaySound class - parametric model not in use
    final PlaySound playSound = new PlaySound();

    // Instance of PlayMusic and PlayHaptics class - datadriven model
    final PlayMusic playMusicFast = new PlayMusic();
    final PlayMusic playMusicMedium = new PlayMusic();
    final PlayMusic playMusicSlow = new PlayMusic();
    final PlayHaptics playHaptics = new PlayHaptics();

    // Velocity tracker
    private VelocityTracker velocityTracker = null;
    double velocity = 0.0;

    /**
     * Constructor of the DrawingView class.
     * Simple constructor to use when creating a view from code.
     * @param context    Context - required parameter for the inherited View constructor
     */
    public DrawingViewOld(Context context) {
        super(context);
        setupDrawing();
    }

    /**
     * Constructor of the DrawingView class.
     * Simple constructor to use when creating a view from code.
     * @param context    Context - required parameter for the inherited View constructor
     * @param attrs      AttributeSet - required parameter for the inherited View constructor
     */
    public DrawingViewOld(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    /**
     * Constructor of the DrawingView class.
     * Simple constructor to use when creating a view from code.
     * @param context         Context - required parameter for the inherited View constructor
     * @param attrs           AttributeSet - required parameter for the inherited View constructor
     * @param defStyleAttr    int - required parameter for the inherited View constructor
     */
    public DrawingViewOld(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Initializes values for painting, such as the brush color, brush type and thickness
     */
    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(currentColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(40);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            playMusicFast.initMusic(this.getContext(), R.raw.g4_wood_type_silver_oak_sound);
            playMusicMedium.initMusic(this.getContext(), R.raw.g4_wood_type_silver_oak_sound);
            playMusicSlow.initMusic(this.getContext(), R.raw.thin_wood_s_4_haptics);

        }*/
        //drawPaint.setShadowLayer(40, 0, 0, Color.HSVToColor(new float[]{ 355f, 1f, 0.81f })); //Color.parseColor(
        //drawPaint.setShader(new LinearGradient(0, 0, currentBrush.first, currentBrush.first, Color.WHITE, currentColor, Shader.TileMode.MIRROR));
    }

    /**
     * Overrides the onSizeChanged function of the view class. Dictates what happens if the size of
     * the view changes, such as if the screen is rotated.
     * @param w    Current width of DrawingView
     * @param h    Current height of DrawingView
     * @param old_w    Old width of DrawingView
     * @param old_h    Old height of DrawingView
     */
    @Override
    protected void onSizeChanged(int w, int h, int old_w, int old_h) {
        super.onSizeChanged(w, h, old_w, old_h);
        // drawCanvas = new Canvas(); // draw new canvas when change orientation of screen
    }

    /**
     * Changes the current color. It also adds a new type of Path to the Path list as well as a
     * brush type to the list of brush types and brush color to the list of colors. This is done so
     * we can iterate through the lists in the onDraw function.
     * @param color The brush color
     */
    public void changeColor(int color) {
        currentColor = color;
        drawPath = new Path();
        colors.add(currentColor);
        brushes.add(currentBrush);
        paths.add(drawPath);
        invalidate();

    }

    /**
     * Changes the current brush (width, cap). Adds a new type of Path to the Path list as well as
     * a brush type to the list of brush types and brush color to the list of colors. This is done
     * so we can iterate through the lists in the onDraw function.
     * @param width The width of the brush
     * @param cap   The type of brush head. Dictates what the the beginning and end of the stroke
     *              looks like
     */
    public void changeBrush(int width, Paint.Cap cap) {
        currentBrush = new Pair<>(width, cap);
        drawPath = new Path();
        brushes.add(currentBrush);
        colors.add(currentColor);
        paths.add(drawPath);
        invalidate();

    }

    /**
     * Overrides the onDraw function of the view class. It is used to draw the picture background
     * and the strokes. The for-loop skeleton is originally taken from
     * <a href="https://stackoverflow.com/questions/43966917/drawing-a-path-with-multiple-colors">
     * here</a> and iterates through the colors, brushes and paths lists to draw the correct path.
     * @param canvas    Canvas - canvas to be drawn on
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        backgrounds[chosenBackground].setBounds(0, 0, getWidth(), getHeight());
        backgrounds[chosenBackground].draw(canvas);

        for (int i = 0; i < paths.size(); i++) {
            drawPaint.setColor(colors.get(i));
            drawPaint.setStrokeWidth(brushes.get(i).first);
            drawPaint.setStrokeCap(brushes.get(i).second);
            canvas.drawPath(paths.get(i), drawPaint);
        }
    }

    /*private void playHaptics() {
        *//*(new Thread(() -> {*//*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //Log.d("VELOCITY", "Velocity: " + velocity);
                if (velocity > 0.045) {
                        playMusicMedium.pausePlaying();
                        playMusicSlow.pausePlaying();
                        playMusicFast.startPlaying(this.getContext());
                            *//*playHaptics.initHaptics(this.getContext(), R.raw.thin_wood_s_4_haptics);
                            playHaptics.startPlaying(this.getContext());*//*
                } else if (velocity > 0.025) {
                        playMusicFast.pausePlaying();
                        playMusicSlow.pausePlaying();
                        playMusicMedium.startPlaying(this.getContext());
                            *//*playHaptics.initHaptics(this.getContext(), R.raw.thin_wood_s_4_haptics);
                            playHaptics.startPlaying(this.getContext());*//*
                } else {
                        playMusicFast.pausePlaying();
                        playMusicMedium.pausePlaying();
                        playMusicSlow.startPlaying(this.getContext());
                            *//*playHaptics.initHaptics(this.getContext(), R.raw.thin_wood_s_4_haptics);
                            playHaptics.startPlaying(this.getContext());*//*
                }
            }
        *//*})).start();*//*
    }*/

    /**
     * Overrides the onTouchEvent function of the view class. If the screen is touched, it starts
     * playing the sound for the haptic and tactile feedback, and when the screen is no longer
     * touched it stops the feedback.
     * @param event    MotionEvent - used to report movement events
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    velocityTracker = VelocityTracker.obtain();
                }
                drawPath.moveTo(touchX, touchY);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    (new Thread(()-> {
                        playMusicFast.init(this.getContext(), R.raw.g4_wood_type_silver_oak_sound);
                        playMusicMedium.init(this.getContext(), R.raw.g4_wood_type_silver_oak_sound);
                        playMusicSlow.init(this.getContext(), R.raw.thin_wood_s_4_haptics);
                        playMusicMedium.startPlaying(this.getContext());
                        /*playHaptics.initHaptics(this.getContext());
                        playHaptics.startPlaying(this.getContext());*/
                    })).start();
                }

                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        velocity = getVelocity(event, pointerId);
                    }
                }, 20);
                //playHaptics.stopPlaying();
                //if (playMusicFast.initialized && playMusicMedium.initialized && playMusicSlow.initialized) {
                //playHaptics();
                //}
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                //playMusicFast.stopPlaying();
                //playMusicMedium.stopPlaying();
                //playMusicSlow.stopPlaying();
                playMusicFast.pausePlaying();
                playMusicMedium.pausePlaying();
                playMusicSlow.pausePlaying();
                //playHaptics.stopPlaying();
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                velocityTracker.recycle();
                break;

            default:
                return false;
        }

        invalidate();
        return true;
    }

    private double getVelocity(MotionEvent event, int pointerId) {
        velocityTracker.addMovement(event);
        // When you want to determine the velocity, call computeCurrentVelocity(). Then
        // call getXVelocity() and getYVelocity() to retrieve the velocity for each pointer ID.
        velocityTracker.computeCurrentVelocity(1000);
        // Log velocity of pixels per second. It's best practice to use
        // VelocityTrackerCompat where possible.
        float velocityX = velocityTracker.getXVelocity(pointerId);
        float velocityY = velocityTracker.getYVelocity(pointerId);
                /*Log.d("VELOCITY", "X velocity: " + velocityTracker.getXVelocity(pointerId));
                Log.d("VELOCITY", "Y velocity: " + velocityTracker.getYVelocity(pointerId));*/
        //Log.d("VELOCITY", "Velocity: " + velocityMeter);
        //1920 X 1200 PIXELS 0,225 X 0,135 M
        return Math.hypot(velocityX, velocityY) * (0.225/1920);
    }
    /**
     * Clears the canvas of any drawing
     */
    public void clearView() {
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).reset();
        }
        invalidate();  // this will force a call to onDraw
    }

    /**
     * Plays the correct frequency and amplitude and sample rate of canvas. This is only used with
     * the PlaySound class.
     */
    private void playCanvasHaptics() {
        if (!playSound.isPlaying) {
            // Create a new thread to play the audio.
            (new Thread(() -> {
                switch (chosenBackground) {
                    case 0:
                        sampleRate = 41000;
                        amplitude = 3000;
                        frequency = 140;
                        break;
                    case 1:
                        sampleRate = 41000;
                        amplitude = 3000;
                        frequency = 90;
                        break;
                    case 2:
                        sampleRate = 41000;
                        amplitude = 3000;
                        frequency = 300;
                        break;
                    case 3:
                        sampleRate = 41000;
                        amplitude = 3000;
                        frequency = 30;
                        break;
                    default:
                }
                buffLength = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                playSound.initTrack(sampleRate, buffLength);
                playSound.startPlaying();
                playSound.playback(amplitude, frequency, sampleRate, buffLength);
            })).start();
        }
    }
}
