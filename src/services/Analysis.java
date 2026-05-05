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
}
