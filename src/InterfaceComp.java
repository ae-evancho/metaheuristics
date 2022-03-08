import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class InterfaceComp {
    public static GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        ColumnConstraints columnOneConstraints = new ColumnConstraints();
        columnOneConstraints.setHalignment(HPos.RIGHT);


        gridPane.getColumnConstraints().addAll(columnOneConstraints);

        return gridPane;
    }
    public static void addUIControls(GridPane gridPane) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(295,325,1);
        xAxis.setLabel("Iterations");
        xAxis.setAnimated(true);
        yAxis.setLabel("Satisfied Clauses");
        yAxis.setAnimated(true);

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Performance Comparison");
        lineChart.setAnimated(false);


        XYChart.Series<Number, Number> series1;
        series1 = new XYChart.Series<>();
        series1.setName("BSO");
        lineChart.getData().add(series1);

        XYChart.Series<Number, Number> series2;
        series2 = new XYChart.Series<>();
        series2.setName("PSO");
        lineChart.getData().add(series2);

        XYChart.Series<Number, Number> series3;
        series3 = new XYChart.Series<>();
        series3.setName("Genetic Algorithm");
        lineChart.getData().add(series3);

        gridPane.add(lineChart,0,0);

        Button button= new Button("Start Comparison");
        FileChooser fil_chooser = new FileChooser();
        Button buttonf = new Button("Select file");
        Label labelf = new Label("no files selected");
        Label labels=new Label("");
        Stage stage= new Stage();
        EventHandler<ActionEvent> event =
                e -> {

                    File file = fil_chooser.showOpenDialog(stage);
                    if (file != null) {

                        labelf.setText(file.getName()
                                + "  selected");
                        labels.setText(file.getAbsolutePath());
                    }
                };

        buttonf.setOnAction(event);

        VBox vboxf = new VBox(10, buttonf, labelf);
        vboxf.setAlignment(Pos.CENTER);

        HBox hbox= new HBox(500,vboxf,button);

        gridPane.add(hbox,0,1);


        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Integer random = ThreadLocalRandom.current().nextInt(10);

            Platform.runLater(() -> {
                series1.getData().add(
                        new XYChart.Data<>(BSO.iteration, BSO.fitness));
                series2.getData().add(
                        new XYChart.Data<>(PSO.iteration, PSO.fitness));
                series3.getData().add(
                        new XYChart.Data<>(GA.iteration, GA.fitness));

            });
        }, 0, 100, TimeUnit.MILLISECONDS);

        EventHandler<ActionEvent> eventExe =
                e -> {
                    series1.getData().clear();
                    series2.getData().clear();
                    series3.getData().clear();
                    lineChart.getData().clear();
                    lineChart.getData().add(series1);
                    lineChart.getData().add(series2);
                    lineChart.getData().add(series3);


                    if(labels.getText()!="") {
                Thread thread1 = new Thread(new BSO(30,200,5,3,45,labels.getText()));
                Thread thread2 = new Thread(new PSO(25,0.3,45,2,2,200,labels.getText()));
                Thread thread3 = new Thread(new GA(100,0.85,0.45,9000,labels.getText()));
                thread1.start();
                thread2.start();
                thread3.start();
            }
            else
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),"FileNotFoundException", "Please select a file and try again");
                };
        button.setOnAction(eventExe);

    }
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}
