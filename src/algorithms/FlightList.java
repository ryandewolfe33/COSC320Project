package algorithms;

import data.Flight;

import java.util.TreeSet;
import java.util.HashMap;
import java.util.Calendar;
import java.util.ArrayList;

public class FlightList implements Accessor {

    private HashMap<Integer, TreeSet<Flight>> flights;
    private Calendar startDate;

    public FlightList(Calendar start, Flight[] fly) {
        this.startDate = start;
        buildMapFromArray(fly);
    }

    public FlightList(Flight[] fly) {
        startDate = null;
        buildMapFromArray(fly);
    }

    public FlightList(Calendar start) {
        this.startDate = start;
    }

    public FlightList() {
        startDate = null;
    }

    public void setStartDate(Calendar newStart) {
        this.startDate = newStart;
    }

    public Calendar getStartDate() {
        return this.startDate;
    }

    public HashMap<Integer, TreeSet<Flight>> getFlightList() {
        return this.flights;
    }

    public void buildMapFromArray(Flight[] fly) {
        this.flights = new HashMap<Integer, TreeSet<Flight>>(); //String = departure airport ID
        for (Flight i : fly) {
            if (i.getDepartureDateTime().after(startDate) || startDate == null) {
                if (flights.get(i.getOriginAirportId()) != null) {
                    TreeSet<Flight> temp = flights.get(i.getOriginAirportId());
                    temp.add(i);
                    flights.put(i.getOriginAirportId(), temp);
                } else {
                    TreeSet<Flight> temp = new TreeSet<Flight>();
                    temp.add(i);
                    flights.put(i.getOriginAirportId(), temp);
                }
            }
        }
    }

    public ArrayList<Flight> getNextFlights(Flight arrival) {
        int arrID = arrival.getDestinationAirportId();
        Calendar arrTime = arrival.getArrivalDateTime();
        TreeSet<Flight> relevant = flights.get(arrID);
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
