package roryoreilly.makeuprecommender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import roryoreilly.makeuprecommender.View.ProfileListAdapter;
import roryoreilly.makeuprecommender.View.StylesListAdapter;

public class StylesActivity extends AppCompatActivity {
    public static final String EXTRA_SKIN = "stylesctivity.extra.skin";
    public static final String EXTRA_HAIR = "stylesctivity.extra.hair";
    public static final String EXTRA_EYE = "stylesctivity.extra.eye";
    public static final String EXTRA_SHAPE = "stylesctivity.extra.shape";
    public static final String EXTRA_OCCASION = "stylesctivity.extra.occasion";

    @Bind(R.id.style_list) ListView listView;

    Integer[] imgid = {
            R.drawable.essentials_look_blur,
            R.drawable.goingout_look_blur,
            R.drawable.natural_blur};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_styles);
        ButterKnife.bind(this);

        final String skin = getIntent().getExtras().getString(EXTRA_SKIN);
        final String hair = getIntent().getExtras().getString(EXTRA_HAIR);
        final String eye = getIntent().getExtras().getString(EXTRA_EYE);
        final String shape = getIntent().getExtras().getString(EXTRA_SHAPE);

        StylesListAdapter adapter = new StylesListAdapter(this, imgid);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String occasion = listView.getSelectedItem().toString();
                Intent intent = new Intent(v.getContext(), RecommendationsActivity.class);
                intent.putExtra(EXTRA_SKIN, skin);
                intent.putExtra(EXTRA_HAIR, hair);
                intent.putExtra(EXTRA_EYE, eye);
                intent.putExtra(EXTRA_SHAPE, shape);
                intent.putExtra(EXTRA_OCCASION, occasion);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_styles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
