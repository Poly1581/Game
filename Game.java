public abstract class Game {
    public static class GameRecord implements Comparable<GameRecord> {
        private Integer score;
        private String playerID;

        public GameRecord(Integer score, String playerID) {
            this.score = score;
            this.playerID = playerID;
        }

        public Integer getScore() {
            return this.score;
        }

        public String getPlayerID() {
            return this.playerID;
        }

        @Override
        public int compareTo(GameRecord otherGameRecord) {
            return this.score - otherGameRecord.score;
        }

        @Override
        public String toString() {
            return playerID + " scored " + score;
        }

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
    }
    public AllGamesRecord playAll() {
        return new AllGamesRecord();
    }
    public abstract GameRecord play();
    public abstract Boolean playNext();
}
