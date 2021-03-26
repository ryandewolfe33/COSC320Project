package cosc320;

import algorithms.Astar;
import data.FlightList;
import algorithms.Preprocessor;
import data.Node;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static FlightList data = new FlightList();

    public static void main(String[] args) {
        // todo: get user input (ie. date, airport A/B)
        String user_input = "";
        int airport_A_id = 0;
        int airport_B_id = 0;

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDateTime.parse(user_input,format);

        // todo: ensure path is correct
        File file = new File("dataset/original/On_Time_On_Time_Performance_2017_1.csv");
        data.buildMapFromArray(Objects.requireNonNull(Preprocessor.loadFile(file)), start);
        try {
            Astar pathFinder = new Astar(data);
            var path = pathFinder.findPath(airport_A_id, airport_B_id);
            for(Node n : path){
                System.out.println(n.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
