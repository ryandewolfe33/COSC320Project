package cosc320;

import algorithms.FlightList;
import algorithms.Preprocessor;
import data.Flight;
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
            var path = findPath(airport_A_id, airport_B_id);
            for(Node n : path){
                System.out.println(n.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ArrayList<Node> findPath(int A, int B) throws Exception {
        ArrayList<Node> path = new ArrayList<>();
        TreeSet<Node> open_list = new TreeSet<>();
        HashSet<Flight> closed_list = new HashSet<>();

        Node current_node = new Node(A,null, new ArrayList<>(data.getAllFlights(A)));
        do {
            open_list.remove(current_node);
            // current outgoing flight
            closed_list.add(current_node.getThisFlight());
            for(int i = 0; i < current_node.getNumNextFlights(); ++i){
                // new outgoing flight
                Flight outgoing_flight = current_node.getNextFlight(i);
                if(!closed_list.contains(outgoing_flight)){
                    Node n = new Node(outgoing_flight.getOriginAirportId(), outgoing_flight, data.getNextFlights(outgoing_flight));
                    // calculate heuristics? or perhaps we should just implement a comparison that does that for us
                    open_list.add(n);
                }
            }
            current_node = open_list.first();
            if(current_node == null){
                throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
            }
        } while(current_node.airport_id != B);
        /*
            Add current node to OpenList

            while (the destination is not the current node && there are still paths to explore) {
                foreach(flight in currentNode){
                    if(flight is not on ClosedList){
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
