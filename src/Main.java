import models.Delivery;
import models.Match;
import services.Analysis;
import utils.CsvReader;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        List<Match> matches = CsvReader.loadMatches();
        List<Delivery> deliveries = CsvReader.loadDeliveries();

        // Feature 1 - Matches per season
        System.out.println("========================================");
        Map<Integer, Integer> matchesPerSeason = Analysis.matchesPerSeason(matches);
        for (Map.Entry<Integer, Integer> entry: matchesPerSeason.entrySet()) {
            System.out.println("Season " + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");


    }
}

