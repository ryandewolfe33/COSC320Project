package data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Flight implements Comparable<Flight>{
    private static final DateTimeFormatter departure_format = DateTimeFormatter.ofPattern("yyyy-MM-ddHHmm");
    private static final DateTimeFormatter time_format = DateTimeFormatter.ofPattern("HHmm");

    private final String flightName;
    private final double ticketPrice;
    private final int flightTime;
    private final int distance;

    private final int originAirportId;
    private final int destinationAirportId;

    private LocalDateTime departureDateTime;
    private final LocalDateTime arrivalDateTime;


    public Flight(String[] dataRow){
        originAirportId = Integer.parseInt(dataRow[11]);
        destinationAirportId = Integer.parseInt(dataRow[20]);

        departureDateTime = LocalDateTime.parse(dataRow[5]+dataRow[30] , departure_format);
        flightTime = Integer.parseInt(dataRow[41]);
        int airtime_hours = flightTime / 60;
        int airtime_minutes = flightTime - (airtime_hours * 60);
        LocalDateTime arrival_time = departureDateTime.plusHours(airtime_hours).plusMinutes(airtime_minutes);
        LocalDateTime local_arrival_time = LocalDateTime.parse(dataRow[41],time_format);
        int timezone_offset_hours = local_arrival_time.getHour() - arrival_time.getHour();
        int timezone_offset_minutes = local_arrival_time.getMinute() - arrival_time.getMinute();
        arrivalDateTime = arrival_time
                .plusHours(timezone_offset_hours).plusMinutes(timezone_offset_minutes)
                .withHour(local_arrival_time.getHour()).withMinute(local_arrival_time.getMinute());

        distance = Integer.parseInt(dataRow[54]);
        ticketPrice = makeTicketPrice(distance);
        flightName = dataRow[8] + dataRow[10] + " on " + departureDateTime.toString();
    }

    public Flight(Flight other) {
        this.flightName = other.flightName;
        this.ticketPrice = other.ticketPrice;
        this.flightTime = other.flightTime;
        this.distance = other.distance;
        this.originAirportId = other.originAirportId;
        this.destinationAirportId = other.destinationAirportId;
        this.departureDateTime = other.departureDateTime;
        this.arrivalDateTime = other.arrivalDateTime;
    }

    public int getOriginAirportId(){
        return originAirportId;
    }

    public LocalDateTime getDepartureDateTime(){
        return departureDateTime;
    }
    public void setDepartureDateTime(LocalDateTime in){
        this.departureDateTime=in;
    }

    public int getDestinationAirportId(){
        return destinationAirportId;
    }

    public LocalDateTime getArrivalDateTime(){
        return arrivalDateTime;
    }

    private double makeTicketPrice(int distanceInMiles){
        Random random = new Random();

        //A variable normally distributed about 1 with std 0.02
        double normalDistribution = (0.02*random.nextGaussian())+1;

        if(normalDistribution<0)
            normalDistribution = 1;
        return 50+0.11*distanceInMiles*normalDistribution;
    }
    public double getTicketPrice(){ return this.ticketPrice;}

    //Flights will be ordered lowest to highest originAirportId, then by departureTime
    public int compareTo(Flight other) {
        if (this.originAirportId == other.originAirportId) {
            return departureDateTime.compareTo(other.departureDateTime);
        }
        return arrivalDateTime.compareTo(other.departureDateTime);/**/
        //return departureDateTime.compareTo(other.departureDateTime);
    }

    public String toString(){
        return flightName + ", leaving " + originAirportId + " at " +  " " +
                this.departureDateTime.getHour() + ":" + this.departureDateTime.getMinute() +
                " and arriving at " + this.destinationAirportId + " at " + " " +
                this.arrivalDateTime.getHour() + ":" + this.arrivalDateTime.getMinute();
    }
}


