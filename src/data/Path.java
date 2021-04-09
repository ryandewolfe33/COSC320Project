package data;

import java.util.ArrayList;
import java.util.TreeSet;

public class Path {
    TreeSet<Flight> nodes = new TreeSet<>();

    public Path(Node tail){
        if (tail != null) {
            Node n = tail;
            do {
                nodes.add(n.getThisFlight());
                n = n.parent;
            } while (n.getThisFlight() != null);
        }
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
