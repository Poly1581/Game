import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Mastermind extends GuessingGame {
    protected static class MastermindGameState extends GameState {
        private final String code;
        public final List<Character> colors = List.of('R', 'G', 'B', 'Y', 'O', 'P');

        /**
         * Constructor - generate random code
         */
        public MastermindGameState() {
            StringBuilder code = new StringBuilder();
            Random rand = new Random();
            rand.setSeed(System.currentTimeMillis());
            while (code.length() < 4) {
                code.append(colors.get(rand.nextInt(colors.size())));
            }
            this.code = code.toString();
        }

        /**
         * Count number of exact matches between code and guess and remove them by setting to *
         *
         * @param code  the code to be guessed
         * @param guess the guess to compare to the code
         * @return the number of exact matches between code and guess
         */
        public static Integer countExact(StringBuilder code, StringBuilder guess) {
            assert code.length() == guess.length();
            int numberOfExacts = 0;
            for (int i = 0; i < guess.length(); i++) {
                if (code.charAt(i) == guess.charAt(i)) {
                    numberOfExacts++;
                    code.setCharAt(i, '*');
                    guess.setCharAt(i, '*');
                }
            }
            return numberOfExacts;
        }

        /**
         * Count the number of partial matches between code and guess after counting exact matches
         *
         * @param code  the code to be guessed
         * @param guess the guess to compare to the code
         * @return the number of partial matches between code and guess
         */
        public static Integer countPartials(StringBuilder code, StringBuilder guess) {
            assert code.length() == guess.length();
            int numberOfPartials = 0;
            for (int i = 0; i < guess.length(); i++) {
                char guessCharacter = guess.charAt(i);
                for (int j = 0; j < code.length(); j++) {
                    char codeCharacter = code.charAt(j);
                    if (guessCharacter == codeCharacter) {
                        numberOfPartials++;
                        guess.setCharAt(i, '*');
                        code.setCharAt(i, '*');
                    }
                }
            }
            return numberOfPartials;
        }

        /**
         * Process the guess by counting exacts and partials then printing result and updating won game and lost game
         *
         * @param guess the guess to compare to the code
         */
        public void processGuess(MastermindGuess guess) {
            StringBuilder codeSB = new StringBuilder(this.code);
            StringBuilder guessSB = new StringBuilder(guess.value);
            Integer numberOfExacts = countExact(codeSB, guessSB);
            Integer numberOfPartials = countPartials(codeSB, guessSB);
            System.out.println(guess.value + " has " +
                    numberOfExacts + " exact matches and " +
                    numberOfPartials + " partial matches");
            this.wonGame = numberOfExacts == 4;
            this.lostGame = --this.numberOfGuesses == 0;
        }
    }

    protected static class MastermindGuess extends Guess {
        public String value;

        public MastermindGuess(String value) {
            this.value = value;
        }
    }

    private final Scanner userInput = new Scanner(System.in);
    private final String playerID;

    public Mastermind() {
        System.out.println("Enter your player ID:");
        this.playerID = userInput.next();
    }

    public static void main(String[] args) {
        Game test = new Mastermind();
        AllGamesRecord record = test.playAll();
        System.out.println("Average score: " + record.average());
        System.out.println("3 highest scores: ");
        for(GameRecord highGame : record.highGameList(3)) {
            System.out.println("\t" + highGame);
        }
    }

    @Override
    protected GameState initialGameState() {
        return new MastermindGameState();
    }

    /**
     * Prompt player for guess
     * @param gameState The current gamestate (for printing colors)
     */
    protected static void promptGuess(GameState gameState) {
        assert gameState instanceof MastermindGameState;
        System.out.println(gameState.numberOfGuesses + " guesses remain.");
        System.out.println("Please enter a 4-letter combination of the characters below:");
        for(Character color : ((MastermindGameState) gameState).colors) {
            System.out.print(color + " ");
        }
        System.out.println();
    }

    /**
     * Check if a guess only contains colors in the gameState
     * @param gameState the current gamestate
     * @param guess the guess to check for erroneous letters
     * @return
     */
    protected static Boolean onlyColors(MastermindGameState gameState, String guess) {
        for(int i = 0; i < guess.length(); i++) {
            if(!gameState.colors.contains(guess.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Guess getGuess(GameState gameState) {
        assert gameState instanceof MastermindGameState;
        String guess;
        Boolean invalidGuess = true;
        do {
            promptGuess(gameState);
            guess = userInput.next().toUpperCase().substring(0,4);
            if(guess.length() != 4) {
                System.out.println("Your guess (" + guess + ") is not 4 letters, please try again.");
                continue;
            }
            if(!onlyColors((MastermindGameState) gameState, guess)) {
                System.out.println("Your guess contains letters other than the defined colors, please try again");
                continue;
            }
            invalidGuess = false;
        } while(invalidGuess);
        return new MastermindGuess(guess);
    }

    @Override
    protected void processGuess(GameState gameState, Guess guess) {
        assert gameState instanceof MastermindGameState;
        assert guess instanceof MastermindGuess;
        ((MastermindGameState) gameState).processGuess((MastermindGuess) guess);
    }

    @Override
    protected String getPlayerID() {
        return this.playerID;
    }

    @Override
    protected Integer getScore(GameState gameState) {
        return 10 - gameState.numberOfGuesses;
    }

    @Override
    public Boolean playNext() {
        System.out.println("Would you like to play a game of Wheel of Fortune?");
        System.out.println("Enter 'yes' to play:");
        return this.userInput.next().equals("yes");
    }
}
