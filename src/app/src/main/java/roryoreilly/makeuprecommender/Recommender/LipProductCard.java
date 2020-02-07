package roryoreilly.makeuprecommender.Recommender;

import java.util.ArrayList;
import java.util.List;

import roryoreilly.makeuprecommender.Database.MySql;
import roryoreilly.makeuprecommender.Database.Product;
import roryoreilly.makeuprecommender.Helper.UserPackage;
import roryoreilly.makeuprecommender.R;

/**
 * Created by roryoreilly on 05/03/16.
 */
public class LipProductCard {
    MySql db;

    public LipProductCard(MySql db) {
        this.db = db;
    }

    public ProductCard createCard(UserPackage user, String occassion) {
        List<Product> products = new ArrayList<>();


        if (occassion.contains("Night")) {
            products.add(getLipstickNight(user));
            products.add(getLipgloss(products.get(0)));
            products.add(getLipLiner(user));
        } else if (occassion.contains("Everyday")) {
            products.add(getLipstickDay(user));
        }

        return new ProductCard(products, "Lips", R.drawable.goingout_look, true);
    }

    // Gets a lipstick from the user's lip colour for daytime makeup
    public Product getLipstickNight(UserPackage user) {
        List<Product> products = db.getProductsFromType("Lipstick");


        return products.get(0);
    }

    // Gets a lipstick from the user's lip colour for daytime makeup
    public Product getLipstickDay(UserPackage user) {
        float hue = user.getLipColour()[0];
        float saturation = user.getLipColour()[1] * 1.25f;
        float brightness = user.getLipColour()[2];

        List<Product> products = db.getProductsFromType("Lipstick");
        Product bestProduct = products.get(0);

        for (int i = products.size() - 1; i >= 0; i--) {
            Product product = products.get(i);
            double eucDis = Math.sqrt(
                    Math.pow((hue-product.getHue())/3.6, 2)
                            + Math.pow((brightness-product.getBrightness())*100, 2)
                            + Math.pow((saturation-product.getSaturation())*100, 2));

            double eucDisBest = Math.sqrt(
                    Math.pow((hue - bestProduct.getHue()) / 3.6, 2)
                            + Math.pow((brightness - bestProduct.getBrightness()) * 100, 2)
                            + Math.pow((saturation - bestProduct.getSaturation()) * 100, 2));

            if (eucDis < eucDisBest) {
                bestProduct = product;
            }
        }

        return bestProduct;
    }

    public Product getLipgloss(Product product) {
        return null;
    }

    public Product getLipLiner(UserPackage user) {
        return null;
    }
}
