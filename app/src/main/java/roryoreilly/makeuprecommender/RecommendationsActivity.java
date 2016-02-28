package roryoreilly.makeuprecommender;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.Recommender.ProductCard;
import roryoreilly.makeuprecommender.Recommender.ProductItem;
import roryoreilly.makeuprecommender.View.ProductCardAdapter;
import roryoreilly.makeuprecommender.View.ProductItemAdapter;

public class RecommendationsActivity extends Activity {

    List<ProductCard> pCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        String skin = getIntent().getExtras().getString(StylesActivity.EXTRA_SKIN);
        String hair = getIntent().getExtras().getString(StylesActivity.EXTRA_HAIR);
        String eye = getIntent().getExtras().getString(StylesActivity.EXTRA_EYE);
        String shape = getIntent().getExtras().getString(StylesActivity.EXTRA_SHAPE);
        String occasion = getIntent().getExtras().getString(StylesActivity.EXTRA_OCCASION);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.productCard);

        // Initialize productItems
        List<ProductItem> pItems = new ArrayList<ProductItem>();
        pItems.add(new ProductItem("Foundation", "Boo", true));
        pItems.add(new ProductItem("Concealer", "Foo", true));
        pItems.add(new ProductItem("Highlighter", "Bar", true));
        Integer imgid = R.drawable.essentials_look_blur;

        pCards = new ArrayList<>();
        pCards.add(new ProductCard(pItems, "Face", imgid, true));
        pCards.add(new ProductCard(pItems, "Face", imgid, true));
        pCards.add(new ProductCard(pItems, "Face", imgid, true));
        pCards.add(new ProductCard(pItems, "Face", imgid, true));
        // Create adapter passing in the sample user data
        ProductCardAdapter adapter = new ProductCardAdapter(pCards, this);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        LinearLayoutManager llm =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvContacts.setLayoutManager(llm);
    }

}
