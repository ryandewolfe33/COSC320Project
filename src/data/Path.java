package data;

import java.util.ArrayList;
import java.util.TreeSet;

public class Path {
    TreeSet<Node> nodes = new TreeSet<>();

    public Path(Node tail){
        if (tail != null) {
            Node n = tail;
            do {
                nodes.add(n);
                n = n.parent;
            } while (n != null);
        }
    }

    @Override
    public String toString() {
        String buffer = "";
        for(Node n : nodes){
            buffer = String.format("%s%s", buffer, n.toString());
        }
        return buffer;
    }
}
