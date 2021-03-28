package data;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public SortedSet<Flight> getAllFlights(String airport_code, LocalDate day) {
        Flight f1 = new Flight();
        Flight f2 = new Flight();
        LocalDateTime first = day.atStartOfDay();
        LocalDateTime second = day.atStartOfDay().plusDays(3);
        f1.setDepartureDateTime(first);
        f2.setDepartureDateTime(second);
        /*System.out.println("first: " + first);
        DebugSet(flights.get(airport_id).subSet(f1,f2));
        System.out.println("second: " + second);/**/
        return flights.get(AirportList.getAirportId(airport_code)).subSet(f1,f2);
    }

    public SortedSet<Flight> getAllFlights(int airport_id, LocalDate day) {
        Flight f1 = new Flight();
        Flight f2 = new Flight();
        LocalDateTime first = day.atStartOfDay();
        LocalDateTime second = day.atStartOfDay().plusDays(3);
        f1.setDepartureDateTime(first);
        f2.setDepartureDateTime(second);
        /*System.out.println("first: " + first);
        DebugSet(flights.get(airport_id).subSet(f1,f2));
        System.out.println("second: " + second);/**/
        return flights.get(airport_id).subSet(f1,f2);
    }

    public SortedSet<Flight> getNextFlights(Flight arrival) {
        int arrID = arrival.getDestinationAirportId();
        /* todo: ensure Flight.compareTo is going to work correctly with the arrivalTime vs departureTime*/
        Flight f1 = new Flight();
        Flight f2 = new Flight();
        f1.setDepartureDateTime(arrival.getArrivalDateTime());
        f2.setDepartureDateTime(f1.getDepartureDateTime().plusDays(2));
        TreeSet<Flight> airport = flights.get(arrID);
        /*var mini_set = airport.tailSet(arrival);
        System.out.println("Copy departure time: " + copy.getDepartureDateTime());
        System.out.println("Flight in arrival time: " + arrival.getArrivalDateTime());
        DebugSet(mini_set);/**/
        return airport.subSet(f1,f2);
    }

    private void DebugSet(SortedSet<Flight> flights){
        for(Flight f : flights){
            System.out.println(f.getDepartureDateTime());
        }
    }

}
