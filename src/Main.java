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
        Set<Integer> matchIds2016 = getMatchIds(matches, 2016);
        Map<String, Integer> extraRunsByEachTeam = Analysis.totalExtraRunsConcededByEachTeam(matchIds2016, deliveries);
        for (Map.Entry<String, Integer> entry: extraRunsByEachTeam.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");


        // Feature 4: Best economic bowlers in 2015
        System.out.println("========================================");
        System.out.println("10 Best economic bowlers in 2015");
        Set<Integer> matchIds2015 = getMatchIds(matches, 2015);
        List<Map.Entry<String, Double>> bestBowlers = Analysis.getBestEconomicBowlers(matchIds2015, deliveries);
        for (int i = 0; i < 10; i++) {
            Map.Entry<String, Double> entry = bestBowlers.get(i);
            System.out.print(entry.getKey() + " : ");
            System.out.printf("%.2f", entry.getValue());
            System.out.println();
        }
        System.out.println("========================================");


        // Feature 5: Matches in every season according to the match's date
        System.out.println("========================================");
        System.out.println("Matches in every season according to the match's date");
        Map<Integer, List<Match>> matchesBySeason = Analysis.getMatchesGroupedAndSortedBySeason(matches);
        for (Map.Entry<Integer, List<Match>> entry: matchesBySeason.entrySet()) {
            System.out.println("----- Season " + entry.getKey() + " -----");
            for (Match match: entry.getValue()) {
                System.out.println(match.getDate() + " | " + match.getTeam1() + " vs " + match.getTeam2() + " | Winner: " + match.getWinner());
            }
        }
        System.out.println("========================================");


        // Feature 6: Get the winner in each season
        System.out.println("========================================");
        System.out.println("Winner of each season");
        Map<Integer, String> seasonWinner = Analysis.getSeasonWinners(matchesBySeason);
        for (Map.Entry<Integer, String> entry: seasonWinner.entrySet()) {
            System.out.println("Season " + entry.getKey() + " : " + entry.getValue());
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

