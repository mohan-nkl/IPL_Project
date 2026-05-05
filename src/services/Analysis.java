package services;

import models.Delivery;
import models.Match;

import java.util.*;

public class Analysis {

    public static Map<Integer, Integer> matchesPerSeason(List<Match> matches) {
        Map<Integer, Integer> seasonMatchCount = new TreeMap<>();
        for (Match match: matches) {
            int season = match.getSeason();
            seasonMatchCount.put(season, seasonMatchCount.getOrDefault(season, 0) + 1);
        }
        return seasonMatchCount;
    }

    public static Map<String, Integer> totalMatchesWonByEachTeamTillDate(List<Match> matches) {
        Map<String, Integer> teamWinCount = new HashMap<>();
        for (Match match: matches) {
            String winner = match.getWinner();
            if (!winner.isEmpty()) {
                teamWinCount.put(winner, teamWinCount.getOrDefault(winner, 0) + 1);
            }
        }
        return teamWinCount;
    }

    public static Map<String, Integer> totalExtraRunsConcededByEachTeam(Set<Integer> matchIds, List<Delivery> deliveries) {

        Map<String, Integer> teamExtraRuns = new HashMap<>();
        for (Delivery delivery: deliveries) {
            if (matchIds.contains(delivery.getMatchId())) {
                String bowlingTeam = delivery.getBowlingTeam();
                int extraRuns = delivery.getExtraRuns();
                teamExtraRuns.put(bowlingTeam, teamExtraRuns.getOrDefault(bowlingTeam, 0) + extraRuns);
            }
        }
        return teamExtraRuns;
    }

    public static List<Map.Entry<String, Double>> getBestEconomicBowlers(Set<Integer> matchIds, List<Delivery> deliveries) {

        Map<String, Integer> runsConcededByEachPlayer = new HashMap<>();
        Map<String, Integer> ballsBowledByEachPlayer = new HashMap<>();

        for (Delivery delivery: deliveries) {
            if (matchIds.contains(delivery.getMatchId())) {
                String bowler = delivery.getBowler();
                int runs = delivery.getTotalRuns() - delivery.getByeRuns() - delivery.getLegByeRuns() - delivery.getPenaltyRuns();
                runsConcededByEachPlayer.put(bowler, runsConcededByEachPlayer.getOrDefault(bowler, 0) + runs);
                if (delivery.getWideRuns() == 0 && delivery.getNoBallRuns() == 0) {
                    ballsBowledByEachPlayer.put(bowler, ballsBowledByEachPlayer.getOrDefault(bowler, 0) + 1);
                }
            }
        }

        Map<String, Double> bowlerEconomyRate = new HashMap<>();
        for (String bowler: runsConcededByEachPlayer.keySet()) {
            int runs = runsConcededByEachPlayer.get(bowler);
            int balls = ballsBowledByEachPlayer.getOrDefault(bowler, 0);
            if (balls != 0) {
                double economyRate = (runs * 6.0) / balls;
                bowlerEconomyRate.put(bowler, economyRate);
            }
        }

        List<Map.Entry<String, Double>> sortedBowlers = new ArrayList<>(bowlerEconomyRate.entrySet());
        sortedBowlers.sort((a, b) -> Double.compare(a.getValue(), b.getValue()));
        return sortedBowlers;

    }

    public static Map<Integer, List<Match>> getMatchesGroupedAndSortedBySeason(List<Match> matches) {
        Map<Integer, List<Match>> matchesInSeason = new TreeMap<>();
        for (Match match: matches) {
            matchesInSeason.computeIfAbsent(match.getSeason(), k -> new ArrayList<>()).add(match);
        }
        for (Integer season: matchesInSeason.keySet()) {
            matchesInSeason.get(season).sort((a, b) -> a.getDate().compareTo(b.getDate()));
        }
        return matchesInSeason;
    }

    public static Map<Integer, String> getSeasonWinners(Map<Integer, List<Match>> matchesInSeason) {

        Map<Integer, String> seasonWinners = new TreeMap<>();
        for (Map.Entry<Integer, List<Match>> entry: matchesInSeason.entrySet()) {
            List<Match> matches = entry.getValue();
            seasonWinners.put(entry.getKey(), matches.get(matches.size() - 1).getWinner());
        }
        return seasonWinners;
    }

    public static Map<Integer, List<String>> getTopPerformerOfTheSeasonAtGroupStage(Map<Integer, List<Match>> matchesInSeason) {

        Map<Integer, List<String>> topPerformers = new TreeMap<>();
        for (Integer season: matchesInSeason.keySet()) {
            List<Match> matches = matchesInSeason.get(season);
            int cutOff = season < 2011 ? matches.size() - 3 : matches.size() - 4;

            Map<String, Integer> winsByEachTeam = new HashMap<>();
            int mostWins = 0;

            for (int i = 0; i < cutOff; i++) {
                String winner = matches.get(i).getWinner();
                winsByEachTeam.put(winner, winsByEachTeam.getOrDefault(winner, 0) + 1);
                mostWins = Math.max(mostWins, winsByEachTeam.get(winner));
            }

            List<String> top = new ArrayList<>();
            for (String team: winsByEachTeam.keySet()) {
                if (winsByEachTeam.get(team) == mostWins) {
                    top.add(team);
                }
            }

            topPerformers.put(season, top);
        }

        return topPerformers;
    }

}
