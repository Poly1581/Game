public abstract class GuessingGame extends Game {
    public static class GameState {
        private Integer numberOfGuesses;
        private Boolean wonGame = false;
        private Boolean lostGame = false;
    }

    public static class Guess {

    }

    @Override
    public GameRecord play() {
        GameState gameState = initialGameState();
        while(!gameState.wonGame && ! gameState.lostGame) {
            processGuess(gameState, getGuess(gameState));
        }
        if(gameState.wonGame) {
            System.out.println("Congratulations, you won!");
        } else {
            System.out.println("Womp womp, you lost.");
        }
        return new GameRecord(getPlayerID(), getScore(gameState));
    }

    /**
     * Create initial game state for a given concrete guessing game
     * @return initial game state
     */
    protected abstract GameState initialGameState();

    /**
     * Get guess given gamestate
     * @param gameState current state of the game
     * @return guess object
     */
    protected abstract Guess getGuess(GameState gameState);

    /**
     * Process guess by updting gameState
     * @param gameState current state of the game
     * @param guess previous guess
     */
    protected abstract void processGuess(GameState gameState, Guess guess);

    /**
     * Get player ID (either AI or person)
     * @return
     */
    protected abstract String getPlayerID();

    /**
     * Score a given game based on its resulting gamestate
     * @param gameState the final gamestate of a game
     * @return the score of a game
     */
    protected abstract Integer getScore(GameState gameState);
}
