import java.util.List;
import java.util.Set;

public class WheelOfFortune extends GuessingGame {
    public static class WheelOfFortuneGameState extends GameState {
        private String phrase;
        public String hiddenPhrase;
        public Set<Character> previousGuesses;
        public Set<Character> remainingGuesses;
    }

    public static class WheelOfFortuneGuess extends Guess {
        Character value;

        public WheelOfFortuneGuess(Character value) {
            this.value = value;
        }
    }

    private final List<String> phrases;
    private Integer nextPhrase;
    private final List<WheelOfFortunePlayer> players;
    private Integer nextPlayer;

    public WheelOfFortune() {

    }

    public WheelOfFortune() {

    }

    public WheelOfFortune() {

    }

    @Override
    protected GameState initialGameState() {
        return null;
    }

    @Override
    protected Guess getGuess(GameState gameState) {
        return null;
    }

    @Override
    protected void processGuess(GameState gameState, Guess guess) {

    }

    @Override
    protected String getPlayerID() {
        return "";
    }

    @Override
    protected Integer getScore(GameState gameState) {
        return 0;
    }

    @Override
    public Boolean playNext() {
        return null;
    }
}
