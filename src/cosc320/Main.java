package cosc320;

import data.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    private enum HeuristicType {
        USE_TIME,
        USE_PRICE;
    };
    private static FlightList data = new FlightList();
    private static HeuristicType heuristicType;

    public static String userInput() {
        Scanner userParams = new Scanner(System.in);
        System.out.println("Presently, the dataset has American domestic travel only. " +
                "All airport ID's correspond to an actual american airport.\n" + "INPUTS ARE NOT VALIDATED");
        System.out.println("Type 'price' to search for the cheapest path or 'time' to find the shortest path");
        String heuristicSelection = userParams.nextLine();//"time";
        if(heuristicSelection.isEmpty()){
            System.out.println("defaulting to 'time'");
            heuristicSelection = "time";
        }
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
        String userDestID = temp.isEmpty() ? "1104203" : AirportList.getAirportId(userParams.nextLine()).toString();


        System.out.println("Enter a day(00-30). Our dataset only includes data for the month of January 2017.");
        String userStartDate = userParams.nextLine();//"2017-01-03";
        if(userStartDate.isEmpty()){
            System.out.println("defaulting to 2017-01-01");
            userStartDate = "2017-01-01";
        } else {
            userStartDate = "2017-01-" + userStartDate;
        }
        userParams.close();
        return "dataset/original/On_Time_On_Time_Performance_2017_1.csv " + userStartDate + " " + userStartID + " " + userDestID + " " + heuristicSelection;
    }

    public static void main(String[] args) {
        //"dataset/original/On_Time_On_Time_Performance_2017_1.csv"
        // Use this to set the path to the dataset leave date originairportid destinationairportid
        System.out.println("Data must be located (relative to project) at:\n'dataset/original/On_Time_On_Time_Performance_2017_1.csv'");
        File file = new File("dataset/original/On_Time_On_Time_Performance_2017_1.csv");
        Benchmarker B = new Benchmarker();
        AtomicReference<Flight[]> unsortedRef = new AtomicReference<>();

        System.out.println();
        B.PrintRuntimeOfThisCode("Loading data took: ", () -> {
            unsortedRef.set(loadFile(file));
            AirportList.printAirports();
        });

        args = userInput().split(" ");
        String dataSetName = "";
        String user_input = "";
        int airport_A_id = 0;
        int airport_B_id = 0;
        try {
            dataSetName = args[0];
            user_input = args[1];
            airport_A_id = Integer.parseInt(args[2]);
            airport_B_id = Integer.parseInt(args[3]);
            if (args[4].equals("time")) {
                heuristicType = HeuristicType.USE_TIME;
            } else {
                heuristicType = HeuristicType.USE_PRICE;
            }
        } catch (Exception e) {
            System.out.println("Invalid arguments provided");
            e.printStackTrace();
            System.exit(0);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(user_input, format);

        System.out.println();
        B.PrintRuntimeOfThisCode("Sorting data took: ", () -> data.buildMapFromArray(Objects.requireNonNull(unsortedRef.get()), start));

        System.out.println();
        final int A_id = airport_A_id;
        final int B_id = airport_B_id;
        B.PrintRuntimeOfThisCode("Find path took: ", () -> {
            try {
                Node path_tail = findPath(A_id, B_id, start);
                if (path_tail != null) {
                    Node n = path_tail;
                    ArrayList<Node> path = new ArrayList<>();
                    do {
                        path.add(n);
                        n = n.parent;
                    } while (n != null);
                    for (int i = path.size() - 1; i >= 0; --i) {
                        System.out.print(path.get(i));
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
                        Airport destination_airport = new Airport(dataFromRow[23], flight.getOriginAirportId());
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

    public static Node findPath(int A, int B, LocalDate start) throws Exception {
        PriorityQueue<Node> open_list = new PriorityQueue<>();
        HashSet<Flight> closed_list = new HashSet<>();
        long heuristic = 0;
        long layover = 0;

        Node current_node = new Node(A, null, null, data.getAllFlights(A, start), layover);
        do {
            // current outgoing flight
            Flight this_flight = current_node.getThisFlight();
            closed_list.add(this_flight);
            for (int i = 0; i < current_node.getNumNextFlights(); ++i) {
                // new outgoing flight
                Flight next_flight = current_node.getNextFlight(i);
                if (!closed_list.contains(next_flight)) {

                    if (this_flight != null) {
                        var edge_A_side = this_flight.getArrivalDateTime();
                        var edge_B_side = next_flight.getDepartureDateTime();
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side) - this_flight.getTimezoneOffset();
                    } else {
                        var edge_A_side = start.atStartOfDay();
                        var edge_B_side = next_flight.getDepartureDateTime();
                        if(next_flight.getDestinationAirportId() == B){
                            continue;
                        }
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side);
                    }
                    switch(heuristicType){
                        case USE_TIME:
                            heuristic = layover + next_flight.getFlightTime();
                            break;
                        case USE_PRICE:
                            heuristic = (long) next_flight.getTicketPrice();
                            break;
                        default:
                            heuristic = Long.MIN_VALUE;
                    }
                    Node n = new Node(next_flight.getDestinationAirportId(), current_node, next_flight, data.getNextFlights(next_flight), heuristic);
                    if (!open_list.contains(n)) {
                        open_list.add(n);
                    }
                }
            }
            current_node = open_list.poll();
            if (current_node == null) {
                throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
            }
        } while (current_node.airport_id != B);
        return current_node;
    }

}
