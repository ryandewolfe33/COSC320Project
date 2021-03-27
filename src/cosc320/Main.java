package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.temporal.ChronoUnit;

public class Main {
    private static FlightList data = new FlightList();
    private static Boolean heuristic;

    public static void main(String[] args) {
        //"dataset/original/On_Time_On_Time_Performance_2017_1.csv"
        // Use this to set the path to the dataset leave date originairportid destinationairportid
        args = userInput().split(" ");
        String dataSetName = "";
        String user_input = "";
        int airport_A_id = 0;
        int airport_B_id = 0;
        heuristic = false;
        try {
            dataSetName = args[0];
            user_input = args[1];
            airport_A_id = Integer.parseInt(args[2]);
            airport_B_id = Integer.parseInt(args[3]);
            if (args[4].equals("time"))
                heuristic = true;
        } catch (Exception e) {
            System.out.println("Invalid arguments provided");
            e.printStackTrace();
            System.exit(0);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(user_input, format);

        // todo: ensure path is correct
        File file = new File(dataSetName);
        Benchmarker B = new Benchmarker();

        System.out.println();
        B.PrintRuntimeOfThisCode("Data load took: ", () -> data.buildMapFromArray(Objects.requireNonNull(loadFile(file)), start));

        System.out.println();
        final int A_id = airport_A_id;
        final int B_id = airport_B_id;
        B.PrintRuntimeOfThisCode("Path finding took: ", () -> {
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
        ArrayList<Flight> listOfFlights = new ArrayList<Flight>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(data), (int) data.length());
            Scanner scanner = new Scanner(in);
            scanner.nextLine(); // dataRow indices
            scanner.nextLine(); // column headings
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] dataFromRow = row.split(",");
                if (!dataFromRow[51].isEmpty()) {
                    Flight addFlight = new Flight(dataFromRow);
                    listOfFlights.add(addFlight);
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

    public static String userInput() {
        Scanner userParams = new Scanner(System.in);
        System.out.println("Presently, the dataset has American domestic travel only. " +
                "All airport ID's correspond to an actual american airport.\n" + "INPUTS ARE NOT VALIDATED");
        System.out.println("Type 'price' to search for the cheapest path or 'time' to find the shortest path");
        String heuristicSelection = userParams.nextLine();//"time";
        System.out.println("Enter a starting airport ID (seven-digit integer). Example: 1105703");
        String userStartID = userParams.nextLine();//"1105703";
        System.out.println("Enter a destination airport ID (seven digit integer). Example: 1104203");
        String userDestID = userParams.nextLine();//"1104203";
        System.out.println("Enter a day(00-30). Our dataset only includes data for the month of January 2017.");
        String userStartDate = "2017-01-" + userParams.nextLine();//"2017-01-03";
        userParams.close();
        return "dataset/original/On_Time_On_Time_Performance_2017_1.csv " + userStartDate + " " + userStartID + " " + userDestID + " " + heuristicSelection;
    }

    public static Node findPath(int A, int B, LocalDate start) throws Exception {
        PriorityQueue<Node> open_list = new PriorityQueue<>();
        HashSet<Flight> closed_list = new HashSet<>();
        long heuristicValue;
        long layover = 0;

        Node current_node = new Node(A, null, null, data.getAllFlights(A, start), layover);
        do {
            // current outgoing flight
            closed_list.add(current_node.getThisFlight());
            for (int i = 0; i < current_node.getNumNextFlights(); ++i) {
                // new outgoing flight
                Flight outgoing_flight = current_node.getNextFlight(i);
                if (!closed_list.contains(outgoing_flight)) {
                    if (current_node.getThisFlight() != null) {
                        var edge_A_side = current_node.getThisFlight().getArrivalDateTime();
                        var edge_B_side = outgoing_flight.getDepartureDateTime();
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side);
                    } else {
                        var edge_A_side = start.atStartOfDay();
                        var edge_B_side = outgoing_flight.getDepartureDateTime();
                        if(outgoing_flight.getDestinationAirportId() == B){
                            continue;
                        }
                        layover = ChronoUnit.MINUTES.between(edge_A_side, edge_B_side);
                    }
                    if (heuristic) {
                        heuristicValue = layover + outgoing_flight.getFlightTime();
                    } else {
                        heuristicValue = (long) outgoing_flight.getTicketPrice();
                    }
                    Node n = new Node(outgoing_flight.getDestinationAirportId(), current_node, outgoing_flight, data.getNextFlights(outgoing_flight), heuristicValue);
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
