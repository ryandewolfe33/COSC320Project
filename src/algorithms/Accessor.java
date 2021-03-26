package algorithms;

import data.Flight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeSet;

interface Accessor {
    void buildMapFromArray(Flight[] fly, LocalDateTime startDate);

    ArrayList<Flight> getNextFlights(Flight arrival);
}
