package data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class Path implements Comparable<Path> {
    TreeSet<Node> nodes = new TreeSet<>();
    HashSet<Integer> airports = new HashSet<>();

    public Path(Node tail){
        if (tail != null) {
            Node n = tail;
            do {
                nodes.add(n);
                airports.add(n.airport_id);
                n = n.parent;
            } while (n != null);
        }
    }
    public int getLength(){
        return nodes.size();
    }
    public boolean contains(int airportID){
        if (airports.contains(airportID))
            return true;
        else return false;
    }


    @Override
    public String toString() {
        String buffer = "";
        for(Node n : nodes){
            buffer = String.format("%s%s", buffer, n.toString());
        }
        return buffer;
    }

    @Override
    public int compareTo(Path o) {
        Node tail = nodes.last();
        Node other = o.nodes.last();
        return tail.compareTo(other);
    }
}
