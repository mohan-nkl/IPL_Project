import models.Delivery;
import models.Match;
import utils.CsvReader;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Match> matches = CsvReader.loadMatches();
        List<Delivery> deliveries = CsvReader.loadDeliveries();


    }
}

