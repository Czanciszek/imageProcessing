package api.Transformation.Affine;

import api.MyImage;

public class ScaleImage {

    public static MyImage scale(MyImage img, double scaleX, double scaleY) {

        int width = (int)(img.getImageWidth() * scaleX);
        int height = (int)(img.getImageHeight() * scaleY);

        MyImage scaledImage = new MyImage(width, height);

        int x = ((img.getImageWidth() << 16) / width) + 1;
        int y = ((img.getImageHeight() << 16) / height) + 1;

        for (int i = 0; i < scaledImage.getImageWidth(); i++) {
            for (int j = 0; j < scaledImage.getImageHeight(); j++) {
                int xx = (i*x) >> 16;
                int yy = (j*y) >> 16;

                int red = img.getRed(xx,yy);
                int green = img.getGreen(xx,yy);
                int blue = img.getBlue(xx,yy);

                scaledImage.setPixel(i, j, 255, red, green, blue);

            }
        }
        return scaledImage;
    }


}
