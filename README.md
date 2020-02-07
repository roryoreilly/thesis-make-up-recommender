# Make-Up Recommendation App
This repo contains the report and source code used to create my android make-up application. The application works by taking a photo of the user, analysing the skin tone, hair colour, eye colour, and the face shape using a machine learning model. With these features a set of makeup is recommended to the user that would most suit them.

## Repo Structure
The repo is broken down in the report that was submitted for thesis completion and the source code which was used as the basis of the project

## Snippet of the Thesis

### 4.1 Software Overview
![software overview](https://github.com/roryoreilly/thesis-make-up-recommender/blob/master/img/app-process.png)

As the app is turned on a database of the makeup products is created. There are 800 products, in total, in the app. Each of these are taken from the Mac range and contain different information regarding each product.

The app begins by getting an image of the user. This image is acquired as mentioned in the previous section. The image is passed to the next activity in the app.

The image is set to the correct format and sent to the Face++ servers. Face++ analyses the image and extract data on the face. This data includes the 83 landmark points on the face.

The result is returned from Face++ and the data, as well as the user image, is used to extract attributes for each of the features. The colour features, skin complexion, eyes, hair, and lips, will get a HSB value as it’s attribute. There will be six different attributes extracted for the face shape.

The features are classified using the attributes for each. They are classified using either Weka, in the case of eye colour and face shape, or a nearest neighbour algorithm in the case of skin complexion and hair colour. The user is able to reselect the correct classification in the cases where the app is wrong.

The occasion is selected by the user after the feature classification. There are three occasions, Night out, daytime, and natural. The occasion selection as well as the feature classes are passed to the next activity in the app, where the makeup is recommended.

Finally, the makeup is recommended and presented to the user. Depending on the occasion there are 5 to 15 products recommended for use. Each of the product recommendations are made individually so that they will be suited for the user and are complementary to each other.
