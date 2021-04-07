package data;

import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.HashMap;

public class FlightList {

    private HashMap<Integer, TreeSet<Flight>> flights;

    public void buildMapFromArray(Flight[] fly, LocalDate startDate, int range) {
        this.flights = new HashMap<>(); //String = departure airport ID
        LocalDate rangeDate = startDate.plusDays(range);
        for (Flight flight : fly) {
            if ((flight.getDepartureDateTime().isAfter(startDate.atStartOfDay())
                    && flight.getDepartureDateTime().isBefore(rangeDate.atStartOfDay().plusHours(23)))) {
                TreeSet<Flight> airport_flights = flights.computeIfAbsent(flight.getOriginAirportId(), k -> new TreeSet<>());
                airport_flights.add(flight);
            }
        }
    }

    public SortedSet<Flight> getAllFlights(int airport_id) {
        return flights.get(airport_id);
    }

    public SortedSet<Flight> getNextFlights(Flight arrival) {
        Flight f1 = new Flight();
        Flight f2 = new Flight();
        f1.setDepartureDateTime(arrival.getArrivalDateTime());
        f2.setDepartureDateTime(f1.getDepartureDateTime().plusHours(40));
        var airport = flights.get(arrival.getDestinationAirportId());
        if(airport != null) {
            return airport.subSet(f1, f2);
        }
        return null;
    }

    private void DebugSet(SortedSet<Flight> flights){
        for(Flight f : flights){
            System.out.println(f.getDepartureDateTime());
        }
    }

}
