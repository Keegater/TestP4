import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main extends Application {

    private Pane root = new Pane(); //Full screen (Board + GUI)
    private final int windowWidth = 1200;
    private final int windowHeight = 800;
    private double waitTime = 1.0;


    private String programTitle;
    private String humanInstructions;
    private int grow, gcol;
    List<List<String>> siloCommands = new ArrayList<>(); //List of Silo Commands in order. Used to build "siloList"
    List<String[]> inOutPutsRaw = new ArrayList<>(); //List of input/outputs. Starts with the name
    List<GridPane> siloGUIs = new ArrayList<>();
    List<HBox> portGUIs = new ArrayList<>();

    GridPane display = new GridPane(); //holds silos, ports, all that.


    Silo[] siloList;
    InOutPut[] inOutPuts;
    Port[] ports;




    private Parent createContent() throws FileNotFoundException {


        display.setAlignment(Pos.CENTER_RIGHT);
        display.setPadding(new Insets(10));

        //parse input and get needed info

        parseInputs(); //builds siloCommands, and inOutPuts
        initSilos(); //builds siloList
        buildSiloGUIs(); //builds siloGUIs

        buildInOutPuts();
        createAndAssignPorts();
        buildPortGuis();

        //pass this all to threadmanager and create an instance
        ThreadManager tm = new ThreadManager(siloList, inOutPuts, ports);





        root.setPrefSize(windowWidth, windowHeight);


        //testing
        tm.startThreads();


        root.getChildren().add(display);






        //root.setBackground(Background.fill(Color.BLACK));

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if( now - lastUpdate > waitTime) { //&& whatever condition you have
                    //call whatever ur "do shit" method is
                    lastUpdate = now;
                }

            }
        };
        timer.start();
        return root;
    }

    private void buildInOutPuts() {
        inOutPuts = new InOutPut[inOutPutsRaw.size()];
        for (int i = 0; i < inOutPutsRaw.size(); i++) {
            InOutPut put = new InOutPut(inOutPutsRaw.get(i));
            inOutPuts[i] = put;
        }

        //assign inputoutputs to the respective silos.
        for(InOutPut put : inOutPuts){
            String pos = "";
            int correctedRow = put.row;
            int correctedCol = put.col;
            if (put.row < 0){
                pos = "UP";
                correctedRow++;
            } else if (put.row > grow -1){
                pos = "DOWN";
                correctedRow--;
            } else if (put.col < 0){
                pos = "LEFT";
                correctedCol++;
            } else if (put.col > gcol -1){
                pos = "RIGHT";
                correctedCol--;
            }

            siloList[correctedRow * gcol + correctedCol].setPort(pos, put);
        }
    }


    //sets TITLE, HUMANINSTRUCTION, gets data for Silos and input/output. Should Initialize those next.
    private void parseInputs(){
        List<String> lines = new ArrayList<>();
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter Program Below. Type \"EXIT\" When Complete. ");

        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            if ("EXIT".equalsIgnoreCase(line)) {
                break;
            }
            lines.add(line);
        }
        scnr.close();

        int grid = 0;
        List<Integer> ends = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            switch (lines.get(i)){
                case "GRID":
                    grid = i;
                    break;
                case "END":
                    ends.add(i);
            }
        }
        programTitle = lines.get(0);
        humanInstructions = lines.get(1);
        for (int i = 2; i < grid; i++) {
            humanInstructions = humanInstructions + "\n" + lines.get(i);
        }
        grow = Integer.parseInt(lines.get(grid + 1).split(" ")[0]);
        gcol = Integer.parseInt(lines.get(grid + 1).split(" ")[1]);

        for (int i = 0; i < grow * gcol; i++) {
            List<String> siloCmds = new ArrayList<>();
            int start;

            if (i == 0){
                start = grid + 2;
            } else {
                start = ends.get(i - 1) + 1;
            }
            for (int j = start; j < ends.get(i) ; j++) {
                siloCmds.add(lines.get(j));
            }
            siloCommands.add(siloCmds);
        }

        for (int i = 0; i < ends.size() - (grow*gcol + 1); i++) {
            int start = ends.get(grow*gcol + (i - 1)) + 1;
            List<String> inoutCmds = new ArrayList<>();
            while (!lines.get(start).equals("END")){
                inoutCmds.add(lines.get(start));
                start++;
            }
            inOutPutsRaw.add(inoutCmds.toArray(new String[0]));
        }
    }


    private void initSilos(){
        siloList = new Silo[siloCommands.size()];
        for (int i = 0; i < siloCommands.size() ; i++) {
            Silo silo = new Silo(siloCommands.get(i));
            siloList[i] = silo;
        }
    }


    private void buildSiloGUIs() {

        int count = 0;
        for(Silo silo : siloList){
            Label accLabel = new Label();
            Label bakLabel = new Label();
            Label pcLabel = new Label();
            Font stdFont = new Font("Arial", 10);
            accLabel.setFont(stdFont);
            accLabel.setTextFill(Color.WHITE);
            bakLabel.setFont(stdFont);
            bakLabel.setTextFill(Color.WHITE);
            pcLabel.setFont(stdFont);
            pcLabel.setTextFill(Color.WHITE);
            //Gridpane that holds whole silo
            GridPane siloPane = new GridPane();


            VBox commandVbox = createVBoxFromList(silo.getProgram());


            siloPane.add(commandVbox, 0, 0);

            //vbox that holds registers
            List<VBox> regList = new ArrayList<>();
            regList.add(createLabelVbox("ACC", accLabel));
            regList.add(createLabelVbox("BAK", bakLabel));
            regList.add(createLabelVbox("PC", pcLabel));

            //test values
            accLabel.setText("1");
            bakLabel.setText("2");
            pcLabel.setText("3");

            VBox regVbox = new VBox();
            regVbox.setAlignment(Pos.CENTER);
            regList.forEach(vBox -> {
                vBox.setPadding(new Insets(0, 5, 0, 5));
                regVbox.getChildren().add(vBox);
            });

            siloPane.add(regVbox, 1, 0);
            regVbox.setAlignment(Pos.TOP_RIGHT);
            siloPane.setBorder(Border.stroke(Color.WHITE));
            siloPane.setHgap(10);

            siloGUIs.add(siloPane);
            siloPane.setPadding(new Insets(2));

            display.add(siloPane, 2 * (count % gcol) + 1, 2 * (count / gcol) + 1);

            //Placeholders, THis is where ports will go

            //display.add(new Label("EMPTY") , (count % grow) * 2 + 1 , (count / grow) * 2 + 2);

            count++;
        }

    }

    public void buildPortGuis(){

        int count = 0;
        for (Port port : ports){
            HBox portBox = new HBox();
            Path arrows = getArrows();

            if (count < grow * (gcol - 1)){
               arrows.setRotate(90);
            }
            Label portLabel  = new Label();
            portLabel.setFont(new Font("Arial", 10));
            portLabel.setTextFill(Color.WHITE);
            if (port.hasValue()){
                portLabel.setText(String.valueOf(port.getValue()));
            }

            portBox.getChildren().add(arrows);
            portBox.getChildren().add(portLabel);
            portGUIs.add(portBox);
            portBox.setAlignment(Pos.CENTER);
            portBox.setPadding(new Insets(4));
            count++;
        }

        for (int i = 0; i < grow * (gcol - 1); i++) {

            display.add(portGUIs.get(i), 2 * ((i + i/(gcol - 1)) % gcol) + 2,2 * ((i + i/(gcol - 1)) / gcol) + 1 );

        }

        for (int i = 0; i < gcol * (grow - 1); i++) {
            int siloindex = i + (grow * (gcol - 1));

            display.add(portGUIs.get(siloindex), (i%gcol) * 2 + 1, (i/gcol) * 2 + 2);

        }



    }

    public Path getArrows(){
        Path doubleArrow = new Path();

        double arrowLength = 20;
        double arrowWidth = 5;
        double spacing = 5; // Spacing between the two arrows

        // First arrow (upwards)
        MoveTo moveTo1 = new MoveTo(arrowWidth / 2, arrowLength);
        LineTo lineTo1 = new LineTo(arrowWidth / 2, 0);
        LineTo lineTo2 = new LineTo(arrowWidth, arrowLength / 4);
        LineTo lineTo3 = new LineTo(arrowWidth / 2, 0);
        LineTo lineTo4 = new LineTo(0, arrowLength / 4);

        // Second arrow (downwards)
        MoveTo moveTo2 = new MoveTo(arrowWidth + spacing + arrowWidth / 2, 0);
        LineTo lineTo5 = new LineTo(arrowWidth + spacing + arrowWidth / 2, arrowLength);
        LineTo lineTo6 = new LineTo(arrowWidth + spacing, arrowLength * 3 / 4);
        LineTo lineTo7 = new LineTo(arrowWidth + spacing + arrowWidth / 2, arrowLength);
        LineTo lineTo8 = new LineTo(arrowWidth * 2 + spacing, arrowLength * 3 / 4);

        doubleArrow.getElements().addAll(moveTo1, lineTo1, lineTo2, lineTo3, lineTo4, moveTo2, lineTo5, lineTo6, lineTo7, lineTo8);

        // Styling the double arrow
        doubleArrow.setStroke(Color.WHITE);
        doubleArrow.setStrokeWidth(2);
        doubleArrow.setStrokeLineJoin(StrokeLineJoin.ROUND);
        doubleArrow.setFill(Color.TRANSPARENT);

        return doubleArrow;
    }



    public void createAndAssignPorts(){
        List<Port> portList  = new ArrayList<>();

        for (int i = 0; i < grow * (gcol - 1); i++) {
            Port port = new Port();
            siloList[i + i/(gcol - 1)].setPort("RIGHT", port);//assign to silo on left: SILO <-> PORT
            siloList[i + i/(gcol - 1) + 1].setPort("LEFT", port);//assign to silo on right
            portList.add(port);
        }

        for (int i = 0; i < gcol * (grow - 1); i++) {
            Port port = new Port();
            siloList[i].setPort("DOWN", port);//assign to silo on top
            siloList[i + gcol].setPort("UP", port);//assign to silo on bottom
            portList.add(port);
        }

        ports = portList.toArray(new Port[0]);
    }

    //used to create the boxes which register values are shown. Text is the title of the box
    private VBox createLabelVbox(String text, Label l2) {
        VBox vbox = new VBox();
        vbox.setSpacing(4);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBorder(Border.stroke(Color.WHITE));
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Arial", 10));
        vbox.getChildren().add(label);
        vbox.getChildren().add(l2);

        return vbox;
    }

    private <T> VBox createVBoxFromList(List<T> list) {
        VBox vbox = new VBox();
        vbox.setSpacing(2); // Set spacing between elements
        vbox.setAlignment(Pos.CENTER_LEFT);
        for (T item : list) {
            Label label = new Label(item.toString());
            label.setTextFill(Color.WHITE);
            label.setFont(new Font("Arial", 10));
            vbox.getChildren().add(label);
        }

        return vbox;
    }




    //DEBUG SHIT
    public static void printListOfStringArrays(List<String[]> listOfStringArrays) {
        for (String[] array : listOfStringArrays) {
            System.out.print("[");
            for (int i = 0; i < array.length; i++) {
                System.out.print(array[i]);
                if (i < array.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }

    public static void printListOfStrings(List<String> listOfString){
        String[] array = listOfString.toArray(new String[0]);
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    @Override
    public void start(Stage stage) throws Exception{
        Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args){
        launch(args);
    }

}
