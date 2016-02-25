package roryoreilly.makeuprecommender.View;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import roryoreilly.makeuprecommender.R;

/**
 * Created by roryoreilly on 24/02/16.
 */
public class StylesListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer[] imgid;

    public StylesListAdapter(Activity context, Integer[] imgid) {
        super(context, R.layout.profile_list);

        this.context = context;
        this.imgid = imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.style_list, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.styleImageButton);

        imageView.setImageResource(imgid[position]);
        return rowView;
    }
}
