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

    public static Map<Integer, List<String>> getWorstPerformerOfTheSeasonAtGroupStage(Map<Integer, List<Match>> matchesInSeason) {

        Map<Integer, List<String>> worstPerformers = new TreeMap<>();
        for (Integer season: matchesInSeason.keySet()) {
            List<Match> matches = matchesInSeason.get(season);
            int cutoff = season < 2011 ? matches.size() - 3 : matches.size() - 4;

            Map<String, Integer> lossesByEachTeam = new HashMap<>();
            int mostLosses = 0;

            for (int i = 0; i < cutoff; i++) {
                String loser = matches.get(i).getLoser();
                if (!loser.equals("No Winner")) {
                    lossesByEachTeam.put(loser, lossesByEachTeam.getOrDefault(loser, 0) + 1);
                    mostLosses = Math.max(mostLosses, lossesByEachTeam.get(loser));
                }
            }

            List<String> bottom = new ArrayList<>();
            for (String team: lossesByEachTeam.keySet()) {
                if (lossesByEachTeam.get(team) == mostLosses) {
                    bottom.add(team);
                }
            }

            worstPerformers.put(season, bottom);
        }

        return worstPerformers;
    }

    public static Map<Integer, List<String>> getTeamsWhichEnteredKnockOutStage(Map<Integer, List<Match>> matchesInSeason) {

        Map<Integer, List<String>> knockoutTeams = new TreeMap<>();
        for (Integer season: matchesInSeason.keySet()) {
            List<Match> matches = matchesInSeason.get(season);
            int size = matches.size();
            List<String> teams = new ArrayList<>();
            if (season < 2011) {
                teams.add(matches.get(size - 3).getTeam1());
                teams.add(matches.get(size - 3).getTeam2());
                teams.add(matches.get(size - 2).getTeam1());
                teams.add(matches.get(size - 2).getTeam2());
            }
            else {
                teams.add(matches.get(size - 4).getTeam1());
                teams.add(matches.get(size - 4).getTeam2());
                teams.add(matches.get(size - 3).getTeam1());
                teams.add(matches.get(size - 3).getTeam2());
            }
            knockoutTeams.put(season, teams);
        }
        return knockoutTeams;
    }

    public static Map<Integer, Map<String, String>> getHighestStrikeRatePlayerPerTeamPerSeason(
            Map<Integer, List<Match>> matchesBySeason, List<Delivery> deliveries) {

        Map<Integer, Map<Integer, Integer>> lastOverPerInningsPerMatch = getLastOverPerInningsPerMatch(deliveries);
        Map<Integer, Set<Integer>> matchIdsBySeason = getMatchIdsBySeason(matchesBySeason);
        Map<Integer, Map<String, Map<String, int[]>>> statsBySeason = getBatterStatsInLastFiveOvers(deliveries, matchIdsBySeason, lastOverPerInningsPerMatch);
        return getBestStrikeRatePlayerPerTeamPerSeason(statsBySeason);
    }


// Structure: matchId -> inning -> lastOver
    private static Map<Integer, Map<Integer, Integer>> getLastOverPerInningsPerMatch(List<Delivery> deliveries) {

        Map<Integer, Map<Integer, Integer>> lastOverPerInningsPerMatch = new HashMap<>();

        for (Delivery delivery : deliveries) {
            int matchId = delivery.getMatchId();
            int inning = delivery.getInning();
            int over = delivery.getOver();

            if (!lastOverPerInningsPerMatch.containsKey(matchId)) {
                lastOverPerInningsPerMatch.put(matchId, new HashMap<>());
            }

            Map<Integer, Integer> inningOverMap = lastOverPerInningsPerMatch.get(matchId);
            if (!inningOverMap.containsKey(inning) || over > inningOverMap.get(inning)) {
                inningOverMap.put(inning, over);
            }
        }
        return lastOverPerInningsPerMatch;
    }


// Structure: season -> set of matchIds
    private static Map<Integer, Set<Integer>> getMatchIdsBySeason(Map<Integer, List<Match>> matchesBySeason) {
        Map<Integer, Set<Integer>> matchIdsBySeason = new TreeMap<>();
        for (Map.Entry<Integer, List<Match>> seasonEntry : matchesBySeason.entrySet()) {
            int season = seasonEntry.getKey();
            Set<Integer> matchIds = new HashSet<>();
            for (Match match : seasonEntry.getValue()) {
                matchIds.add(match.getMatchId());
            }
            matchIdsBySeason.put(season, matchIds);
        }
        return matchIdsBySeason;
    }

    // Helper 3: Find which season a match belongs to
    private static int getSeasonForMatch(int matchId, Map<Integer, Set<Integer>> matchIdsBySeason) {
        for (Map.Entry<Integer, Set<Integer>> entry : matchIdsBySeason.entrySet()) {
            if (entry.getValue().contains(matchId)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    // Helper 4: Accumulate runs and balls for each batter in the last 5 overs
// Structure: season -> team -> batter -> [runs, balls]
    private static Map<Integer, Map<String, Map<String, int[]>>> getBatterStatsInLastFiveOvers(
            List<Delivery> deliveries,
            Map<Integer, Set<Integer>> matchIdsBySeason,
            Map<Integer, Map<Integer, Integer>> lastOverPerInningsPerMatch) {

        Map<Integer, Map<String, Map<String, int[]>>> statsBySeason = new TreeMap<>();
        for (Delivery delivery : deliveries) {
            int matchId = delivery.getMatchId();
            int inning = delivery.getInning();
            int over = delivery.getOver();

            int season = getSeasonForMatch(matchId, matchIdsBySeason);
            if (season == -1) {
                continue;
            }

            int lastOver = lastOverPerInningsPerMatch.get(matchId).get(inning);
            if (over < lastOver - 4) {
                 continue;
            }

            String team = delivery.getBattingTeam();
            String batter = delivery.getBatter();

            if (!statsBySeason.containsKey(season)) {
                statsBySeason.put(season, new HashMap<>());
            }
            if (!statsBySeason.get(season).containsKey(team)) {
                statsBySeason.get(season).put(team, new HashMap<>());
            }
            if (!statsBySeason.get(season).get(team).containsKey(batter)) {
                statsBySeason.get(season).get(team).put(batter, new int[2]);
            }

            int[] stats = statsBySeason.get(season).get(team).get(batter);
            stats[0] += delivery.getBatterRuns(); // runs
            if (delivery.getWideRuns() == 0) {
                stats[1]++; // balls (exclude wides)
            }
        }
        return statsBySeason;
    }

    // Helper 5: Find the batter with the highest strike rate per team per season
    private static Map<Integer, Map<String, String>> getBestStrikeRatePlayerPerTeamPerSeason(
            Map<Integer, Map<String, Map<String, int[]>>> statsBySeason) {

        Map<Integer, Map<String, String>> result = new TreeMap<>();
        for (Map.Entry<Integer, Map<String, Map<String, int[]>>> seasonEntry : statsBySeason.entrySet()) {
            int season = seasonEntry.getKey();
            Map<String, String> bestPlayerPerTeam = new HashMap<>();

            for (Map.Entry<String, Map<String, int[]>> teamEntry : seasonEntry.getValue().entrySet()) {
                String team = teamEntry.getKey();
                String bestPlayer = null;
                double bestStrikeRate = 0.0;

                for (Map.Entry<String, int[]> playerEntry : teamEntry.getValue().entrySet()) {
                    String batter = playerEntry.getKey();
                    int runs = playerEntry.getValue()[0];
                    int balls = playerEntry.getValue()[1];

                    if (balls == 0) {
                        continue;
                    }

                    double strikeRate = (runs * 100.0) / balls;
                    if (strikeRate > bestStrikeRate) {
                        bestStrikeRate = strikeRate;
                        bestPlayer = batter;
                    }
                }

                if (bestPlayer != null) {
                    bestPlayerPerTeam.put(team, bestPlayer + " : " + String.format("%.2f", bestStrikeRate));
                }
            }

            result.put(season, bestPlayerPerTeam);
        }
        return result;
    }


}
