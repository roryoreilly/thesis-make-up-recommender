package roryoreilly.makeuprecommender.Database;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import roryoreilly.makeuprecommender.R;

/**
 * Created by roryoreilly on 29/02/16.
 */
public class FillDatabase {
    public static final int PRODUCTS_FILE = R.raw.products;

    public static void addProducts(MySql db, Context context) {
        try {
            BufferedReader reader = readDataFile(context, PRODUCTS_FILE);

            String line;
            while ((line = reader.readLine()) != null) {
                String split[] = line.split(", ");
                Product product;
                if (split[0].equals("000000")) {
                    product = new Product(split[1], split[2], split[3], split[4], split[5]);
                } else {
                    product = new Product(split[1], split[2], split[3], split[4], split[5], split[0]);
                }

                db.insertProduct(product);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static BufferedReader readDataFile(Context context, int file) {
        InputStream is = context.getResources().openRawResource(file);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader inputReader = new BufferedReader(isr, 8192);

        return inputReader;
    }
}
