package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.temporal.ChronoUnit;

public class Main {
    private static FlightList data = new FlightList();

    public static void main(String[] args) {
        //"dataset/original/On_Time_On_Time_Performance_2017_1.csv"
        // Use this to set the path to the dataset leave date originairportid destinationairportid
        String userInputsAsString = "dataset/original/On_Time_On_Time_Performance_2017_1.csv 2017-01-15 1105703 1104203";
        args = userInputsAsString.split(" ");
        String dataSetName = "";
        String user_input = "";
        int airport_A_id = 0;
        int airport_B_id = 0;
        try {
            dataSetName = args[0];
            user_input = args[1];
            airport_A_id = Integer.parseInt(args[2]);
            airport_B_id = Integer.parseInt(args[3]);
        }
        catch(Exception e){
            System.out.println("Invalid arguments provided");
            e.printStackTrace();
            System.exit(0);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(user_input, format);

        // todo: ensure path is correct
        File file = new File(dataSetName);
        data.buildMapFromArray(Objects.requireNonNull(loadFile(file)), start);
        long startTime = System.currentTimeMillis();
        try {
            Node path_tail = findPath(airport_A_id, airport_B_id, start);
            if(path_tail != null){
                Node n = path_tail;
                do {
                    System.out.println(n.toString());
                    n = n.parent;
                } while (n != null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("The algorithms took " + (endTime - startTime) + " milliseconds");
    }

    public static Flight[] loadFile(File data) {
        ArrayList<Flight> listOfFlights = new ArrayList<Flight>();
        try {
            Scanner forFile = new Scanner(data);
            forFile.nextLine(); // dataRow indices
            forFile.nextLine(); // column headings
            while (forFile.hasNextLine()) {
                String row = forFile.nextLine();
                String[] dataFromRow = row.split(",");
                if (!dataFromRow[51].isEmpty()) {
                    Flight addFlight = new Flight(dataFromRow);
                    listOfFlights.add(addFlight);
                }
            }
            forFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, file not found");
            return null;
        }
        Collections.sort(listOfFlights);
        return listOfFlights.toArray(new Flight[0]);
    }


    public static Node findPath(int A, int B, LocalDate start) throws Exception {
        PriorityQueue<Node> open_list = new PriorityQueue<>();
        HashSet<Flight> closed_list = new HashSet<>();
        long layover = 0;

        Node current_node = new Node(A, null, null, data.getAllFlights(A), layover);
        do {
            // current outgoing flight
            closed_list.add(current_node.getThisFlight());
            for (int i = 0; i < current_node.getNumNextFlights(); ++i) {
                // new outgoing flight
                Flight outgoing_flight = current_node.getNextFlight(i);
                if (!closed_list.contains(outgoing_flight)) {

                    if(current_node.getThisFlight() != null)
                        layover = ChronoUnit.MINUTES.between(current_node.getThisFlight().getArrivalDateTime(), outgoing_flight.getDepartureDateTime());
                    else
                        layover = ChronoUnit.MINUTES.between(outgoing_flight.getDepartureDateTime(), start.atStartOfDay());
                    long layoverAndFlightTime = layover + outgoing_flight.getFlightTime();

                    Node n = new Node(outgoing_flight.getOriginAirportId(), current_node, outgoing_flight, data.getNextFlights(outgoing_flight), layover);
                    // calculate heuristics? or perhaps we should just implement a comparison that does that for us
                    open_list.add(n);
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
