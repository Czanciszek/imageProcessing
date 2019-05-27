package application;

import api.FilePath;
import api.MyImage;
import api.Transformation.Closing;
import api.Transformation.ConvexFigure;
import api.Transformation.ScaleImage;
import api.Transformation.StandardDeviation;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static api.Transformation.RotateImage.rotate;
import static api.Transformation.ScaleImage.scale;
import static com.sun.javafx.iio.common.ImageTools.scaleImage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final Logger logger = Logger.getLogger(Main.class.getName());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(
                "Closing Image",
                "Standard Deviation Filter",
                "Convex Figure",
                "Scale Image",
                "Rotate Image"
        );
        comboBox.setPromptText("Choose...");

        Label param1Label = new Label("Param1 size: ");
        TextField param1Field = new TextField();

        Label param2Label = new Label("Param2 size: ");
        TextField param2Field = new TextField();

        final ComboBox angleComboBox = new ComboBox();
        angleComboBox.getItems().addAll(
                0, 45, 90, 135
        );

        Image image = new Image("http://mikecann.co.uk/wp-content/uploads/2009/12/javafx_logo_color_1.jpg");

        ImageView inputImageView = new ImageView();
        inputImageView.setImage(image);
        inputImageView.setFitHeight(500);
        inputImageView.setFitWidth(500);

        ImageView outputImageView = new ImageView();
        outputImageView.setImage(image);
        outputImageView.setFitWidth(500);
        outputImageView.setFitHeight(500);

        Button loadImageButton = new Button();
        loadImageButton.setText("Load Image");
        loadImageButton.setOnAction((event) -> {
            File file = fileChooser.showOpenDialog(stage);
            try {

                FileInputStream input = new FileInputStream(file.getAbsolutePath());
                FilePath filePath = new FilePath( file.getAbsolutePath() );
                System.out.println(filePath.path);

                Image newImage = new Image(input);

                inputImageView.setImage(newImage);
                double imageWidth = newImage.getWidth();
                double imageHeight = newImage.getHeight();
                inputImageView.setFitHeight(imageHeight);
                inputImageView.setFitWidth(imageWidth);

                outputImageView.setImage(newImage);
                outputImageView.setFitHeight(imageHeight);
                outputImageView.setFitWidth(imageWidth);

                MyImage myImage = new MyImage();
                myImage.readImage(file.getAbsolutePath());

            } catch (FileNotFoundException e) {
                logger.log(Level.SEVERE, "exception file not found", e);
            }
        });

        GridPane grid = new GridPane();

        Button setMethodButton = new Button();
        setMethodButton.setText("Set");
        setMethodButton.setOnAction((event) -> {

            param1Label.setVisible(false);
            param1Field.setVisible(false);
            param2Label.setVisible(false);
            param2Field.setVisible(false);
            angleComboBox.setVisible(false);

            String method = comboBox.getValue().toString();
            if ( method.equals("Standard Deviation Filter") ){
                param1Label.setText("Mask size");
                param1Label.setVisible(true);
                param1Field.setVisible(true);
            }
            else if( method.equals("Closing Image")) {
                param1Label.setText("Length");
                param2Label.setText("Angle");
                param1Field.setVisible(true);
                param1Label.setVisible(true);
                param2Label.setVisible(true);
                angleComboBox.setVisible(true);
            }
            else if( method.equals("Scale Image")) {
                param1Label.setText("Scale X");
                param2Label.setText("Scale Y");
                param1Label.setVisible(true);
                param2Label.setVisible(true);
                param1Field.setVisible(true);
                param2Field.setVisible(true);
            }
            else if( method.equals("Rotate Image")) {
                param1Label.setText("Rotate");
                param1Label.setVisible(true);
                param1Field.setVisible(true);
            }
        });

        Button loadFilterButton = new Button();
        loadFilterButton.setText("Process");
        loadFilterButton.setOnAction((event) -> {
            MyImage myImage = new MyImage();
            myImage.readImage(FilePath.path);

            int width = myImage.getImageWidth();
            int height = myImage.getImageHeight();

            int flag = 0; //0 -> Black&White // 1-> RGB
            for( int z = 0; z<width; z++) {
                for (int x = 0; x < height; x++) {
                    int red = myImage.getRed(z, x);
                    int green = myImage.getGreen(z, x);
                    int blue = myImage.getBlue(z, x);
                    if( red != green || green != blue ) {
                        flag = 1;
                        break;
                    }
                }
                if( flag == 1 )
                    break;
            }

            String method = comboBox.getValue().toString();

            String resultPath= "Result.png";
            String resultPathRed = "StandardDeviationRed.png";
            String resultPathGreen = "StandardDeviationGreen.png";
            String resultPathBlue = "StandardDeviationBlue.png";

            switch (method) {
                case "Closing Image":
                    int length = Integer.parseInt(param1Field.getText());
                    int angle = Integer.parseInt(angleComboBox.getValue().toString());

                    Closing closing2 = new Closing();
                    closing2.closeImage(myImage, length, angle);
                    myImage.writeImage(resultPath);
                    break;
                case "Standard Deviation Filter":
                    int size = Integer.parseInt(param1Field.getText());

                    StandardDeviation bin2 = new StandardDeviation();
                    bin2.stdDeviation(myImage, size, 0);
                    myImage.writeImage(resultPath);

                    if (flag == 1) {
                        myImage.writeImage(resultPathRed);

                        bin2.stdDeviation(myImage, size, 1);
                        myImage.writeImage(resultPathGreen);

                        bin2.stdDeviation(myImage, size, 2);
                        myImage.writeImage(resultPathBlue);
                    }
                    break;
                case "Convex Figure":
                    ConvexFigure convexFigure = new ConvexFigure();
                    convexFigure.convexFigure(myImage);
                    myImage.writeImage(resultPath);
                    break;
                case "Scale Image":
                    double scaleX = Double.parseDouble(param1Field.getText());
                    double scaleY = Double.parseDouble(param2Field.getText());
                    MyImage scaledImage = scale(myImage, scaleX, scaleY);
                    scaledImage.writeImage(resultPath);
                    break;
                case "Rotate Image":
                    int angle2 = Integer.parseInt(param1Field.getText());
                    MyImage rotatedImage = rotate(myImage, angle2);
                    rotatedImage.writeImage(resultPath);
                    break;
            }

            try {
                File file2 = new File(resultPath);

                FileInputStream input2 = new FileInputStream(file2);
                Image image2 = new Image(input2);
                outputImageView.setImage(image2);
                outputImageView.setFitWidth(image2.getWidth());
                outputImageView.setFitHeight(image2.getHeight());

                //Żeby dalsza filtracja przebiegała na prawym obrazku
                new FilePath(resultPath);
            }
            catch (FileNotFoundException e) {
                logger.log(Level.SEVERE, "exception file not found", e);
            }

        });

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(inputImageView, outputImageView);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15, 12, 15, 12));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(hBox, loadImageButton, setMethodButton, loadFilterButton, comboBox);

        Scene scene = new Scene(new Group(), 1200, 800);

        grid.setVgap(4);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(11, 11, 11, 11));

        grid.add(new Label("Filtration: "), 0, 0);
        grid.add(comboBox, 1, 0);

        grid.add(setMethodButton, 2, 0);

        grid.add(param1Label, 3, 0);
        param1Label.setVisible(false);
        grid.add(param1Field, 4, 0);
        param1Field.setVisible(false);
        grid.add(param2Label, 5, 0);
        param2Label.setVisible(false);
        grid.add(param2Field, 6, 0);
        param2Field.setVisible(false);
        grid.add(angleComboBox, 6, 0 );
        angleComboBox.setVisible(false);


        grid.setHalignment(param1Field, HPos.CENTER);

        grid.add(loadFilterButton, 7, 0);

        grid.add(vBox, 0, 2, 11, 1);

        grid.add(loadImageButton, 0, 3, 11, 1);
        grid.setHalignment(loadImageButton, HPos.CENTER);

        Group root = (Group)scene.getRoot();
        root.getChildren().add(grid);
        stage.setTitle("Image Processor");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String args[]) {
        launch(args);
    }
}