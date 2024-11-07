public abstract class Game {
    public static class GameRecord {

    }
    public static class AllGamesRecord {
    }
    public AllGamesRecord playAll() {
        return new AllGamesRecord();
    }
    public abstract GameRecord play();
    public abstract Boolean playNext();
}
