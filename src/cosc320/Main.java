package cosc320;

import algorithms.FlightList;
import algorithms.Preprocessor;
import data.Node;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // todo: get user input (ie. date, airport A/B)
        String user_input = "";
        int airport_A_id = 0;
        int airport_B_id = 0;

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDateTime.parse(user_input,format);

        // todo: ensure path is correct
        File file = new File("dataset/original/On_Time_On_Time_Performance_2017_1.csv");
        FlightList data = new FlightList();
        data.buildMapFromArray(Preprocessor.loadFile(file), start);
    }

    ArrayList<Node> findPath(int A, int B){
        ArrayList<Node> path = new ArrayList<>();

        return path;
    }
}
