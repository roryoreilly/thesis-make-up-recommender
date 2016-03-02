package roryoreilly.makeuprecommender;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.Database.MySql;
import roryoreilly.makeuprecommender.Database.Product;
import roryoreilly.makeuprecommender.Recommender.FaceProductCard;
import roryoreilly.makeuprecommender.Recommender.ProductCard;
import roryoreilly.makeuprecommender.View.ProductCardAdapter;

public class RecommendationsActivity extends Activity {
    protected RecyclerViewPager mRecyclerView;
    MySql db;

    String skin;
    String eye;
    String occasion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        db = new MySql(this);

        skin = getIntent().getExtras().getString(StylesActivity.EXTRA_SKIN);
        String hair = getIntent().getExtras().getString(StylesActivity.EXTRA_HAIR);
        eye = getIntent().getExtras().getString(StylesActivity.EXTRA_EYE);
        String shape = getIntent().getExtras().getString(StylesActivity.EXTRA_SHAPE);
        occasion = getIntent().getExtras().getString(StylesActivity.EXTRA_OCCASION);

        initViewPager();
    }

    protected void initViewPager() {
        mRecyclerView = (RecyclerViewPager)findViewById(R.id.viewpager);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerView.setLayoutManager(layout);


        List<Product> items = new ArrayList<>();

        FaceProductCard fpc = new FaceProductCard(db);
        items.add(fpc.getFoundation(skin));
        items.add(fpc.getConcealer(skin));
        items.add(fpc.getBronzer());
        items.add(fpc.getBlush("Fair", "Warm", "Blue", "Day"));

        List<ProductCard> cards = new ArrayList<>();
        cards.add(new ProductCard(items, "Face", R.drawable.boldlips_look, true));
        cards.add(new ProductCard(items, "Eyes", R.drawable.glameyes_look, true));
        cards.add(new ProductCard(items, "Lips", R.drawable.goingout_look, true));

        mRecyclerView.setAdapter(new ProductCardAdapter(this, mRecyclerView, cards));

//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = mRecyclerView.getChildCount();
                int width = mRecyclerView.getChildAt(0).getWidth();
                int padding = (mRecyclerView.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        mRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
            }
        });

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mRecyclerView.getChildCount() < 3) {
                    if (mRecyclerView.getChildAt(1) != null) {
                        if (mRecyclerView.getCurrentPosition() == 0) {
                            View v1 = mRecyclerView.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = mRecyclerView.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (mRecyclerView.getChildAt(0) != null) {
                        View v0 = mRecyclerView.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (mRecyclerView.getChildAt(2) != null) {
                        View v2 = mRecyclerView.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });
    }
}