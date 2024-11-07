public abstract class GuessingGame extends Game {
    public class GameState {
        private Boolean wonGame = false;
        private Boolean lostGame = false;
    }

    public class Guess {

    }

    @Override
    public GameRecord play() {
        GameState gameState = initialGameState();
        while(!gameState.wonGame && ! gameState.lostGame) {

        }
    }

    protected abstract GameState initialGameState();
    protected abstract Guess getGuess(GameState gameState);
    protected abstract void processGuess(GameState gameState, Guess guess);


    protected abstract String getPlayerID();
    protected abstract Integer getScore(GameState gameState);
}
