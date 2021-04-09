package data;

import java.util.ArrayList;
import java.util.TreeSet;

public class Path implements Comparable<Path> {
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
    public int getLength(){
        return nodes.size();
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
