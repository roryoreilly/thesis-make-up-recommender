package roryoreilly.makeuprecommender.View;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import roryoreilly.makeuprecommender.R;

/**
 * Created by roryoreilly on 24/02/16.
 */
public class StylesListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer[] imgid;
    private final String[] itemname;

    public StylesListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.item_style, itemname);

        this.context = context;
        this.imgid = imgid;
        this.itemname = itemname;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_style, null,true);

        ImageView imageButton = (ImageView) rowView.findViewById(R.id.styleImageButton);
        imageButton.setTag(itemname[position]);
        imageButton.setAdjustViewBounds(true);

        imageButton.setImageResource(imgid[position]);
        return rowView;
    }
}
