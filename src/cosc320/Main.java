package cosc320;

import algorithms.FlightList;
import algorithms.Preprocessor;
import data.Node;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
        data.buildMapFromArray(Preprocessor.loadFile(file), start);
        var path = findPath(airport_A_id, airport_B_id);
        for(Node n : path){

        }
    }

    static ArrayList<Node> findPath(int A, int B){
        ArrayList<Node> path = new ArrayList<>();
        Node node_A = new Node(A,null, new ArrayList<>(data.getAllFlights(A)));
        Node current_node = node_A;
        /*
            Add current node to OpenList

            while (the destination is not the current node && there are still paths to explore) {
                Set current node to the cheapest node on the OpenList
                Add flight for current node to the ClosedList
                foreach(flight in currentNode){
                    if(flight is not on ClosedList){
                        create new node
                        set new node to flight destination
                        associate all outgoing flights from destination to current node
                        set current node as parent to new node (connecting flight)
                        calculate heuristics for new node
                        if(new node has twin node in OpenList){
                            if(new node is better than existing){
                                update existing node to be new node
                            }
                        } else {
                            add node to OpenList
                        }
                    }
                }
            }
            //found path logic
         */
        return path;
    }
}
