import data.Flight;
import java.util.ArrayList;

interface Accessor {
    public void buildMap();

    public ArrayList<Flight> getNextFlights(Flight arrival);
}
