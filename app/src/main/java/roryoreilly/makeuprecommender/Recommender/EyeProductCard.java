package roryoreilly.makeuprecommender.Recommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import roryoreilly.makeuprecommender.Database.MySql;
import roryoreilly.makeuprecommender.Database.Product;
import roryoreilly.makeuprecommender.Helper.UserPackage;
import roryoreilly.makeuprecommender.R;

/**
 * Created by roryoreilly on 05/03/16.
 */
public class EyeProductCard {
    MySql db;

    public EyeProductCard(MySql db) {
        this.db = db;
    }

    public ProductCard createCard(UserPackage user, String occassion) {
        List<Product> products = new ArrayList<>();
        if (occassion.equals("Everyday")) {
            products.add(getShadowDay(user));
        } else if (occassion.equals("Night")) {
            products.addAll(getShadowNight(user));
            products.add(getLiner(products.get(products.size()-1)));
        }

        products.add(getMascara(occassion));
        products.add(getPrimer(user.getSkinColour()));

        return new ProductCard(products, "Eye", R.drawable.glameyes_look, true);
    }

    public List<Product> getShadowNight(UserPackage user) {
        List<Product> products = db.getProductsFromType("Eye Shadow");

        // Remove eye shadow colours which do not match eyes and hair
        for (int i = products.size() - 1; i >= 0; i--) {
            Product product = products.get(i);
            // remove unmatched eye color and eye shadow
            if ((user.getEye().equals("Blue") || user.getEye().equals("Green"))
                    && product.getHue()>75 && product.getHue()<290) {
                products.remove(i);
            }

            // remove unmatched hair color and eye shadow
            if (user.getHair().equals("Blonde") && product.getHue()>55
                    && product.getHue()<345 && product.getSaturation()>0.15) {
                products.remove(i);
            } else if (user.getHair().equals("Red") && product.getHue()>55
                    && product.getHue()<345 && product.getSaturation()>0.15
                    && product.getSaturation()<0.60) {
                products.remove(i);
            }
        }

        // get a base hue colour to select the shadows from
        Random rand = new Random();
        float baseHue = products.get(rand.nextInt(products.size())).getHue();
        float lowHue = baseHue-10;
        float highHue = baseHue+10;
        for (int i = products.size() - 1; i >= 0; i--) {
            Product product = products.get(i);
            if (product.getHue()<lowHue && product.getSaturation()>0.15) {
                products.remove(i);
            } else if (product.getHue()>highHue && product.getSaturation()>0.15) {
                products.remove(i);
            }
        }


        // sort shadows by brightness
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return Double.compare(lhs.getBrightness(), rhs.getBrightness());
            }
        });

        List<Product> shadows = new ArrayList<>();
        shadows.add(products.get(rand.nextInt(products.size()/4)));
        shadows.add(products.get(rand.nextInt((products.size()/4)+products.size()/4)));
        shadows.add(products.get(rand.nextInt((products.size()/4)+(2*products.size())/4)));
        shadows.add(products.get(rand.nextInt((products.size()/4)+(3*products.size())/4)));

        return shadows;
    }

    // Gets a eyeshadow for everyday use by selecting the product a little darker
    // than the user's skin
    public Product getShadowDay(UserPackage user) {
        float hue = user.getSkinColour()[0];
        float saturation = user.getSkinColour()[1] * 1.3f;
        float brightness = user.getSkinColour()[2];

        List<Product> products = db.getProductsFromType("Eye Shadow");
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

    // Gets an eye liner based on the eye shadow picked
    public Product getLiner(Product baseShadow) {
        float hue = baseShadow.getHue();
        float saturation = baseShadow.getSaturation();
        float brightness = baseShadow.getBrightness();
        List<Product> products = db.getProductsFromType("Eye Liner");
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

    // Returns the mascara suited for a user
    public Product getMascara(String occassion) {
        List<Product> products = db.getProductsFromType("Mascara");

        // remove the products that are only suited during the night
        for (int i = products.size() - 1; i >= 0; i--) {
            Product product = products.get(i);
            if (occassion.equals("Everyday") & product.getName().contains("Naughty")) {
                products.remove(i);
            }
        }

        return products.get(new Random().nextInt(products.size()));
    }


    // Gets the primer which has the closest colour to the user's skin
    public Product getPrimer(float[] hsv) {
        List<Product> products = db.getProductsFromType("Eye Primer");

        float hue = hsv[0];
        float saturation = hsv[1];
        float brightness = hsv[2];
        final Product tempSkin = new Product(hue, saturation, brightness);

        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return Double.compare(
                        eucDisHsv(lhs.getHsv(), tempSkin.getHsv()),
                        eucDisHsv(rhs.getHsv(), tempSkin.getHsv()));
            }

            private float eucDisHsv(float[] q, float[] p) {
                return (float) (Math.sqrt(
                        Math.pow((q[0] - p[0]) / 360, 2)
                                + Math.pow(q[1] - p[1], 2)
                                + Math.pow(q[2] - p[2], 2)));
            }
        });

        return products.get(0);
    }
}
