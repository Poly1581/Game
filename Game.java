public abstract class Game {
    public static class GameRecord {
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
    }
    public static class AllGamesRecord {
    }
    public AllGamesRecord playAll() {
        return new AllGamesRecord();
    }
    public abstract GameRecord play();
    public abstract Boolean playNext();
}
