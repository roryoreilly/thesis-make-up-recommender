package roryoreilly.makeuprecommender;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.Recommender.Product;
import roryoreilly.makeuprecommender.View.ProductItemAdapter;

public class RecommendationsActivity extends Activity {

    List<Product> products;

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

        // Initialize products
        products = new ArrayList<Product>();
        products.add(new Product("Foundation", "Boo", true));
        products.add(new Product("Concealer", "Foo", true));
        products.add(new Product("Highlighter", "Bar", true));
        // Create adapter passing in the sample user data
        ProductItemAdapter adapter = new ProductItemAdapter(products);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }

}
