package api;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

public class MyImage {
    private BufferedImage image;
    private int width, height;
    private int pixels[];

    private int totalPixels;

    private enum ImageType {
        JPG, PNG
    }

    private ImageType imgType;


    public MyImage() {
    }

    public MyImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.totalPixels = this.width * this.height;
        this.pixels = new int[this.totalPixels];
        image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        this.imgType = ImageType.PNG;
        initPixelArray();
    }

    public void readImage(String filePath) {
        try {
            File f = new File(filePath);
            image = ImageIO.read(f);
            String fileType = filePath.substring(filePath.lastIndexOf('.') + 1);
            if ("jpg".equals(fileType)) {
                imgType = ImageType.JPG;
            } else {
                imgType = ImageType.PNG;
            }
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.totalPixels = this.width * this.height;
            this.pixels = new int[this.totalPixels];
            initPixelArray();
        } catch (IOException e) {
            System.out.println("Error Occurred!\n" + e);
        }
    }

    public void writeImage(String filePath) {
        try {
            File f = new File(filePath);
            String fileType = filePath.substring(filePath.lastIndexOf('.') + 1);
            ImageIO.write(image, fileType, f);
        } catch (IOException e) {
            System.out.println("Error Occurred!\n" + e);
        }
    }

    private void initPixelArray() {
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.out.println("Error Occurred: " + e);
        }
    }

    public int getImageWidth() {
        return width;
    }

    public int getImageHeight() {
        return height;
    }

    public int getAlpha(int x, int y) {
        return (pixels[x + (y * width)] >> 24) & 0xFF;
    }

    public int getRed(int x, int y) {
        return (pixels[x + (y * width)] >> 16) & 0xFF;
    }

    public int getGreen(int x, int y) {
        return (pixels[x + (y * width)] >> 8) & 0xFF;
    }

    public int getBlue(int x, int y) {
        return pixels[x + (y * width)] & 0xFF;
    }

    public void setPixel(int x, int y, int alpha, int red, int green, int blue) {
        pixels[x + (y * width)] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        updateImagePixelAt(x, y);
    }

    private void updateImagePixelAt(int x, int y) {
        image.setRGB(x, y, pixels[x + (y * width)]);
    }

}