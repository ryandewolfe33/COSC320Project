import Random;
import 

package data;

public class Flight implements Compareable<Flight>{
    private String flightName;
    private String originAirportId;
    private SimpleDateFormat departureDateTime;
    private String destinationAirportId;
    private SimpleDateFormat arrivalDateTime;
    private double ticketPrice;

    public Flight(String[] dataRow){
        this.originAirportId = dataRow[11];
        this.departureDateTime = new SimpleDateFormat(dataRow[0] +"."+ dataRow[2] +"."+ dataRow[3] +" "+
                                                      dataRow[30].substring(0,2) + ":" + dataRow[30].substring(2,4));
        this.destinationAirportId = dataRow[20];
        this.arrivalDateTime = new SimpleDateFormat(dataRow[0] +"."+ dataRow[2] +"."+ dataRow[3] +" "+
                dataRow[41].substring(0,2) + ":" + dataRow[41].substring(2,4));
        this.ticketPrice = makeTicketPrice(Integer.parseInt(dataRow[54]));
        this.flightName = dataRow[8] + dataRow[10] + " on " + this.departureDateTime.
    }

    private double makeTicketPrice(int distanceInMiles){
        Random random = new Random();
        double normalDistribution = (0.02*randomndom.getGaussian())+1  //A variable normally distributed about 1 with std 0.02
        if(normalDistribution<0)
            normalDistribution = 1;
        return 50+0.11*distanceInMiles*normalDistribution;
    }




}
