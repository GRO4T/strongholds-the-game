package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.lang.*;
import java.util.ArrayList;

public class Main extends Application {

    final int windowX = 800;
    final int windowY = 600;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // I don't think I will be using fxml
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Line line = new Line();
        line.setStartX(100.0);
        line.setStartY(150.0);
        line.setEndX(200.0);
        line.setEndY(150.0);

        Text text = new Text();
        text.setFont(new Font(45));
        text.setX(0);
        text.setY(100);
        text.setText("Hello ŁORLD!");

        Rectangle rectangle = new Rectangle(0, windowY - 20, windowX, 40);

        Circle circle = new Circle(windowX / 2, windowY / 2, 20);
/*
        ArrayList<Node> childrenList = new ArrayList<Node>();
        childrenList.add(line);
        childrenList.add(text);
*/
        Group root = new Group();
        Group shapes = new Group

        shapesList.add(line);
        shapesList.add(rectangle);
        shapesList.add(circle);
        ObservableList list = root.getChildren();
        list.add(text);
        list.add(shapesList);

        Scene scene = new Scene(root, windowX, windowY);
        scene.setFill(Color.BROWN);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);


        final long startNanoTime = System.nanoTime();
        new AnimationTimer(){
           public void handle(long currentNanoTime){
              double t = (currentNanoTime - startNanoTime) / 1000000000.0;
           }
        }.start();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);


    }
}
