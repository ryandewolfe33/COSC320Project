package algorithms;

import java.io.File;

import data.Flight;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDateTime;
public class Preprocessor {
    public static Flight[] loadFile(File data){
        ArrayList<Flight> listOfFlights = new ArrayList<Flight>();
        try{
            Scanner forFile=new Scanner(data);
            forFile.nextLine(); // dataRow indices
            forFile.nextLine(); // column headings
            while(forFile.hasNextLine()){
                String row=forFile.nextLine();
                String[] dataFromRow=row.split(",");
                if(!dataFromRow[51].isEmpty()) {
                    Flight addFlight = new Flight(dataFromRow);
                    listOfFlights.add(addFlight);
                }
            }
            forFile.close();
        }
        catch(FileNotFoundException e) {
                System.out.println("Sorry, file not found");
                return null;
            }
        Collections.sort(listOfFlights);
        return listOfFlights.toArray(new Flight[0]);
    }
    //The second method only adds flight into an array if departure time is after dateRestriction
    public static Flight[] loadFile(File data, LocalDateTime dateRestriction){
        ArrayList<Flight> listOfFlights = new ArrayList<Flight>();
        try{
            Scanner forFile=new Scanner(data);
            while(forFile.hasNext()){
                String row=forFile.next();
                String[] dataFromRow=row.split(", ");
                Flight addFlight=new Flight(dataFromRow);
                if(addFlight.getDepartureDateTime().compareTo(dateRestriction)>=0){
                    listOfFlights.add(addFlight);
                }
            }
            forFile.close();
        }
        catch(FileNotFoundException e) {
                System.out.println("Sorry, file not found");
                return null;
            }
        Collections.sort(listOfFlights);
        return listOfFlights.toArray(new Flight[0]);
    }
}