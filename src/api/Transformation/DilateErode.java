package api.Transformation;

import api.MyImage;

import static api.SetMask.setMask;
import static api.SetPixel.setPixel;

public class DilateErode {

    public static void binaryImage(MyImage img, boolean dilate, int length, int deg) {

        int width = img.getImageWidth();
        int height = img.getImageHeight();
        int output[] = new int[width * height];

        int[][] mask = setMask(length, deg);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int minX = ( length%2==0 ) ? x - (length/2) - 1 : x - (length/2);
                int minY = ( length%2==0 ) ? y - (length/2) - 1 : y - (length/2);
                int maxX = x + (length / 2);
                int maxY = y + (length / 2);

                int max = 0;
                int min = 255;

                int tx = 0;
                for( int k=minX; k<=maxX; k++) {
                    int ty=0;
                    for( int j = minY; j<=maxY; j++ ) {
                        if( k>=0 && j>=0 && k<width && j<height ) {
                            if( mask[ty][tx] == 1 ) {
                                if (img.getRed(k, j) > max && dilate) {
                                    max = img.getRed(k,j);
                                }
                                else if(img.getRed(k,j) < min && !dilate) {
                                    min = img.getRed(k, j);
                                }
                            }
                        }
                        ty++;
                    }
                    tx++;
                }
                output[x+y*width] = (dilate) ? max : min;
            }
        }

        setPixel(img, width, height, output);

    }

}