package api.Transformation.Affine;

import api.MyImage;

import static java.lang.Math.*;

public class WarpImage {

    public static MyImage warp(MyImage img, int angle) {

        int width = img.getImageWidth();
        int height = img.getImageHeight();

        double radian = angle*(PI/180);
        double tangens = tan(radian);

        MyImage warpedImage = new MyImage(width, height );
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                warpedImage.setPixel(i, j, 255,0,0,0);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int newX = (int)(i+tangens*j);

                int alpha = img.getAlpha(i,j);
                int red = img.getRed(i,j);
                int green = img.getGreen(i,j);
                int blue = img.getBlue(i,j);

                if( newX < 0 || newX >= width )
                    newX = 0;

                warpedImage.setPixel(newX, j, alpha, red, green, blue);
            }
        }

        for (int i = 0; i <width-1; i++) {
            for (int j = 0; j < height-1; j++) {
                if ( warpedImage.getRed(i,j) == 0 && warpedImage.getGreen(i,j) == 0 && warpedImage.getBlue(i,j) == 0)
                    warpedImage.setPixel(i, j, 255, warpedImage.getRed(i+1,j),warpedImage.getGreen(i+1,j), warpedImage.getBlue(i+1,j));
            }
        }

        return warpedImage;
    }
}
