import Random;

package data;

public class Flight implements Compareable<Flight>{
    private String flightName;
    private String originAirportId;
    private Date departureDateTime;
    private String destinationAirportId;
    private Date arrivalDateTime;
    private double ticketPrice;
    private int flightTime;


    public Flight(String[] dataRow){
        this.originAirportId = dataRow[11];
        this.departureDateTime = new Date(dataRow[0], dataRow[2], dataRow[3], dataRow[30].substring(0,2), dataRow[30].substring(2,4));

        this.destinationAirportId = dataRow[20];
        this.arrivalDateTime = new Date(dataRow[0], dataRow[2], dataRow[3], dataRow[41].substring(0,2), dataRow[41].substring(2,4));

        this.ticketPrice = makeTicketPrice(Integer.parseInt(dataRow[54]));
        this.flightTIme = makeFlightTime();
        this.flightName = dataRow[8] + dataRow[10] + " on " + this.departureDateTime.
    }

    public String getOriginAirportId() {
        return originAirportId;
    }
    public Date getDepartureDateTime() {
        return departureDateTime;
    }
    public String getDestinationAirportId() {
        return destinationAirportId;
    }
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }
    
    private double makeTicketPrice(int distanceInMiles){
        Random random = new Random();
        double normalDistribution = (0.02*randomndom.getGaussian())+1  //A variable normally distributed about 1 with std 0.02
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
    public getFlightTime(){return this.flightTime;}


    //Flights will be ordered lowest to highest originAirportId, then by departureTime
    public int compareTo(Flight other){
        if(this.originAirportId == other.originAirportId)
            if(this.departureDateTime.befor(other.departureDateTime))
                return -1;
            else
                return 1;
        else
            return (this.originAirportId < other.originAirportId)? -1:1;
    }

    public String toString(){
        return flightName + ", leaving ", + originAirportId + " at " + this.departureDateTime.getMonth() + "/" +
                this.departureDateTime.getDate() + " " + this.departureDateTime.getHours() + ":" +
                this.departureDateTime.getMinutes() + " and arriving at " +
                this.destinationAirportId + " at " + this.arrivalDateTime.getMonth() + "/" +
                this.arrivalDateTime.getDate() + " " + this.arrivalDateTime.getHours() + ":"+
                this.arrivalDateTime.getMinutes();
    }


}
