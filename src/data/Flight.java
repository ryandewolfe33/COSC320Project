package data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Flight implements Comparable<Flight>{
    private static final DateTimeFormatter departure_format = DateTimeFormatter.ofPattern("yyyy-MM-dd Hmm");
    private static final DateTimeFormatter time_format = DateTimeFormatter.ofPattern("Hmm");
    private static final Random random = new Random(1988);

    private String flightName;
    private long ticketPrice;
    private int flightTime; // minutes
    private int distance; // miles

    private int originAirportId;
    private int destinationAirportId;

    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private long timezoneOffset; // minutes

    public Flight(){}

    public Flight(String[] dataRow){
        String originId = dataRow[12];
        String destinId = dataRow[21];
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
        String fliTime = dataRow[50];
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
            int h = flightTime / 60;
            int m = flightTime - (h * 60);
            LocalDateTime arrival_time = departureDateTime.plusMinutes(m);
            arrival_time = arrival_time.plusHours(h);
            LocalTime local_arrival_time = LocalTime.parse(arrTime, time_format);
            int timezone_offset_hours = local_arrival_time.getHour() - arrival_time.getHour();
            int timezone_offset_minutes = local_arrival_time.getMinute() - arrival_time.getMinute();
            timezoneOffset = timezone_offset_minutes + (timezone_offset_hours * 60);
            //dealing with timezones and INCORRECT DATA ENTRY, without knowing the actual timezones..
            if(Math.abs(timezoneOffset) < 12){
                arrival_time = arrival_time.plusMinutes(timezoneOffset);
            } else {
                timezoneOffset = 0;
            }
            arrivalDateTime = arrival_time
                    .withHour(local_arrival_time.getHour())
                    .withMinute(local_arrival_time.getMinute());

            distance = Integer.parseInt(dist);
            ticketPrice = makeTicketPrice();
            flightName = fliName;
        } catch (Exception e){
            System.out.println(e.getMessage());
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

    public int getFlightTime(){return flightTime;}

    public long getTimezoneOffset(){
        return timezoneOffset;
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

    private static double lerp(double v0, double v1, double alpha){
        double min = Math.min(v0,v1);
        double max = Math.max(v0,v1);
        return Math.min(max,Math.max(min,min + (alpha * (max-min))));
    }
    public static double clamp(double v, double min, double max){
        return Math.min(max,Math.max(min,v));
    }

    private long makeTicketPrice(){
        double base_cost = 37;
        double time_of_day = departureDateTime.getHour() / 24.0;
        double peak = 14 / 24.0;
        double th = 1 - Math.abs(peak-time_of_day);
        th =  clamp(1.25*th,0,1);
        double min = 0.11*distance;
        double max = 0.20*distance;
        //A variable normally distributed about 1 with std 0.02
        return (long)(base_cost + lerp(min,max,th) + (th*7*random.nextGaussian()));
    }
    public long getTicketPrice(){ return this.ticketPrice;}

    //Flights will be ordered lowest to highest originAirportId, then by departureTime
    public int compareTo(Flight other) {
        return departureDateTime.compareTo(other.departureDateTime);
    }

    public String toString(){
        return String.format("%-8s {departure: %s; ticket price: %4s; origin: %s; destination: %s; flight time: %3d minutes; distance: %4d}",
                flightName,
                departureDateTime,
                "$" + ticketPrice,
                AirportList.getAirportCode(originAirportId),
                AirportList.getAirportCode(destinationAirportId),
                flightTime,
                distance);
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


