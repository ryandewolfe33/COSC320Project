package cosc320;

import data.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    private static FlightList data = new FlightList();

    //"dataset/original/On_Time_On_Time_Performance_2017_1.csv"
    // Use this to set the path to the dataset leave date originairportid destinationairportid
    final static String relativeFilePath = "dataset/original/10000_flight_data.csv";

    public static String userInput() {
        Scanner userParams = new Scanner(System.in);
        System.out.println("Presently, the dataset has American domestic travel only. " +
                "All airport ID's correspond to an actual american airport.\n" + "INPUTS ARE NOT VALIDATED");

        System.out.println("Enter a starting airport (three letter code). Example: JFK");

        String temp = userParams.nextLine();
        if(temp.isEmpty()){
            System.out.println("defaulting to airport id: " + AirportList.getAirportCode(1105703));
        }
        String userStartID = temp.isEmpty() ? "1105703": AirportList.getAirportId(temp).toString();
        System.out.println("Enter a destination airport (three letter code). Example: LAX");

        temp = userParams.nextLine();
        if(temp.isEmpty()){
            System.out.println("defaulting to airport id: " + AirportList.getAirportCode(1104203));
        }
        String userDestID = temp.isEmpty() ? "1104203" : AirportList.getAirportId(temp).toString();

        System.out.println("Enter search range in days (1-30; default 31). January 31st just before midnight is the cut off point for departures.");
        temp = userParams.nextLine();
        String range = temp.isEmpty() ? "50" : temp;

        System.out.println("Enter a start day(00-31). Our dataset only includes data for the month of January 2017.");
        String userStartDate = userParams.nextLine();//"2017-01-03";
        if(userStartDate.isEmpty()){
            System.out.println("defaulting to 2017-01-01");
            userStartDate = "2017-01-01";
        } else {
            userStartDate = "2017-01-" + userStartDate;
        }
        System.out.println("Select an algorithm ('A' or 'B'");
        String algSelect = userParams.nextLine();//"A";
        if(algSelect.isEmpty()) {
            System.out.println("defaulting to A");
            algSelect = "A";
        }



        userParams.close();
        return "dataset/original/On_Time_On_Time_Performance_2017_1.csv " + userStartDate + " " + userStartID + " " + userDestID + " " + range;
    }

    public static void main(String[] args) {
        System.out.println("Data must be located (relative to project) at:\n'dataset/original/(filename)'");
        File file = new File(relativeFilePath);
        Benchmarker bench = new Benchmarker();
        AtomicReference<Flight[]> unsortedRef = new AtomicReference<>();

        System.out.println();
        bench.PrintRuntimeOfThisCode("Loading data took: ", () -> {
            unsortedRef.set(loadFile(file));
            AirportList.printAirports();
        });

        args = userInput().split(" ");
        //String dataSetName = "";
        String user_input = "";
        int airport_A_id = 0;
        int airport_B_id = 0;
        int range = 0;
        String alg = null;
        try {
            //dataSetName = args[0];
            user_input = args[1];
            airport_A_id = Integer.parseInt(args[2]);
            airport_B_id = Integer.parseInt(args[3]);
            range = Integer.parseInt(args[4]);
            alg = args[6];
        } catch (Exception e) {
            System.out.println("Invalid arguments provided");
            e.printStackTrace();
            System.exit(1);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(user_input, format);

        System.out.println();
        int finalRange = range;
        bench.PrintRuntimeOfThisCode("Sorting data took: ", () -> data.buildMapFromArray(Objects.requireNonNull(unsortedRef.get()), start, finalRange));

        System.out.println();
        final int A_id = airport_A_id;
        final int B_id = airport_B_id;
        final String algorithm = alg;
        AtomicReference<Node> path_tail = null;
        bench.PrintRuntimeOfThisCode("Find path took: ", () -> {
            try {
                AlgorithmA A = new AlgorithmA();
                AlgorithmB B = new AlgorithmB();
                if (algorithm.equals("A"))
                    path_tail.set(A.findPath(A_id, B_id, data));
                else path_tail.set(B.findPath(A_id, B_id, data));
                if (path_tail.get() != null) {
                    Node n = path_tail.get();
                    ArrayList<Node> path = new ArrayList<>();
                    do {
                        path.add(n);
                        n = n.parent;
                    } while (n != null);
                    for (int i = path.size() - 1; i >= 0; --i) {
                        System.out.println(path.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static Flight[] loadFile(File data) {
        ArrayList<Flight> listOfFlights = new ArrayList<Flight>(500000);
        try {
            BufferedReader in = new BufferedReader(new FileReader(data), (int) data.length());
            Scanner scanner = new Scanner(in);
            scanner.nextLine(); // dataRow indices
            scanner.nextLine(); // column headings
            Flight flight;
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] dataFromRow = row.split(",");
                if (!dataFromRow[51].isEmpty()) { // flight was cancelled?
                    flight = new Flight(dataFromRow);
                    listOfFlights.add(flight);
                    if (!dataFromRow[14].isEmpty()){
                        Airport origin_airport = new Airport(dataFromRow[14], flight.getOriginAirportId());
                        AirportList.addAirport(origin_airport);
                    }
                    if (!dataFromRow[23].isEmpty()){
                        Airport destination_airport = new Airport(dataFromRow[23], flight.getDestinationAirportId());
                        AirportList.addAirport(destination_airport);
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Sorry, file not found");
            return null;
        }
        Collections.sort(listOfFlights);
        return listOfFlights.toArray(new Flight[0]);
    }
}
