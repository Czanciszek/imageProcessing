package application;

import api.FilePath;
import api.MyImage;
import api.Transformation.Closing;
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

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final Logger logger = Logger.getLogger(Main.class.getName());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        final ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(
                "Closing Image",
                "Standard Deviation Filter"
        );
        comboBox.setPromptText("Choose...");

        Label maskSizeLabel = new Label("Mask size: ");
        TextField maskSizeField = new TextField();

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
                outputImageView.setImage(newImage);

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

            String method = comboBox.getValue().toString();
            if ( method.equals("Standard Deviation Filter") ){
                maskSizeLabel.setVisible(true);
                maskSizeField.setVisible(true);
            }
            else{
                maskSizeLabel.setVisible(false);
                maskSizeField.setVisible(false);
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
                    Closing closing2 = new Closing();
                    closing2.monoImage(myImage);
                    myImage.writeImage(resultPath);
                    break;
                case "Standard Deviation Filter":
                    int maskSize = Integer.parseInt(maskSizeField.getText());
                    StandardDeviation bin2 = new StandardDeviation();

                    bin2.stdDeviation(myImage, maskSize, 0);
                    myImage.writeImage(resultPath);

                    if (flag == 1) {
                        myImage.writeImage(resultPathRed);

                        bin2.stdDeviation(myImage, maskSize, 1);
                        myImage.writeImage(resultPathGreen);

                        bin2.stdDeviation(myImage, maskSize, 2);
                        myImage.writeImage(resultPathBlue);
                    }
                    break;
            }

            try {
                File file2 = new File(resultPath);

                FileInputStream input2 = new FileInputStream(file2);
                Image image2 = new Image(input2);
                outputImageView.setImage(image2);
                outputImageView.setFitWidth(500);
                outputImageView.setFitHeight(500);

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

        Scene scene = new Scene(new Group(), 1070, 650);

        grid.setVgap(4);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(8, 8, 8, 8));

        grid.add(new Label("Filtration: "), 0, 0);
        grid.add(comboBox, 1, 0);

        grid.add(setMethodButton, 2, 0);

        grid.add(maskSizeLabel, 3, 0);
        maskSizeLabel.setVisible(false);
        grid.add(maskSizeField, 4, 0);
        maskSizeField.setVisible(false);
        grid.setHalignment(maskSizeField, HPos.CENTER);

        grid.add(loadFilterButton, 7, 0);

        grid.add(vBox, 0, 2, 8, 1);

        grid.add(loadImageButton, 0, 3, 8, 1);
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