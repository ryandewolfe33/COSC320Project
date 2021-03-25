package algorithms;

import data.Flight;

import java.util.HashMap;
import java.util.Calendar;
import java.util.ArrayList;

public class FlightList implements Accessor {

    private HashMap<Integer, ArrayList<Flight>> flights;
    private Calendar startDate;

    public FlightList(Calendar start, Flight[] fly) {
        this.startDate = start;
        buildMapFromArray(fly);
    }

    public void setStartDate(Calendar newStart) {
        this.startDate = newStart;
    }

    public Calendar getStartDate() {
        return this.startDate;
    }

    public HashMap<Integer, ArrayList<Flight>> getFlightList() {
        return this.flights;
    }

    public void buildMapFromArray(Flight[] fly) {
        this.flights = new HashMap<Integer, ArrayList<Flight>>(); //String = departure airport ID
        //TODO: call Liza's method to read in csv
    }

    public ArrayList<Flight> getNextFlights(Flight arrival) {
        int arrID = arrival.getDestinationAirportId();
        Calendar arrTime = arrival.getArrivalDateTime();
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
