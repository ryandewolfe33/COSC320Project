package data;

import java.util.SortedSet;

public class Node implements Comparable<Node> {
    public final Node parent;
    private Flight last_flight = null;
    public final int airport_id;
    private SortedSet<Flight> next_flights = null;
    private final long f;
    private final long g;
    private final long h;

    public Node(int airport_id, Node parent, Flight connecting_flight, SortedSet<Flight> next_flights, long time, long price){
        this.parent = parent;
        this.last_flight = connecting_flight;
        this.airport_id = airport_id;
        this.next_flights = next_flights;
        this.g = time + (parent != null ? parent.g : 0);
        this.h = price + (parent != null ? parent.h : 0);
        this.f = g+h;
    }

    public Flight getThisFlight(){
        return last_flight;
    }

    public Flight getNextFlight(int index){
        if(index < next_flights.size() && index > 0) {
            int i=0;
            for(Flight f: next_flights)
                if(i++ == index)
                    return f;

        }
        return null;
    }

    public int getNumNextFlights(){
        return next_flights.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        if (airport_id != node.airport_id) return false;
        return next_flights.equals(node.next_flights);
    }

    @Override
    public int compareTo(Node o) {
        if(f == o.f){
            if(h == o.h){
                return Long.compare(g, o.g);
            }
            return Long.compare(h, o.h);
        }
        return Long.compare(f, o.f);
    }

    @Override
    public String toString() {
        if(last_flight != null) {
            return String.format("%s\n\t\t [f: %4d; time: %4d minutes; price: %5s]\n", last_flight, f, g, "$" + h);
        }
        return "";
    }
}
