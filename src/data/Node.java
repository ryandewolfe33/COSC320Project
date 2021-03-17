package data;

import java.util.ArrayList;

public class Node {
    private ArrayList<Flight> outgoing_flights = new ArrayList<>();

    public Flight get(int index){
        if(index < outgoing_flights.size() && index > 0) {
            return outgoing_flights.get(index);
        }
        return null;
    }
    
    public int size(){
        return outgoing_flights.size();
    }
}
