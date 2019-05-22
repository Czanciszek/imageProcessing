package api.Transformation;

import api.MyImage;

import static api.SetPixel.setPixel;

public class StandardDeviation {
    public static void stdDeviation(MyImage img, int maskSize, int data){
        int width = img.getImageWidth();
        int height = img.getImageHeight();

        int output[] = new int[width * height];

        int min = 255;
        int max = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int minX = x-(maskSize/2);
                int minY = y-(maskSize/2);
                int maxX = x+(maskSize/2);
                int maxY = y+(maskSize/2);

                float avg = 0.0f;
                for( int k=minX; k<=maxX; k++) {
                    for( int j = minY; j<=maxY; j++ ) {
                        if( k>=0 && j>=0 && k<width && j<height ) {
                            if( data == 0 )
                                avg += img.getRed(k,j);
                            else if( data == 1 )
                                avg += img.getGreen(k,j);
                            else if ( data == 2 )
                                avg += img.getBlue(k,j);
                        }
                    }
                }
                avg /= (maskSize*maskSize);

                float tmp;
                float sum = 0.0f;
                for( int k=minX; k<=maxX; k++) {
                    for( int j = minY; j<=maxY; j++ ) {
                        if( k>=0 && j>=0 && k<width && j<height ){
                            if( data == 0 )
                                tmp = (img.getRed(k,j) - avg );
                            else if( data == 1 )
                                tmp = (img.getGreen(k,j) - avg );
                            else
                                tmp = (img.getBlue(k,j) - avg );
                            sum += (tmp*tmp);
                        }
                    }
                }
                int val = (int) Math.sqrt( sum/(maskSize*maskSize-1) );

                if (val < min )
                    min = val;

                if( val > max )
                    max = val;


                output[x + y*width] = val;
            }
        }

        //Normalizacja
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                output[x + y*width ] = ( (-255/(min-max)) * (output[x + y*width ] - min ) );
            }
        }

        setPixel(img, width, height, output);

    }

}