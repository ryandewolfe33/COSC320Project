package cosc320;

import data.Flight;
import data.FlightList;
import data.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    private static FlightList data = new FlightList();

    public static void main(String[] args) {
        //"dataset/original/On_Time_On_Time_Performance_2017_1.csv"
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
        try {
            var path = findPath(airport_A_id, airport_B_id);
            for (Node n : path) {
                System.out.println(n.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    public static ArrayList<Node> findPath(int A, int B) throws Exception {
        ArrayList<Node> path = new ArrayList<>();
        PriorityQueue<Node> open_list = new PriorityQueue<>();
        HashSet<Flight> closed_list = new HashSet<>();

        Node current_node = new Node(A, null, null, new ArrayList<>(data.getAllFlights(A)));
        do {
            // current outgoing flight
            closed_list.add(current_node.getThisFlight());
            for (int i = 0; i < current_node.getNumNextFlights(); ++i) {
                // new outgoing flight
                Flight outgoing_flight = current_node.getNextFlight(i);
                if (!closed_list.contains(outgoing_flight)) {
                    Node n = new Node(outgoing_flight.getOriginAirportId(), current_node, outgoing_flight, data.getNextFlights(outgoing_flight));
                    // calculate heuristics? or perhaps we should just implement a comparison that does that for us
                    open_list.add(n);
                }
            }
            current_node = open_list.poll();
            if (current_node == null) {
                throw new Exception("Cannot find a path. There appears to be either an issue with the algorithm, or the data.");
            }
        } while (current_node.airport_id != B);
        return path;
    }

    public static long calculateTimeHeuristic(Flight last, Flight next) {
        return ChronoUnit.MINUTES.between(last.getArrivalDateTime(), next.getArrivalDateTime())+next.getFlightTime();
    }
}
