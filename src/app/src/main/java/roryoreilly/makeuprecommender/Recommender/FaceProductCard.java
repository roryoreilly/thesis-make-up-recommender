package roryoreilly.makeuprecommender.Recommender;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import roryoreilly.makeuprecommender.Database.MySql;
import roryoreilly.makeuprecommender.Database.Product;
import roryoreilly.makeuprecommender.Helper.UserPackage;
import roryoreilly.makeuprecommender.R;

public class FaceProductCard {
    MySql db;

    public FaceProductCard(MySql db) {
        this.db = db;
    }

    public ProductCard createCard(UserPackage user, String occassion) {
        List<Product> products = new ArrayList<>();
        products.add(getFoundation(user.getSkinColour()));
        products.add(getConcealer(products.get(0)));
        products.add(getBronzer(user));
        products.add(getHighlighter(user));
        products.add(getBlush(user, occassion));

        return new ProductCard(products, "Face", R.drawable.goingout_look, true);
    }

    public Product getFoundation(float[] hsv) {
        float hue = hsv[0];
        float saturation = hsv[1];
        float brightness = hsv[2];
        final Product tempSkin = new Product(hue, saturation, brightness);
        List<Product> products = db.getProductsFromType("Foundation");

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



    // Attempts to chose concealer in the same warmth of the foundation
    // but one tone less if applicable
    public Product getConcealer(Product foundation) {
        List<Product> products = db.getProductsFromType("Concealer");
        String warmth;
        if (foundation.getName().contains("NC")) {
            warmth = "NC";
        } else if (foundation.getName().contains("NW")) {
            warmth = "NW";
        }

        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return Double.compare(
                        Integer.valueOf(lhs.getName().split(" ")[4]),
                        Integer.valueOf(rhs.getName().split(" ")[4]));
            }
        });

        for (int i = products.size() - 1; i >= 0; i--) {
            if (foundation.getName().contains("NW") && products.get(i).getName().contains("NC")) {
                products.remove(i);
            } else if (foundation.getName().contains("NC") && products.get(i).getName().contains("NW")){
                products.remove(i);
            }
        }

        Product bestProduct = products.get(0);
        for (int i = products.size() - 1; i >= 0; i--) {
            Product product = products.get(i);
            if (foundation.getBrightness() < product.getBrightness()) {
                double eucDis = Math.sqrt(
                        Math.pow((foundation.getHue()-product.getHue())/0.9, 2)
                        + Math.pow((foundation.getBrightness()-product.getBrightness())*100, 2)
                        + Math.pow((foundation.getSaturation()-product.getSaturation())*100, 2));

                double eucDisBest = Math.sqrt(
                        Math.pow((foundation.getHue()-bestProduct.getHue())/2.4, 2)
                                + Math.pow((foundation.getBrightness()-bestProduct.getBrightness())*100, 2)
                                + Math.pow((foundation.getSaturation()-bestProduct.getSaturation())*100, 2));

                if (eucDis < eucDisBest) {
                    bestProduct = product;
                }
            }
        }

        return bestProduct;
    }

    //Returns bronzer chosen by the skin warmth and the tone
    public Product getBronzer(UserPackage user) {
        List<Product> products = db.getProductsFromType("Bronzer");

        int value = Integer.valueOf(user.getSkinCode().split("\\s")[1]);
        for (int i = products.size() - 1; i >= 0; i--) {
            Product product = products.get(i);
            if (user.isWarm()) {
                if (product.getName().contains("Refined Golden")) {
                    if (value <= 25) {
                        return product;
                    }
                } else if (product.getName().contains("Golden")) {
                    if (value > 25) {
                        return product;
                    }
                }
            } else if (!user.isWarm()) {
                if (product.getName().contains("Matte Bronze")) {
                    if (value > 25) {
                        return product;
                    }
                } else if (product.getName().contains("Bronze")) {
                    if (value <= 25) {
                        return product;
                    }
                }
            }
        }
        return products.get(0);
    }

    // Get a highlighter using the user's skin
    public Product getHighlighter(UserPackage user) {
        float hue = user.getSkinColour()[0];
        float saturation = user.getSkinColour()[1];
        float brightness = user.getSkinColour()[2];

        List<Product> products = db.getProductsFromType("Highlighter");
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



    public Product getBlush(UserPackage up, String occassion) {
        return getBlush("Fair", "Warm", "Blue", "Day");
    }
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
