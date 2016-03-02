package roryoreilly.makeuprecommender.Recommender;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import roryoreilly.makeuprecommender.Database.MySql;
import roryoreilly.makeuprecommender.Database.Product;

/**
 * Created by roryoreilly on 01/03/16.
 */
public class FaceProductCard {
    MySql db;

    public FaceProductCard(MySql db) {
        this.db = db;
    }

    // TODO better way to select foundation
    public Product getFoundation(String code) {
        List<Product> products = db.getProductsFromType("Foundation");
        for(Product product: products) {
            if (product.getName().contains(code)) {
                return product;
            }
        }

        Log.d("Find Foundation", "Failed to find a foundation");
        return products.get(0);
    }

    // TODO choose method to select concealer
    public Product getConcealer(String skinCode) {
        List<Product> products = db.getProductsFromType("Concealer");
        String tone = skinCode.split(" ")[0];
        String value = skinCode.split(" ")[1];

        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return Double.compare(
                        Integer.valueOf(lhs.getName().split(" ")[4]),
                        Integer.valueOf(rhs.getName().split(" ")[4]));
            }
        });

        for (int i = products.size() - 1; i >= 0; i--) {
            if (products.get(i).getName().contains(tone)) {
                if (products.get(i).getName().contains(value) && i>0) {
                    return products.get(i-1);
                }
            } else {
                products.remove(i);
            }
        }
        return products.get(0);

//        Product bestProduct = products.get(0);
//        for(Product product: products) {
//            if (foundation.getBrightness() < product.getBrightness()) {
//                double eucDis = Math.sqrt(
//                        Math.pow((foundation.getHue()-product.getHue())/0.9, 2)
//                        + Math.pow((foundation.getBrightness()-product.getBrightness())*100, 2)
//                        + Math.pow((foundation.getSaturation()-product.getSaturation())*100, 2));
//
//                double eucDisBest = Math.sqrt(
//                        Math.pow((foundation.getHue()-bestProduct.getHue())/2.4, 2)
//                                + Math.pow((foundation.getBrightness()-bestProduct.getBrightness())*100, 2)
//                                + Math.pow((foundation.getSaturation()-bestProduct.getSaturation())*100, 2));
//
//                if (eucDis < eucDisBest) {
//                    bestProduct = product;
//                }
//            }
//        }

//        return bestProduct;
    }

    //TODO implement this
    public Product getBronzer() {
        List<Product> products = db.getProductsFromType("Bronzer");
        return products.get(0);
    }

    // TODO fix this to give more accurate results
    public Product getBlush(String skin, String undertone, String eye, String occasion) {
        List<Product> products = db.getProductsFromType("Blush");
        Product chosenProduct = products.get(0);

        int size=products.size();
        for (int i=0; i<size; i++) {
            Product product = products.get(i);
            Log.d("Blush", product.toString() + "\t"+String.valueOf(size));
            if (skin.equals("Fair")) {
                if (product.getSaturation() < 0.52 || product.getBrightness()<.70) {
                    products.remove(product);
                    i--; size--;
                    continue;
                } else if (undertone.equals("Warm") && product.getHue()>70) {
                    products.remove(product);
                    i--; size--;
                    continue;
                } else if (undertone.equals("Cool") && product.getHue()<70) {
                    products.remove(product);
                    i--; size--;
                    continue;
                }
            } else if (skin.equals("Medium")) {
                if (product.getBrightness() > .92) {
                    products.remove(product);
                    i--; size--;
                    continue;
                } else if (undertone.equals("Warm") && product.getHue()>70) {
                    products.remove(product);
                    i--; size--;
                    continue;
                } else if (undertone.equals("Cool") && product.getHue()<70) {
                    products.remove(product);
                    i--; size--;
                    continue;
                }
            } else if (skin.equals("Dark")) {
                if (undertone.equals("Warm") && (product.getBrightness()>.70 || product.getSaturation()>.52)) {
                    products.remove(product);
                    i--; size--;
                    continue;
                } else if (undertone.equals("Cool") && product.getBrightness()<.92) {
                    products.remove(product);
                    i--; size--;
                    continue;
                }
            }


            if (!skin.equals("Dark") && !undertone.equals("Warm")) {
                if (eye.equals("Blue")) {
                    if (product.getBrightness() < .70) {
                        products.remove(product);
                        i--; size--;
                        continue;
                    } else if (product.getBrightness()<90 && product.getSaturation()<.35) {
                        products.remove(product);
                        i--; size--;
                        continue;
                    }
                } else if (eye.equals("Green")) {
                    if (product.getBrightness() < .70) {
                        products.remove(product);
                        i--; size--;
                        continue;
                    }
                } else if (eye.equals("Brown")) {
                    if (product.getHue()>10 && product.getHue()<20 && product.getBrightness()<.90 && product.getSaturation()<55) {
                        products.remove(product);
                        i--; size--;
                        continue;
                    }
                }
            }
        }

        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return Double.compare(lhs.getBrightness(), rhs.getBrightness());
            }
        });


        for(Product p: products) {
            Log.d("Blush", p.toString());
        }
        Random rand = new Random();
        if (occasion.equals("Night")) {
            int index = rand.nextInt(products.size()/2);
            chosenProduct = products.get(index + ((products.size()-1)/2));
        } else if (occasion.equals("Day")) {
            int index = rand.nextInt(products.size()/2);
            chosenProduct = products.get(index);
        }

        return chosenProduct;
    }
}
