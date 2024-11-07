import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    public static class GameRecord implements Comparable<GameRecord> {
        private Integer score;
        private String playerID;

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
        List<GameRecord> gameRecords;

        public AllGamesRecord() {
            gameRecords = new ArrayList<>();
        }


    }
    public AllGamesRecord playAll() {
        return new AllGamesRecord();
    }
    public abstract GameRecord play();
    public abstract Boolean playNext();
}
