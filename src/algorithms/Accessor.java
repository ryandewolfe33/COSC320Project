package algorithms;

import data.Flight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

interface Accessor {
    public void buildMapFromArray(Flight[] fly);

    public ArrayList<Flight> getNextFlights(Flight arrival);

    public void setStartDate(Calendar newStart);

    public Calendar getStartDate();

    public HashMap<Integer, ArrayList<Flight>> getFlightList();
}
