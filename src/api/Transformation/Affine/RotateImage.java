package api.Transformation.Affine;

import api.MyImage;

import static java.lang.Math.*;

public class RotateImage {

    public static MyImage rotate(MyImage img, int angle) {

        int width = img.getImageWidth();
        int height = img.getImageHeight();

        int newSize = 2*(int)sqrt(width*width+height*height);

        double radian = angle*(PI/180);
        double sinus = sin(radian);
        double cosinus = cos(radian);

        MyImage rotatedImage = new MyImage( newSize, newSize );
        for (int i = 0; i < newSize; i++)
            for (int j = 0; j < newSize; j++)
                rotatedImage.setPixel(i, j, 255,0,0,0);


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int newX = (int)((cosinus*i)-(sinus*j));
                int newY = (int)((sinus*i)+(cosinus*j));

                int alpha = img.getAlpha(i,j);
                int red = img.getRed(i,j);
                int green = img.getGreen(i,j);
                int blue = img.getBlue(i,j);

                rotatedImage.setPixel(newX+(newSize/2), newY+(newSize/2), alpha, red, green, blue);
            }
        }

        for (int i = 0; i < newSize-1; i++) {
            for (int j = 0; j < newSize-1; j++) {
                if ( rotatedImage.getRed(i,j) == 0 && rotatedImage.getGreen(i,j) == 0 && rotatedImage.getBlue(i,j) == 0)
                    rotatedImage.setPixel(i, j, 255, rotatedImage.getRed(i+1,j),rotatedImage.getGreen(i+1,j), rotatedImage.getBlue(i+1,j));
            }
        }

        return rotatedImage;
    }
}