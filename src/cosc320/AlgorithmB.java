package cosc320;

import data.FlightList;
import data.Node;

import java.time.LocalDate;

public class AlgorithmB {

    public AlgorithmB(){}

    public static Node findPath(int A, int B, LocalDate start, FlightList data, Boolean heuristic) throws Exception {
        long layover = 0;
        Node current_node = new Node(A, null, null, data.getAllFlights(A, start), layover);
        return current_node;
    }
}
