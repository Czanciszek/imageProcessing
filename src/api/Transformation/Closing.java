package api.Transformation;

import api.MyImage;

public class Closing {

    public void monoImage(MyImage img) {
        DilateErode.binaryImage(img, true, 5, 135);
        DilateErode.binaryImage(img, false, 5, 135);
    }

}