import java.util.Random;

public class WheelOfFortuneRandomPlayer implements WheelOfFortunePlayer {
    private Random rand;

    public WheelOfFortuneRandomPlayer() {
        rand = new Random();
        rand.setSeed(System.currentTimeMillis());
    }

    @Override
    public WheelOfFortune.WheelOfFortuneGuess nextGuess(WheelOfFortune.WheelOfFortuneGameState gameState) {
        int randomIndex = rand.nextInt(gameState.remainingGuesses.size());
        int currentIndex = 0;
        for(Character remainingGuess : gameState.remainingGuesses) {
            if(currentIndex == randomIndex) {
                return new WheelOfFortune.WheelOfFortuneGuess(remainingGuess);
            }
            currentIndex++;
        }
        return null;
    }

    @Override
    public Boolean playNext() {
        return true;
    }

    @Override
    public String playerID() {
        return "Random Player";
    }

    @Override
    public void reset() {

    }
}
