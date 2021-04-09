package data;

import java.util.HashSet;
import java.util.TreeSet;

public class Path implements Comparable<Path> {
    TreeSet<Node> nodes = new TreeSet<>();
    HashSet<Integer> airports = new HashSet<>();
    Node header = null;

    public Path(Node tail) {
        if (tail != null) {
            header = tail;
            Node n = tail;
            do {
                nodes.add(n);
                airports.add(n.airport_id);
                n = n.parent;
            } while (n != null);
        }
    }

    public int getLength() {
        return nodes.size();
    }

    public Node getHeader() {
        return header;
    }

    public boolean contains(int airportID) {
        if (airports.contains(airportID))
            return true;
        else return false;
    }


    @Override
    public String toString() {
        String buffer = "";
        for (Node n : nodes) {
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
