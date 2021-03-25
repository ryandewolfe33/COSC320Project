package data;

import java.util.Calendar;
import java.util.Random;

public class Flight implements Comparable<Flight>{
    private final String flightName;
    private final int originAirportId;
    private final Calendar departureDateTime;
    private final int destinationAirportId;
    private final Calendar arrivalDateTime;
    private final double ticketPrice;
    private final int flightTime;


    public Flight(String[] dataRow){
        this.originAirportId = Integer.parseInt(dataRow[11]);
        this.departureDateTime = Calendar.getInstance();
        this.departureDateTime.set(Integer.parseInt(dataRow[0]),
                                   Integer.parseInt(dataRow[2]),
                                   Integer.parseInt(dataRow[3]),
                                   Integer.parseInt(dataRow[30].substring(0, 2)),
                                   Integer.parseInt(dataRow[30].substring(2, 4)));

        this.destinationAirportId = Integer.parseInt(dataRow[20]);
        this.arrivalDateTime = Calendar.getInstance();
        this.arrivalDateTime.set(Integer.parseInt(dataRow[0]),
                Integer.parseInt(dataRow[2]),
                Integer.parseInt(dataRow[3]),
                Integer.parseInt(dataRow[41].substring(0, 2)),
                Integer.parseInt(dataRow[41].substring(2, 4)));

        this.ticketPrice = makeTicketPrice(Integer.parseInt(dataRow[54]));
        this.flightTime = makeFlightTime();
        this.flightName = dataRow[8] + dataRow[10] + " on " + this.departureDateTime.get(Calendar.DATE);
    }

    public int getOriginAirportId(){
        return originAirportId;
    }

    public Calendar getDepartureDateTime(){
        return departureDateTime;
    }

    public int getDestinationAirportId(){
        return destinationAirportId;
    }
    
    public Calendar getArrivalDateTime(){
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

    private int makeFlightTime(){
        int flightTime = 0;
        return flightTime;
    }


    //Flights will be ordered lowest to highest originAirportId, then by departureTime
    public int compareTo(Flight other){
        if(this.originAirportId == other.originAirportId)
            if(this.departureDateTime.before(other.departureDateTime))
                return -1;
            else
                return 1;
        else
            return (this.originAirportId < other.originAirportId)? -1:1;
    }

    public String toString(){
        return flightName + ", leaving " + originAirportId + " at " +  " " +
                this.departureDateTime.get(Calendar.HOUR_OF_DAY) + ":" + this.departureDateTime.get(Calendar.MINUTE) +
                " and arriving at " + this.destinationAirportId + " at " + " " +
                this.arrivalDateTime.get(Calendar.HOUR_OF_DAY) + ":" + this.arrivalDateTime.get(Calendar.MINUTE);
    }
        }


