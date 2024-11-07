import java.util.*;

public abstract class Game {
    public static class GameRecord implements Comparable<GameRecord> {
        private final Integer score;
        private final String playerID;

        /**
         * Constructor for a gamerecord
         * @param score the score of the game
         * @param playerID the player who played the game
         */
        public GameRecord(Integer score, String playerID) {
            this.score = score;
            this.playerID = playerID;
        }

        /**
         * Getter for score
         * @return score of gamerecord
         */
        public Integer getScore() {
            return this.score;
        }

        /**
         * Getter for playerID
         * @return player of gamerecord
         */
        public String getPlayerID() {
            return this.playerID;
        }

        /**
         * Compare method (compare by score)
         * @param otherGameRecord the object to be compared.
         * @return Difference in scores
         */
        @Override
        public int compareTo(GameRecord otherGameRecord) {
            return this.score - otherGameRecord.score;
        }

        /**
         * Tostring method (include score and playerID)
         * @return Stringified game record
         */
        @Override
        public String toString() {
            return playerID + " scored " + score;
        }

        /**
         * Equals method
         * @param other object to compare to
         * @return Equality (true if equal, false otherwise)
         */
        @Override
        public boolean equals(Object other) {
            if(other == null) {
                return false;
            }
            if(!(other instanceof GameRecord)) {
                return false;
            }
            return this.playerID.equals(((GameRecord) other).playerID) && this.score.equals(((GameRecord) other).score);
        }
    }
    public static class AllGamesRecord {
        List<GameRecord> allGameRecords;
        Map<String, List<GameRecord>> playerGameRecords;

        /**
         * Compute average of list of games
         * @param gameRecords game records to be averaged
         * @return average of all gamerecords in gamerecords
         */
        public static Double average(List<GameRecord> gameRecords) {
            Double averageScore = 0.0;
            for(GameRecord gameRecord : gameRecords) {
                averageScore += gameRecord.getScore();
            }
            return averageScore / (double) gameRecords.size();
        }

        /**
         * Return highest games from a given list
         * @param gameRecords list of game records to find highest scores from
         * @param numberOfGames number of games to include in list
         * @return a list of the highest games from the gameRecords passed
         */
        public static List<GameRecord> highGameList(List<GameRecord> gameRecords, Integer numberOfGames) {
            Collections.sort(gameRecords);
            return gameRecords.subList(0, Math.min(gameRecords.size(), numberOfGames));
        }

        /**
         * Constructor for AllGamesRecord.
         * Sets allGamesRecord to empty list and playerGameRecords to empty map
         */
        public AllGamesRecord() {
            this.allGameRecords = new ArrayList<>();
            this.playerGameRecords = new HashMap<>();
        }

        /**
         * Add a given gameRecord to AllGamesRecord (add to allGameRecords and playerGameRecords)
         * @param gameRecord the GameRecord to be added
         */
        public void add(GameRecord gameRecord) {
            this.allGameRecords.add(gameRecord);
            if(!this.playerGameRecords.containsKey(gameRecord.getPlayerID())) {
                this.playerGameRecords.put(gameRecord.getPlayerID(), new ArrayList<>());
            }
            this.playerGameRecords.get(gameRecord.getPlayerID()).add(gameRecord);
        }

        /**
         * Compute the average of all games
         * @return the average of all games
         */
        public Double average() {
            return average(allGameRecords);
        }

        /**
         * Compute the average score for a given player
         * @param playerID the player whose average score is to be computed
         * @return the average score for the given player
         */
        public Double average(String playerID) {
            return playerGameRecords.containsKey(playerID) ? average(playerGameRecords.get(playerID)) : 0.0;
        }

        /**
         * Get the highest scoring games from all games
         * @param numberOfGames the number of games to get (capped at size of all games)
         * @return the highest games (ordered) from all games
         */
        public List<GameRecord> highGameList(Integer numberOfGames) {
            return highGameList(allGameRecords, numberOfGames);
        }

        /**
         * Get highest scoring games from a given players games
         * @param playerID the player whose highest games are to be returned
         * @param numberOfGames the number of games to be returned (capped at size of games)
         * @return the highest games (ordered) from a given players games
         */
        public List<GameRecord> highGameList(String playerID, Integer numberOfGames) {
            return playerGameRecords.containsKey(playerID) ? highGameList(playerGameRecords.get(playerID), numberOfGames) : new ArrayList<>();
        }
    }
    public AllGamesRecord playAll() {
        AllGamesRecord gameRecords = new AllGamesRecord();
        while(playNext()) {
            gameRecords.add(play());
        }
        return gameRecords;
    }
    public abstract GameRecord play();
    public abstract Boolean playNext();
}
