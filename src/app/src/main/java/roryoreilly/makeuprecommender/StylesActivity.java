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
import roryoreilly.makeuprecommender.Helper.UserPackage;
import roryoreilly.makeuprecommender.View.ProfileListAdapter;
import roryoreilly.makeuprecommender.View.StylesListAdapter;

public class StylesActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "stylesctivity.extra.skin";
    public static final String EXTRA_OCCASION = "stylesctivity.extra.occasion";

    @Bind(R.id.style_button_list) ListView listView;

    Integer[] imgid = {
            R.drawable.essentials_look_blur,
            R.drawable.goingout_look_blur,
            R.drawable.natural_blur};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_styles);
        ButterKnife.bind(this);

        final UserPackage uPackage = getIntent().getExtras().getParcelable(EXTRA_USER);

        final String[] occasionName = new String[]{"Everyday", "Night", "Natural"};
        StylesListAdapter adapter = new StylesListAdapter(this, occasionName, imgid);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String occasion = (String) v.getTag();
                Intent intent = new Intent(v.getContext(), RecommendationsActivity.class);
                intent.putExtra(EXTRA_USER, uPackage);
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
