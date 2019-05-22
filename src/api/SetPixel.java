package api;

public class SetPixel {
    public static void setPixel(MyImage img, int width, int height, int[] output) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = output[x + y * width];
                img.setPixel(x, y, 255, v, v, v);
            }
        }
    }
}
