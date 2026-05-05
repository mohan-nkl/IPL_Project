package services;

import models.Match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
}
