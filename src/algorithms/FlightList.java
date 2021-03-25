package algorithms;

import data.Flight;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;

public class FlightList implements Accessor {

    private HashMap<String, ArrayList<Flight>> flights;
    private Date startDate;

    public FlightList(Date start) {
        this.startDate = start;
    }

    public void setStartDate(Date newStart) {
        this.startDate = newStart;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public HashMap<String, ArrayList<Flight>> getFlightList() {
        return this.flights;
    }

    private void buildFromDate(Date start) {
        this.flights = new HashMap<String, ArrayList<Flight>>(); //String = departure airport ID
        //TODO: call Liza's method to read in csv
    }

    public void buildMap() {
        buildFromDate(startDate);
    }

    public ArrayList<Flight> getNextFlights(Flight arrival) {
        String arrID = arrival.getDestinationAirportId();
        Date arrTime = arrival.getArrivalDateTime();
        ArrayList<Flight> relevant = flights.get(arrID);
        ArrayList<Flight> connections = new ArrayList<Flight>();
        for (int i = 0; i < relevant.size(); i++) {
            if (relevant.get(i).getDepartureDateTime().after(arrTime))   //if sorted by time (desc), add all values until this becomes false
                connections.add(relevant.get(i));
            else if (relevant.get(i) == null)
                break;
        }
        return connections;
    }

}
