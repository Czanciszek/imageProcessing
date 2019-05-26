package api.Transformation;

import api.MyImage;

public class Closing {

    public void closeImage(MyImage img, int length, int angle) {
        DilateErode.binaryImage(img, true, length, angle);
        DilateErode.binaryImage(img, false, length, angle);
    }

}