public interface WheelOfFortunePlayer {
    WheelOfFortune.WheelOfFortuneGuess nextGuess(WheelOfFortune.WheelOfFortuneGameState gameState);
    Boolean playNext();
    String playerID();
    void reset();
}
