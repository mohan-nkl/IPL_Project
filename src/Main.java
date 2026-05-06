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
        printMatchesPerSeason(matchesPerSeason);



        // Feature 2: Total wins by each team
        System.out.println("========================================");
        System.out.println("Total wins by each team");
        Map<String, Integer> teamWins = Analysis.totalMatchesWonByEachTeamTillDate(matches);
        printTeamWins(teamWins);



        // Feature 3: Total extra runs conceded by each team in 2016
        System.out.println("========================================");
        System.out.println("Total extra runs conceded by each team in 2016");
        Set<Integer> matchIds2016 = getMatchIds(matches, 2016);
        Map<String, Integer> extraRunsByEachTeam = Analysis.totalExtraRunsConcededByEachTeam(matchIds2016, deliveries);
        printExtraRunsByEachTeam(extraRunsByEachTeam);


        // Feature 4: Best economic bowlers in 2015
        System.out.println("========================================");
        System.out.println("10 Best economic bowlers in 2015");
        Set<Integer> matchIds2015 = getMatchIds(matches, 2015);
        List<Map.Entry<String, Double>> bestBowlers = Analysis.getBestEconomicBowlers(matchIds2015, deliveries);
        printBestBowlers(bestBowlers);


        // Feature 5: Matches in every season according to the match's date
        System.out.println("========================================");
        System.out.println("Matches in every season according to the match's date");
        Map<Integer, List<Match>> matchesBySeason = Analysis.getMatchesGroupedAndSortedBySeason(matches);
        printMatchesBySeason(matchesBySeason);


        // Feature 6: Get the winner in each season
        System.out.println("========================================");
        System.out.println("Winner of each season");
        Map<Integer, String> seasonWinner = Analysis.getSeasonWinners(matchesBySeason);
        printSeasonWinner(seasonWinner);


        // Feature 7: Get the best performers in each season
        System.out.println("========================================");
        System.out.println("Best performers in each season at Group stage");
        Map<Integer, List<String>> bestPerformers = Analysis.getTopPerformerOfTheSeasonAtGroupStage(matchesBySeason);
        printSeasonTeamMap(bestPerformers);


        // Feature 8: Get the worst performers in each season
        System.out.println("========================================");
        System.out.println("Worst performers in each season at Group stage");
        Map<Integer, List<String>> worstPerformers = Analysis.getWorstPerformerOfTheSeasonAtGroupStage(matchesBySeason);
        printSeasonTeamMap(worstPerformers);


        // Feature 9: Get the teams which qualified for knockout games in each season
        System.out.println("========================================");
        System.out.println("Teams which qualified for knockouts");
        Map<Integer, List<String>> qualifiedTeams = Analysis.getTeamsWhichEnteredKnockOutStage(matchesBySeason);
        printQualifiedTeams(qualifiedTeams);


        // Feature 10: Player with highest strike rate in the last 5 overs from each team over the years.
        System.out.println("========================================");
        System.out.println("Highest strike rate batters in the last 5 overs of the match per team per season");
        Map<Integer, Map<String, String>> strikeRates =
                Analysis.getHighestStrikeRatePlayerPerTeamPerSeason(matchesBySeason, deliveries);
        printHighestStrikeRatePlayerPerTeamPerSeason(strikeRates);
    }

    private static void printHighestStrikeRatePlayerPerTeamPerSeason(Map<Integer, Map<String, String>> strikeRates) {
        for (Map.Entry<Integer, Map<String, String>> seasonEntry : strikeRates.entrySet()) {
            System.out.println("--------");
            System.out.println("Season " + seasonEntry.getKey());
            for (Map.Entry<String, String> teamEntry : seasonEntry.getValue().entrySet()) {
                System.out.println(teamEntry.getKey() + " : " + teamEntry.getValue());
            }
        }
    }

    private static void printQualifiedTeams(Map<Integer, List<String>> qualifiedTeams) {
        for (Map.Entry<Integer, List<String>> entry: qualifiedTeams.entrySet()) {
            System.out.print(entry.getKey() + " : ");
            List<String> teams = entry.getValue();
            for (int i = 0; i < 4; i++) {
                if (i < 3) {
                    System.out.print(teams.get(i) + ", ");
                }
                else {
                    System.out.print(teams.get(i));
                }
            }
            System.out.println();
        }
        System.out.println("========================================");
    }

    private static void printSeasonWinner(Map<Integer, String> seasonWinner) {
        for (Map.Entry<Integer, String> entry: seasonWinner.entrySet()) {
            System.out.println("Season " + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");
    }

    private static void printMatchesBySeason(Map<Integer, List<Match>> matchesBySeason) {
        for (Map.Entry<Integer, List<Match>> entry: matchesBySeason.entrySet()) {
            System.out.println("----- Season " + entry.getKey() + " -----");
            for (Match match: entry.getValue()) {
                System.out.println(match.getDate() + " | " + match.getTeam1() + " vs " + match.getTeam2() + " | Winner: " + match.getWinner());
            }
        }
        System.out.println("========================================");
    }

    private static void printBestBowlers(List<Map.Entry<String, Double>> bestBowlers) {
        for (int i = 0; i < 10; i++) {
            Map.Entry<String, Double> entry = bestBowlers.get(i);
            System.out.print(entry.getKey() + " : ");
            System.out.printf("%.2f", entry.getValue());
            System.out.println();
        }
        System.out.println("========================================");
    }

    private static void printExtraRunsByEachTeam(Map<String, Integer> extraRunsByEachTeam) {
        for (Map.Entry<String, Integer> entry: extraRunsByEachTeam.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");
    }

    private static void printTeamWins(Map<String, Integer> teamWins) {
        for (Map.Entry<String, Integer> entry: teamWins.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");
    }

    private static void printMatchesPerSeason(Map<Integer, Integer> matchesPerSeason) {
        for (Map.Entry<Integer, Integer> entry: matchesPerSeason.entrySet()) {
            System.out.println("Season " + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("========================================");
    }

    private static void printSeasonTeamMap(Map<Integer, List<String>> teamPerformanceMap) {
        for (Map.Entry<Integer, List<String>> entry: teamPerformanceMap.entrySet()) {
            System.out.print(entry.getKey() + " : ");
            List<String> teams = entry.getValue();
            for (int i = 0; i < teams.size(); i++) {
                if (i != teams.size() - 1) {
                    System.out.print(teams.get(i) + ", ");
                }
                else {
                    System.out.print(teams.get(i));
                }
            }
            System.out.println();
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

