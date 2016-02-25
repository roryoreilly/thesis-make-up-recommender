package roryoreilly.makeuprecommender;

import android.os.Bundle;
import android.app.Activity;

public class RecommendationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        String skin = getIntent().getExtras().getString(StylesActivity.EXTRA_SKIN);
        String hair = getIntent().getExtras().getString(StylesActivity.EXTRA_HAIR);
        String eye = getIntent().getExtras().getString(StylesActivity.EXTRA_EYE);
        String shape = getIntent().getExtras().getString(StylesActivity.EXTRA_SHAPE);
        String occasion = getIntent().getExtras().getString(StylesActivity.EXTRA_OCCASION);
    }

}
