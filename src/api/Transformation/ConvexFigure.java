package api.Transformation;

import api.MyImage;

public class ConvexFigure {

    public static void convexFigure(MyImage img) {

        int mask[][] = {{1,1,0},{1, -1, 0}, {1, 0, -1}};

        MyImage tmp = new MyImage(img.getImageWidth(), img.getImageHeight());

        boolean imageEqual = false;

        while(!imageEqual) {

            for(int i=0; i<img.getImageWidth(); i++)
                for(int j=0; j<img.getImageHeight(); j++ )
                    tmp.setPixel(i, j, img.getAlpha(i,j), img.getRed(i,j), img.getGreen(i,j), img.getBlue(i,j));

            imageEqual = true;

            for( int i = 0; i<8; i++ ) {
                bwhitmiss(img, mask);
                mask = rotateMask(mask);
            }

            for(int i=0; i<img.getImageWidth(); i++)
                for(int j=0; j<img.getImageHeight(); j++ )
                    if( tmp.getRed(i,j) != img.getRed(i,j) )
                        imageEqual = false;

        }
    }

    private static void bwhitmiss(MyImage img, int[][] mask) {
        int width = img.getImageWidth();
        int height = img.getImageHeight();

        for (int y = 1; y < height-1; y++) {
            for (int x = 1; x < width-1; x++) {
                boolean flag = true;
                for (int k = x - 1, tx=0; k <= x + 1; k++, tx++) {
                    for (int j = y - 1, ty=0; j <= y + 1; j++, ty++) {
                        if (k >= 0 && j >= 0 && k < width && j < height) {
                            if (mask[tx][ty] == 0)
                                ;
                            else if (mask[tx][ty] == -1 && img.getRed(k, j) == 0)
                                ;
                            else if (mask[tx][ty] == 1 && img.getRed(k, j) == 255)
                                ;
                            else
                                flag = false;
                        }
                    }
                }
                if (flag)
                    img.setPixel(x, y, 255, 255, 255, 255);
            }
        }
    }

    static int[][] rotateMask(int[][] mask) {

        int[][] rotatedMask = new int[3][3];

        rotatedMask[0][1] = mask[0][0];
        rotatedMask[0][2] = mask[0][1];
        rotatedMask[1][2] = mask[0][2];

        rotatedMask[2][2] = mask[1][2];
        rotatedMask[2][1] = mask[2][2];
        rotatedMask[2][0] = mask[2][1];

        rotatedMask[1][0] = mask[2][0];
        rotatedMask[0][0] = mask[1][0];
        rotatedMask[1][1] = mask[1][1];

        return rotatedMask;
    }
}
