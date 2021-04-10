package data;

import java.util.HashSet;
import java.util.TreeSet;

public class Path implements Comparable<Path> {
    TreeSet<Node> nodes = new TreeSet<>();
    HashSet<Integer> airports = new HashSet<>();
    Node last = null;

    public Path(Node tail) {
        if (tail != null) {
            last = tail;
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

    public Node getTail() {
        return last;
    }

    public boolean contains(int airportID) {
        return airports.contains(airportID);

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
