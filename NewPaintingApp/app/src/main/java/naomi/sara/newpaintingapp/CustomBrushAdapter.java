package naomi.sara.newpaintingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Custom adapter that provides access to the brush items. This adapter is responsible for making
 * a View for each of the brush items. The class inherits from the BaseAdapter class.
 */
public class CustomBrushAdapter extends BaseAdapter {
    Context context;
    int[] brushes;
    LayoutInflater inflater;

    /**
     * Constructor of the CustomBrushAdapter class. It initializes the LayoutInflater inflater.
     * @param applicationContext Context - used to set the context of the class
     * @param brushes Int array of brush pictures
     */
    public CustomBrushAdapter(Context applicationContext, int[] brushes) {
        this.context = applicationContext;
        this.brushes = brushes;
        inflater = (LayoutInflater.from(applicationContext));
    }

    /**
     * It is required to override the BaseAdapters getCount() method.
     * @return The number of brush items that are in the brush picture array
     */
    @Override
    public int getCount() {
        return brushes.length;
    }

    /**
     * It is required to override the BaseAdapters getItem() method.
     * @param i Position of the item whose data we want within the adapter's
     * data set.
     * @return null
     */
    @Override
    public Object getItem(int i) {
        return null;
    }

    /**
     * It is required to override the BaseAdapters getItemId() method.
     * @param i The position of the item within the adapter's data set whose row id we want.
     * @return 0
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Overrides the getView function of the BaseAdapter class. The function gets a View that
     * displays and sets the brush pictures in the custom_brush_items layout.
     * @param i The position of the item within the adapter's data set of the brushes
     * @param view The old view we save the inflated custom brush items in if it is null
     * @param viewGroup The parent that this view will eventually be attached to
     * @return The view with the brush images as a spinner
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.custom_brush_items, null);
            ImageView icon = (ImageView) view.findViewById(R.id.brushImageView);
            icon.setImageResource(brushes[i]);
        } else {
            ImageView icon = (ImageView) view.findViewById(R.id.brushImageView);
            icon.setImageResource(brushes[i]);
        }
        return view;
    }
}
