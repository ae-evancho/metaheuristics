import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class InterfaceParticle {

    public static GridPane createRegistrationFormPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints columnOneConstraints = new ColumnConstraints();
        columnOneConstraints.setHalignment(HPos.RIGHT);
        gridPane.getColumnConstraints().addAll(columnOneConstraints);

        return gridPane;
    }
    public static void addUIControls(GridPane gridPane) {
        Label label= new Label("PopulSize");
        Label label2= new Label("25");
        Slider slider= new Slider(1,100,25);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setBlockIncrement(1);
        slider.setMaxHeight(300);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(newValue.intValue());
            label2.setText(""+newValue.intValue());
        });
        VBox root= new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(label,slider,label2);
        root.setAlignment(Pos.CENTER);


        ///////////////////////////////

        Label label3= new Label("MaxIter");
        Label label4= new Label("200");
        Slider slider2= new Slider(0,500,200);
        slider2.setOrientation(Orientation.VERTICAL);
        slider2.setBlockIncrement(1);
        slider2.setMaxHeight(300);
        slider2.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider2.setValue(newValue.intValue());
            label4.setText(""+newValue.intValue());
        });
        VBox root2= new VBox();
        root2.setSpacing(10);
        root2.getChildren().addAll(label3,slider2,label4);
        root2.setAlignment(Pos.CENTER);


        //////////////////////////////

        Label label5= new Label("W");
        Label label6= new Label("0.3");
        Slider slider3= new Slider(0,1,0.3);
        slider3.setOrientation(Orientation.VERTICAL);
        slider3.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider3.setValue(Math.floor(newValue.doubleValue()*10)/10);
            label6.setText(""+Math.floor(newValue.doubleValue()*10)/10);
        });
        VBox root3= new VBox();
        root3.setSpacing(10);
        root3.getChildren().addAll(label5,slider3,label6);
        root3.setAlignment(Pos.CENTER);


        //////////////////////////////////////////

        Label label7= new Label("VitesseMax");
        Label label8= new Label("50");
        Slider slider4= new Slider(0,75,50);
        slider4.setOrientation(Orientation.VERTICAL);
        slider4.setBlockIncrement(1);
        slider4.setMaxHeight(300);
        slider4.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider4.setValue(newValue.intValue());
            label8.setText(""+newValue.intValue());
        });
        VBox root4= new VBox();
        root4.setSpacing(10);
        root4.getChildren().addAll(label7,slider4,label8);
        root4.setAlignment(Pos.CENTER);


        //////////////////////////////////////////

        Label label9= new Label("C1");
        Label label10= new Label("2");
        Slider slider5= new Slider(0,10,2);
        slider5.setOrientation(Orientation.VERTICAL);
        slider5.setMaxHeight(300);
        slider5.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider5.setValue(Math.floor(newValue.doubleValue()*10)/10);
            label10.setText(""+Math.floor(newValue.doubleValue()*10)/10);
        });
        VBox root5= new VBox();
        root5.setSpacing(10);
        root5.getChildren().addAll(label9,slider5,label10);
        root5.setAlignment(Pos.CENTER);


        ////////////////////////////////////////

        Label label11= new Label("C2");
        Label label12= new Label("2");
        Slider slider6= new Slider(0,10,2);
        slider6.setOrientation(Orientation.VERTICAL);
        slider6.setMaxHeight(300);
        slider6.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider6.setValue(Math.floor(newValue.doubleValue()*10)/10);
            label12.setText(""+Math.floor(newValue.doubleValue()*10)/10);
        });
        VBox root6= new VBox();
        root6.setSpacing(10);
        root6.getChildren().addAll(label11,slider6,label12);
        root6.setAlignment(Pos.CENTER);

        HBox hboxs= new HBox();
        hboxs.setSpacing(10);
        hboxs.getChildren().addAll(root,root2,root3,root4,root5,root6);

        ////////////////////////////////////////

        Stage stage= new Stage();

        FileChooser fil_chooser = new FileChooser();
        Button buttonf = new Button("Select file");
        Label labelf = new Label("no files selected");
        Label labels=new Label();
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

        Button buttondefault= new Button("Set to Default");

        EventHandler<ActionEvent> eventDefault =
                e -> {
                    slider.setValue(25);
                    slider2.setValue(200);
                    slider3.setValue(0.3);
                    slider4.setValue(50);
                    slider5.setValue(2);
                    slider6.setValue(2);
                };

        buttondefault.setOnAction(eventDefault);

        VBox vboxf = new VBox(10, buttonf, labelf);
        vboxf.setAlignment(Pos.CENTER);

        HBox hboxf= new HBox(500, buttondefault, vboxf);
        hboxf.setPadding(new Insets(10,0,0,0));

        gridPane.add(hboxf, 0,1);

        //////////////////////////
        Label labelresult = new Label("Result : ");

        TextField fieldresult = new TextField();
        fieldresult.setPrefHeight(30);
        fieldresult.setPrefWidth(520);
        fieldresult.setMaxWidth(520);

        VBox vboxresult= new VBox(10,labelresult,fieldresult);
        vboxresult.setAlignment(Pos.TOP_LEFT);

        gridPane.add(vboxresult,0,2);

        //////////////////////////

        Label labeltime = new Label("Execution Time : ");

        TextField fieldtime = new TextField();
        fieldtime.setPrefHeight(30);
        fieldtime.setPrefWidth(120);
        fieldtime.setMaxWidth(120);

        Button buttonexe= new Button("Execute");
        buttonexe.setPrefHeight(35);
        buttonexe.setMaxHeight(35);

        VBox vboxtime= new VBox(10,labeltime,fieldtime);
        vboxtime.setAlignment(Pos.TOP_LEFT);

        HBox hboxtimeexe= new HBox(340, vboxtime, buttonexe);
        hboxtimeexe.setAlignment(Pos.BOTTOM_LEFT);

        gridPane.add(hboxtimeexe,0,3);

        /////////////////////////////////


        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(295,325,1);
        xAxis.setLabel("Iterations");
        xAxis.setAnimated(true);
        yAxis.setLabel("Satisfied Clauses");
        yAxis.setAnimated(true);

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Performance Chart");
        lineChart.setAnimated(false);

        XYChart.Series<Number, Number> series;
        series = new XYChart.Series<>();
        series.setName("Data Series");
        lineChart.getData().add(series);

        HBox col1= new HBox();
        col1.setSpacing(10);
        col1.getChildren().addAll(hboxs, lineChart);

        gridPane.add(col1,0,0);



        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Integer random = ThreadLocalRandom.current().nextInt(10);

            Platform.runLater(() -> {
                series.getData().add(
                        new XYChart.Data<>(PSOMain.iteration, PSOMain.fitness));

            });
        }, 0, 100, TimeUnit.MILLISECONDS);


        EventHandler<ActionEvent> eventExe =
                e -> {
                    series.getData().clear();
                    fieldresult.setText("");
                    fieldtime.setText("");

                    if(labels.getText()!="") {
                        Thread thread1 = new Thread(new PSOMain((int)slider.getValue(),slider3.getValue(),(int)slider4.getValue(), slider5.getValue(), slider6.getValue(),(int)slider2.getValue(),fieldresult,fieldtime,labels.getText()));
                        thread1.start();
                    }
                    else
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),"FileNotFoundException", "Please select a file and try again");

                };
        buttonexe.setOnAction(eventExe);
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
