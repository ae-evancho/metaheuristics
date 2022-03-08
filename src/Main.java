import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Slider;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Metaheuristics Application");

        GridPane gridpaneBSO = InterfaceBee.createRegistrationFormPane();
        InterfaceBee.addUIControls(gridpaneBSO);
        GridPane gridpanePSO = InterfaceParticle.createRegistrationFormPane();
        InterfaceParticle.addUIControls(gridpanePSO);
        GridPane gridpaneGA = InterfaceGA.createRegistrationFormPane();
        InterfaceGA.addUIControls(gridpaneGA);
        GridPane gridpaneComp = InterfaceComp.createRegistrationFormPane();
        InterfaceComp.addUIControls(gridpaneComp);

        TabPane tabpane= new TabPane();

        Tab tabGA= new Tab("Genetic Algorithm");
        tabGA.setContent(gridpaneGA);
        tabpane.getTabs().add(tabGA);

        Tab tabPSO= new Tab("PSO");
        tabPSO.setContent(gridpanePSO);
        tabpane.getTabs().add(tabPSO);

        Tab tabBSO= new Tab("BSO");
        tabBSO.setContent(gridpaneBSO);
        tabpane.getTabs().add(tabBSO);

        Tab tabComp= new Tab("Comparative Study");
        tabComp.setContent(gridpaneComp);
        tabpane.getTabs().add(tabComp);


        Scene scene = new Scene(tabpane, 900, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}