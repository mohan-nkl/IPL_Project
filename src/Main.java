import models.Delivery;
import models.Match;
import services.Analysis;
import utils.CsvReader;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        List<Match> matches = CsvReader.loadMatches();
        List<Delivery> deliveries = CsvReader.loadDeliveries();

        // Feature 1 - Matches per season
        System.out.println("========================================");
        System.out.println("Matches per season");
        Map<Integer, Integer> matchesPerSeason = Analysis.matchesPerSeason(matches);
        for (Map.Entry<Integer, Integer> entry: matchesPerSeason.entrySet()) {
            System.out.println("Season " + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");


        // Feature 2: Total wins by each team
        System.out.println("========================================");
        System.out.println("Total wins by each team");
        Map<String, Integer> teamWins = Analysis.totalMatchesWonByEachTeamTillDate(matches);
        for (Map.Entry<String, Integer> entry: teamWins.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");

        // Feature 3: Total extra runs conceded by each team in 2016
        System.out.println("========================================");
        System.out.println("Total extra runs conceded by each team in 2016");
        Set<Integer> matchIds = getMatchIds(matches, 2016);
        Map<String, Integer> extraRunsByEachTeam = Analysis.totalExtraRunsConcededByEachTeam(matchIds, deliveries);
        for (Map.Entry<String, Integer> entry: extraRunsByEachTeam.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");


    }

    private static Set<Integer> getMatchIds(List<Match> matches, int season) {
        Set<Integer> matchIds = new HashSet<>();
        for (Match match: matches) {
            if (match.getSeason() == season) {
                matchIds.add(match.getMatchId());
            }
        }
        return matchIds;
    }
}

