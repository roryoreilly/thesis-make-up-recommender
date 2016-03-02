package roryoreilly.makeuprecommender.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySql extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_database";

    private static final String PRODUCT_TABLE_NAME = "products";
    private static final String PRODUCT_ID = "id";
    private static final String PRODUCT_COLOUR = "rgb";
    private static final String PRODUCT_TYPE = "type";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_DESCRIPTION = "description";
    private static final String PRODUCT_URL = "url";
    private static final String PRODUCT_IMAGE = "image";

    private static final String[] PRODUCT_COLUMNS = { PRODUCT_ID,
            PRODUCT_COLOUR,
            PRODUCT_TYPE,
            PRODUCT_NAME,
            PRODUCT_DESCRIPTION,
            PRODUCT_URL,
            PRODUCT_IMAGE};

    public MySql(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_TABLE_BUSINESS = "CREATE TABLE "+ PRODUCT_TABLE_NAME +
                " (" + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRODUCT_COLOUR + " TEXT, "
                + PRODUCT_TYPE + " TEXT, "
                + PRODUCT_NAME + " TEXT, "
                + PRODUCT_DESCRIPTION + " TEXT,"
                + PRODUCT_URL + " TEXT,"
                + PRODUCT_IMAGE + " TEXT )";

        db.execSQL(CREATE_TABLE_BUSINESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop tables if already exists
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE_NAME);
        this.onCreate(db);
    }

    public long insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCT_TYPE, product.getType());
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_DESCRIPTION, product.getDescription());
        values.put(PRODUCT_URL, product.getUrl());
        values.put(PRODUCT_IMAGE, product.getImage());

        String colour = product.getRgb();
        if (colour != null) {
            values.put(PRODUCT_COLOUR, colour);
        }

        long product_id = db.insert(PRODUCT_TABLE_NAME, null, values);
        db.close();

        return product_id;
    }


    public List<Product> getProductsFromType(String type){
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = " "+PRODUCT_TYPE+" = ?";
        Cursor cursor = db.query(PRODUCT_TABLE_NAME,
                PRODUCT_COLUMNS, whereClause, new String[]{type},
                null, null, null, null);

        List<Product> productList = new ArrayList<>();

        // parse all results and add them to the list
        if (cursor.moveToFirst()) {
            Product product;
            do {
                product = new Product(
                cursor.getString(2),        //type
                        cursor.getString(3),        //name
                        cursor.getString(4),        //description
                        cursor.getString(5),        //url
                        cursor.getString(6));       //rgb

                if (cursor.getString(1) != null) {
                    product.setHsv(cursor.getString(1));
                }
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return productList;
    }
}
