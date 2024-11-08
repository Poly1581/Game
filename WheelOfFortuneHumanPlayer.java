import java.util.Scanner;

public class WheelOfFortuneHumanPlayer implements WheelOfFortunePlayer {
    private final String playerID;
    private final Scanner userInput = new Scanner(System.in);

    public WheelOfFortuneHumanPlayer() {
        this.playerID = getPlayerID();
    }

    private String getPlayerID() {
        System.out.println("Enter your player ID:");
        return this.userInput.next();
    }

    @Override
    public WheelOfFortune.WheelOfFortuneGuess nextGuess(WheelOfFortune.WheelOfFortuneGameState gameState) {
        Character guess = null;
        boolean invalidGuess = true;
        while(invalidGuess) {
            System.out.println(gameState);
            System.out.println("Enter your guess:");
            guess = Character.toLowerCase(this.userInput.next().charAt(0));
            if(gameState.previousGuesses.contains(guess)) {
                System.out.println("You have already guessed " + guess + " before, please enter a new guess.");
                continue;
            }
            if(!gameState.remainingGuesses.contains(guess)) {
                System.out.println("You entered an invalid guess (" + guess + "), please enter a valid guess.");
                continue;
            }
            invalidGuess = false;
        }
        return new WheelOfFortune.WheelOfFortuneGuess(guess);
    }

    @Override
    public Boolean playNext() {
        System.out.println("Would you like to play a game of Wheel of Fortune?");
        System.out.println("Enter 'yes' to play:");
        return this.userInput.next().equals("yes");
    }

    @Override
    public String playerID() {
        return this.playerID;
    }

    @Override
    public void reset() {

    }
}
