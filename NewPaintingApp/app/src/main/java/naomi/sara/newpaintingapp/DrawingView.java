package naomi.sara.newpaintingapp;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;



/**
 * Class that creates a canvas that can be drawn on. Skeleton code is originally taken from
 * <a href="https://reintech.io/blog/implementing-drawing-app-android-custom-canvas-views">here</a>.
 * The class inherits from the View class.
 */
public class DrawingView extends View {
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
    private final Drawable background = ResourcesCompat.getDrawable(res, R.drawable.wood, null);
    private final Drawable background1 = ResourcesCompat.getDrawable(res, R.drawable.canvas, null);
    private final Drawable background2 = ResourcesCompat.getDrawable(res, R.drawable.silk, null);
    private final Drawable background3 = ResourcesCompat.getDrawable(res, R.drawable.glass_plate_background, null);

    // Background array
    private final Drawable[] backgrounds = {background, background1, background2, background3};


    // Current background variable. Is initialized to 0 which is Wood.
    private int chosenBackground = 0;

    // Current brush variable. Is initialized to 0 which is thin (round) brush.
    private int chosenBrush = 0;

    // Instance of PlaySound class - parametric model not in use
    //final PlaySound playSound = new PlaySound();

    // Instance of PlayMusic and PlayHaptics class - datadriven model
    private final PlayHaptics playHapticsFast = new PlayHaptics();
    private PlayHaptics playHapticsMedium = new PlayHaptics();
    private final PlayHaptics playHapticsSlow = new PlayHaptics();

    private final PlayMusic playMusic = new PlayMusic();

    // Velocity tracker
    private VelocityTracker velocityTracker = null;
    private double velocity = 0.0;

    // Haptics controller
    private final ControlFeedback feedbackController = new ControlFeedback();


    /**
     * Constructor of the DrawingView class.
     * Simple constructor to use when creating a view from code.
     * @param context    Context - required parameter for the inherited View constructor
     */
    public DrawingView(Context context) {
        super(context);
        setupDrawing();
    }

    /**
     * Constructor of the DrawingView class.
     * Simple constructor to use when creating a view from code.
     * @param context    Context - required parameter for the inherited View constructor
     * @param attrs      AttributeSet - required parameter for the inherited View constructor
     */
    public DrawingView(Context context, AttributeSet attrs) {
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
    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupDrawing();
    }

    /**
     * Initializes properties for painting, such as the brush color, brush type and thickness
     */
    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(currentColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(currentBrush.first);
        drawPaint.setStrokeCap(currentBrush.second);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
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
    }

    public void changeCanvas(int canvasIndex) {
        chosenBackground = canvasIndex;
        clearView();
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
    public void changeBrush(int width, Paint.Cap cap, int brushIndex) {
        chosenBrush = brushIndex;
        currentBrush = new Pair<>(width, cap);
        drawPath = new Path();
        brushes.add(currentBrush);
        colors.add(currentColor);
        paths.add(drawPath);
        invalidate();

    }

    public Paint getPaint() {
        return drawPaint;
    }

    public Path getPath() {
        return drawPath;
    }

    public int getCanvas() {
        return chosenBackground;
    }


    /**
     * Overrides the onDraw function of the view class. It is used to draw the canvas background
     * and the strokes. The for-loop skeleton is originally taken from
     * <a href="https://stackoverflow.com/questions/43966917/drawing-a-path-with-multiple-colors">
     * here</a> and iterates through the colors, brushes and paths lists to draw the correct path.
     * @param canvas    Canvas - canvas to be drawn on
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        backgrounds[chosenBackground].setBounds(0, 0, getWidth(), getHeight());
        backgrounds[chosenBackground].draw(canvas);

        if (paths.isEmpty()) {
            canvas.drawPath(drawPath, drawPaint);
        } else {
            for (int i = 0; i < paths.size(); i++) {
                drawPaint.setColor(colors.get(i));
                drawPaint.setStrokeWidth(brushes.get(i).first);
                drawPaint.setStrokeCap(brushes.get(i).second);
                canvas.drawPath(paths.get(i), drawPaint);
            }
        }
    }


    /**
     * Overrides the onTouchEvent function of the view class. If the screen is touched, it starts
     * playing the sound for the haptic and tactile feedback, and when the screen is no longer
     * touched it stops the feedback. It also adjusts the haptic and tactile feedback being played
     * based on the velocity.
     * @param event    MotionEvent - used to report movement events
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();
        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);
        Log.d("ACTION EVENT IS", String.valueOf(event.getActionMasked()));


        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    velocityTracker = VelocityTracker.obtain();
                }
                drawPath.moveTo(touchX, touchY);

                feedbackController.initializeFeedback(playHapticsFast, playHapticsMedium,
                        playHapticsSlow, this.getContext(), chosenBrush, chosenBackground);
                feedbackController.initializeMusic(playMusic, this.getContext(), chosenBrush, chosenBackground);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX,touchY);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        velocity = getVelocity(event, pointerId);
                        if (velocity < 0.005f && event.getHistorySize() > 1) {
                            float hisTouchX = event.getHistoricalX(event.getPointerCount() - 1, event.getHistorySize() - 1);
                            float hisTouchY = event.getHistoricalY(event.getPointerCount() - 1, event.getHistorySize() - 1);

                            if (abs(hisTouchX - touchX) < 30.0f && abs(hisTouchY - touchY) < 50.0f) {
                                playHapticsFast.setVolume(0f, 0f);
                                playHapticsMedium.setVolume(0f, 0f);
                                playHapticsSlow.setVolume(0f, 0f);
                            } else {
                                feedbackController.velocityFeedback(velocity, playHapticsFast,
                                        playHapticsMedium, playHapticsSlow);
                                playMusic.setVolume(velocity);
                            }
                        } else {
                            feedbackController.velocityFeedback(velocity, playHapticsFast,
                                    playHapticsMedium, playHapticsSlow);
                            playMusic.setVolume(velocity);
                        }
                    }
                }, 10);


                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                playMusic.pausePlaying();
                playHapticsFast.pausePlaying();
                playHapticsMedium.pausePlaying();
                playHapticsSlow.pausePlaying();
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                playMusic.pausePlaying();
                playHapticsFast.pausePlaying();
                playHapticsMedium.pausePlaying();
                playHapticsSlow.pausePlaying();
                // Return a VelocityTracker object back to be re-used by others.
                velocityTracker.recycle();
                invalidate();
                break;
        }
        invalidate();
        return true;
    }

    /**
     * Clears the canvas of any paint/drawing.
     */
    public void clearView() {
        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).reset();
        }
        invalidate();  // this will force a call to onDraw
    }

    /**
     * Method used to compute the velocity of the user's movements on the screen in m/s.
     * @param event         In actuality this is the MotionEvent.ACTION_MOVE
     * @param pointerId     Pointer identifier associated with a particular pointer data index in
     *                      the event
     * @return              The velocity in m/s
     */
    private double getVelocity(MotionEvent event, int pointerId) {
        velocityTracker.addMovement(event);
        // When you want to determine the velocity, call computeCurrentVelocity(). Then
        // call getXVelocity() and getYVelocity() to retrieve the velocity for each pointer ID.
        velocityTracker.computeCurrentVelocity(1000);

        // Velocity of pixels per second.
        float velocityX = velocityTracker.getXVelocity(pointerId);
        float velocityY = velocityTracker.getYVelocity(pointerId);
        // 1920 X 1200 pixels 0,225 X 0,135 m
        return Math.hypot(velocityX, velocityY) * (0.225/1920);
    }
}