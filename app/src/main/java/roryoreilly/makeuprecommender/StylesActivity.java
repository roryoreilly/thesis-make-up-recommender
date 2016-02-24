package roryoreilly.makeuprecommender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import roryoreilly.makeuprecommender.Classifier.UserProfile;

public class StylesActivity extends AppCompatActivity {
    public static final String EXTRA_SKIN = "stylesctivity.extra.skin";
    public static final String EXTRA_HAIR = "stylesctivity.extra.hair";
    public static final String EXTRA_EYE = "stylesctivity.extra.eye";
    public static final String EXTRA_SHAPE = "stylesctivity.extra.shape";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_styles);

        String skin = getIntent().getExtras().getString(EXTRA_SKIN);
        String hair = getIntent().getExtras().getString(EXTRA_HAIR);
        String eye = getIntent().getExtras().getString(EXTRA_EYE);
        String shape = getIntent().getExtras().getString(EXTRA_SHAPE);


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
