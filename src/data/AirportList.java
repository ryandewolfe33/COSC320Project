package data;

import java.util.HashMap;

public class AirportList {
    private static final HashMap<String,Integer> code_lookup = new HashMap<>();
    private static final HashMap<Integer,String> id_lookup = new HashMap<>();

    public static Integer getAirportId(String code){
        return code_lookup.get(code);
    }
    public static String getAirportCode(Integer id){
        return id_lookup.get(id);
    }
    public static void addAirport(Airport airport){
        code_lookup.put(airport.code, airport.id);
        id_lookup.put(airport.id, airport.code);
    }

    public static void printAirports(){
        int i = 0;
        for(var kv : code_lookup.entrySet()){
            if(i++ == 15) {
                i = 1;
                System.out.println();
            }
            System.out.printf("%s, ", kv.getKey());
        }
        System.out.println();
    }
}
