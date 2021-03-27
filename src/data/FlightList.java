package data;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.ArrayList;

public class FlightList {

    private HashMap<Integer, TreeSet<Flight>> flights;

    public void buildMapFromArray(Flight[] fly, LocalDate startDate) {
        this.flights = new HashMap<>(); //String = departure airport ID
        for (Flight flight : fly) {
            if (startDate == null || flight.getDepartureDateTime().isAfter(startDate.atStartOfDay())) {
                TreeSet<Flight> airport_flights = flights.computeIfAbsent(flight.getOriginAirportId(), k -> new TreeSet<>());
                airport_flights.add(flight);
            }
        }
    }

    public TreeSet<Flight> getAllFlights(int airport_id){
        return flights.get(airport_id);
    }

    public SortedSet<Flight> getNextFlights(Flight arrival) {
        int arrID = arrival.getDestinationAirportId();
        /* todo: ensure Flight.compareTo is going to work correctly with the arrivalTime vs departureTime*/
        Flight copy = new Flight(arrival);
        copy.setDepartureDateTime(copy.getArrivalDateTime());

        TreeSet<Flight> airport = flights.get(arrID);
        return airport.tailSet(copy);
        //return new ArrayList<>(airport.tailSet(arrival));
    }

}
