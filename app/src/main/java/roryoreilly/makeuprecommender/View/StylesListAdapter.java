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
    private final String[] title;
    private final Integer[] imgid;

    public StylesListAdapter(Activity context, String[] title, Integer[] imgid) {
        super(context, R.layout.profile_list, title);

        this.context = context;
        this.title = title;
        this.imgid = imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.style_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.classifier_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.classifier_icon);

        txtTitle.setText(title[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    };
}
