
package data;

import java.util.Date;
import java.util.Random;

public class Flight implements Comparable<Flight>{
    private String flightName;
    private int originAirportId;
    private Date departureDateTime;
    private int destinationAirportId;
    private Date arrivalDateTime;
    private double ticketPrice;
    private int flightTime;


    public Flight(String[] dataRow){
        this.originAirportId = Integer.parseInt(dataRow[11]);
        this.departureDateTime = new Date(dataRow[0], dataRow[2], dataRow[3], dataRow[30].substring(0,2), dataRow[30].substring(2,4));

        this.destinationAirportId = Integer.parseInt(dataRow[20]);
        this.arrivalDateTime = new Date(dataRow[0], dataRow[2], dataRow[3], dataRow[41].substring(0,2), dataRow[41].substring(2,4));

        this.ticketPrice = makeTicketPrice(Integer.parseInt(dataRow[54]));
        this.flightTime = makeFlightTime();
        this.flightName = dataRow[8] + dataRow[10] + " on " + this.departureDateTime.
    }

    public int getOriginAirportId() {
        return originAirportId;
    }
    public Date getDepartureDateTime() {
        return departureDateTime;
    }
    public int getDestinationAirportId() {
        return destinationAirportId;
    }
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }
    
    private double makeTicketPrice(int distanceInMiles){
        Random random = new Random();
        double normalDistribution = (0.02*random.nextGaussian())+1;  //A variable normally distributed about 1 with std 0.02
        if(normalDistribution<0)
            normalDistribution = 1;
        return 50+0.11*distanceInMiles*normalDistribution;
    }
    public double getTicketPrice(){ return this.ticketPrice;}

    //flightTime has the last 2 digits as the minutes, and the first 0-2 digits as the hours
    private int makeFlightTime(){
        int flightTime = 0;
        flightTime = this.arrivalDateTime.getHours()*100 + this.arrivalDateTime.getMinutes();
        flightTime -= this.departureDateTime.getHours()*100 + this.departureDateTime.getMinutes();
        if(this.departureDateTime.getDate() < this.arrivalDateTime.getDate())
            flightTime += 2400;
        return flightTime;
    }
    public int getFlightTime(){return this.flightTime;}


    //Flights will be ordered lowest to highest originAirportId, then by departureTime
    public int compareTo(Flight other){
        return departureDateTime.compareTo(other.departureDateTime);
    }

    /*public String toString(){
        return flightName + ", leaving ", + originAirportId + " at " + this.departureDateTime.getMonth() + "/" +
                this.departureDateTime.getDate() + " " + this.departureDateTime.getHours() + ":" +
                this.departureDateTime.getMinutes() + " and arriving at " +
                this.destinationAirportId + " at " + this.arrivalDateTime.getMonth() + "/" +
                this.arrivalDateTime.getDate() + " " + this.arrivalDateTime.getHours() + ":"+
                this.arrivalDateTime.getMinutes();
    }*/

}
