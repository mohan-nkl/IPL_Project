# IPL Data Analysis (2008 - 2017)

A Java-based data analysis project that processes IPL match and delivery data from the 2008 to 2017 seasons.

## Dataset
- `matches.csv` — contains match-level data for every IPL game from 2008 to 2017.
- `deliveries.csv` — contains ball-by-ball delivery data for every match from 2008 to 2017.

## Features Implemented

1. **Matches per Season** — Total number of matches played in each IPL season.
2. **All-time Wins by Team** — Total matches won by each team across all seasons.
3. **Extra Runs in 2016** — Total extra runs conceded by each team during the 2016 season.
4. **Top Economical Bowlers in 2015** — Best bowlers ranked by economy rate during the 2015 season.
5. **Match Schedule by Season** — All matches in each season are listed in chronological order.
6. **Season Winners** — The team that won the IPL trophy in each season.
7. **Best Performing Team per Season in terms of Wins** — Team(s) with the most wins during the group stage of each season.
8. **Worst Performing Team per Season in terms of Losses** — Team(s) with the most losses during the group stage of each season.
9. **Teams in the Knock-out stage** - Teams that qualified for the knock-out stage in each season.
10. **Player with the highest strike-rate** - Player with the highest strike rate in the last 5 overs of the innings from each team in each season

## Project Structure

```
IPL_Project/
├── database/
│   ├── matches.csv
│   └── deliveries.csv
├── src/
│   ├── models/
│   │   ├── Match.java
│   │   └── Delivery.java
│   ├── services/
│   │   └── Analysis.java
│   ├── utils/
│   │   └── CsvReader.java
│   └── Main.java
└── README.md
```

## How to Run

Open the project in an IDE and run `Main.java`.
