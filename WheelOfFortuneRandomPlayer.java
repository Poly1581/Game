public class WheelOfFortuneRandomPlayer implements WheelOfFortunePlayer {
    @Override
    public WheelOfFortune.WheelOfFortuneGuess nextGuess(WheelOfFortune.WheelOfFortuneGameState gameState) {
        return null;
    }

    @Override
    public Boolean playNext() {
        return false;
    }

    @Override
    public String playerID() {
        return "";
    }

    @Override
    public void reset() {

    }
}
