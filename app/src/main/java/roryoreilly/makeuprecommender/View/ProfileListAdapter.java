package roryoreilly.makeuprecommender.View;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import roryoreilly.makeuprecommender.R;

public class ProfileListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public ProfileListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.profile_list, itemname);

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.profile_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.classifier_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.classifier_icon);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    };

}
