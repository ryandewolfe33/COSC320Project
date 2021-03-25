import data.Flight;

interface Accessor {
    public void buildMap();

    public ArrayList<Flight> getNextFlights(Flight arrival);
}
