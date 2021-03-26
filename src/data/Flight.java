package data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Flight implements Comparable<Flight>{
    private static final DateTimeFormatter departure_format = DateTimeFormatter.ofPattern("yyyy-MM-dd Hmm");
    private static final DateTimeFormatter time_format = DateTimeFormatter.ofPattern("Hmm");

    private String flightName;
    private double ticketPrice;
    private int flightTime;
    private int distance;

    private int originAirportId;
    private int destinationAirportId;

    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;


    public Flight(String[] dataRow){
        String originId = dataRow[11];
        String destinId = dataRow[20];
        String deptDate = dataRow[5];
        StringBuffer deptTime = new StringBuffer(dataRow[29]);
        StringBuffer arrTime = new StringBuffer(dataRow[40]);
        int required_length = 3;
        int delta = required_length - deptTime.length();
        if(delta > 0){
            deptTime.insert(0,delta == 2 ? "00" : "0");
        }
        deptTime.insert(0,deptDate + " ");
        delta = required_length - arrTime.length();
        if(delta > 0){
            arrTime.insert(0,delta == 2 ? "00" : "0");
        }
        String fliTime = dataRow[51];
        String dist = dataRow[54];
        String fliName = dataRow[8] + dataRow[10];
        try {
            if (false) {
                System.out.println(fliName);
                System.out.printf("origin ID: %s\n", originId);
                System.out.printf("destination ID: %s\n", destinId);
                System.out.printf("departure time: %s\n", deptTime.toString());
                System.out.printf("arrival time: %s\n", arrTime.toString());
                System.out.printf("flight time: %s\n", fliTime);
                System.out.printf("distance: %s\n", dist);
            }
            originAirportId = Integer.parseInt(originId);
            destinationAirportId = Integer.parseInt(destinId);

            departureDateTime = LocalDateTime.parse(deptTime, departure_format);
            flightTime = Integer.parseInt(fliTime);
            int airtime_hours = flightTime / 60;
            int airtime_minutes = flightTime - (airtime_hours * 60);
            LocalDateTime arrival_time = departureDateTime.plusHours(airtime_hours).plusMinutes(airtime_minutes);

            LocalTime local_arrival_time = LocalTime.parse(arrTime, time_format);
            int timezone_offset_hours = local_arrival_time.getHour() - arrival_time.getHour();
            int timezone_offset_minutes = local_arrival_time.getMinute() - arrival_time.getMinute();
            arrivalDateTime = arrival_time
                    .plusHours(timezone_offset_hours).plusMinutes(timezone_offset_minutes)
                    .withHour(local_arrival_time.getHour()).withMinute(local_arrival_time.getMinute());

            distance = Integer.parseInt(dist);
            ticketPrice = makeTicketPrice(distance);
            flightName = fliName + " on " + departureDateTime.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
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
        /* todo: uncomment this code when the heuristics are implemented
        if (this.originAirportId == other.originAirportId) {
            return departureDateTime.compareTo(other.departureDateTime);
        }
        return arrivalDateTime.compareTo(other.departureDateTime);/**/
        return departureDateTime.compareTo(other.departureDateTime);
    }

    public String toString(){
        return flightName + ", leaving " + originAirportId + " at " +  " " +
                this.departureDateTime.getHour() + ":" + this.departureDateTime.getMinute() +
                " and arriving at " + this.destinationAirportId + " at " + " " +
                this.arrivalDateTime.getHour() + ":" + this.arrivalDateTime.getMinute();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;
        return originAirportId == flight.originAirportId
                && destinationAirportId == flight.destinationAirportId
                && departureDateTime.equals(flight.departureDateTime)
                && arrivalDateTime.equals(flight.arrivalDateTime);
    }

    @Override
    public int hashCode() {
        //todo: implement hashCode() and not use generated method
        int result = originAirportId;
        result = 31 * result + destinationAirportId;
        result = 31 * result + departureDateTime.hashCode();
        result = 31 * result + arrivalDateTime.hashCode();
        return result;
    }
}


