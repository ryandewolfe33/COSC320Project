package data;

import java.util.ArrayList;

public class Node {
    private Flight me = null;
    private int airport_id = -1;
    private ArrayList<Flight> next_flights = null;

    public Node(int airport_id, Flight outgoing_flight, ArrayList<Flight> next_flights){
        me = outgoing_flight;
        this.airport_id = airport_id;
        this.next_flights = next_flights;
    }

    public Flight get(){
        return me;
    }

    public Flight getNextFlight(int index){
        if(index < next_flights.size() && index > 0) {
            return next_flights.get(index);
        }
        return null;
    }

    public int size(){
        return next_flights.size();
    }
}
