import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main extends Application {

    private Pane root = new Pane(); //Full screen (Board + GUI)
    private final int windowWidth = 1200;
    private final int windowHeight = 800;


    private String programTitle;
    private String humanInstructions;
    private int grow, gcol;
    List<List<Silo>> siloList = new ArrayList<>();




    private Parent createContent() throws FileNotFoundException {

        //parse input and get needed info
        parseInputs();









        root.setPrefSize(windowWidth, windowHeight);
        root.setBackground(Background.fill(Color.BLACK));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

            }
        };
        timer.start();
        return root;
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

        List<String[]> siloCommands = new ArrayList<>(); //List of Silo Commands in order
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
            siloCommands.add(siloCmds.toArray(new String[0]));
            siloCmds.clear();
        }

        List<String[]> inOutPuts = new ArrayList<>(); //List of Silo Commands in order
        for (int i = 0; i < ends.size() - (grow*gcol + 1); i++) {
            int start = ends.get(grow*gcol + (i - 1)) + 1;
            List<String> inoutCmds = new ArrayList<>();
            while (!lines.get(start).equals("END")){
                inoutCmds.add(lines.get(start));
                start++;
            }
            inOutPuts.add(inoutCmds.toArray(new String[0]));
        }
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
