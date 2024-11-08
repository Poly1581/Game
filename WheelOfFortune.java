import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class WheelOfFortune extends GuessingGame {
    protected static class WheelOfFortuneGameState extends GameState {
        private final String phrase;
        public String hiddenPhrase;
        public final List<Character> previousGuesses = new ArrayList<>();
        public final List<Character> remainingGuesses = new ArrayList<>();

        public WheelOfFortuneGameState(String phrase) {
            this.phrase = phrase;
            for(char c = 'a'; c < 'z'; c++) {
                remainingGuesses.add(c);
            }
            getHiddenPhrase();
        }

        /**
         * Generate hidden phrase using phrase and remaining guesses.
         */
        public void getHiddenPhrase() {
            StringBuilder hiddenPhrase = new StringBuilder(this.phrase);
            for(int i = 0; i < hiddenPhrase.length(); i++) {
                char hiddenCharacter = hiddenPhrase.charAt(i);
                if(!Character.isWhitespace(hiddenCharacter)
                        && this.remainingGuesses.contains(Character.toLowerCase(hiddenCharacter))) {
                    hiddenPhrase.setCharAt(i, '*');
                }
            }
            this.hiddenPhrase = hiddenPhrase.toString();
        }

        public Boolean correctGuess(WheelOfFortuneGuess guess) {
            return this.phrase.indexOf(guess.value) != -1
                    || this.phrase.indexOf(Character.toUpperCase(guess.value)) != -1;
        }

        /**
         * Update game state based on guess
         * <ol>
         *     <li>
         *         Remove guess from remaining guesses
         *     </li>
         *     <li>
         *         Add guess to previous guesses
         *     </li>
         *     <li>
         *         If guess is not in phrase:
         *         <ol>
         *             <li>
         *                 Decrement number of guesses
         *             </li>
         *             <li>
         *                 Set lost game to true if no guesses remain
         *             </li>
         *         </ol>
         *     </li>
         *     <li>
         *         If guess is in phrase:
         *         <ol>
         *             <li>
         *                 Update hidden phrase
         *             </li>
         *             <li>
         *                 Set won game to true if there are no more *'s in hidden phrase
         *             </li>
         *         </ol>
         *     </li>
         * </ol>
         * @param guess the player's previous guess
         */
        public void processGuess(WheelOfFortuneGuess guess) {
            this.remainingGuesses.remove(guess.value);
            this.previousGuesses.add(guess.value);
            if (correctGuess(guess)) {
                getHiddenPhrase();
                this.wonGame = this.hiddenPhrase.indexOf('*') == -1;
            } else {
                this.lostGame = --this.numberOfGuesses == 0;
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.numberOfGuesses).append(" guesses remain.").append("\n");
            sb.append("The hidden phrase is ").append("\n");
            sb.append(this.hiddenPhrase).append("\n");
            sb.append("Previous guesses are: ").append("\n");
            for(Character previousGuess : this.previousGuesses) {
                sb.append(previousGuess).append(" ");
            }
            sb.append("\n");
            sb.append("Remaining guesses are: ").append("\n");
            for(Character remainingGuess : this.remainingGuesses) {
                sb.append(remainingGuess).append(" ");
            }
            sb.append("\n");
            return sb.toString();
        }
    }

    protected static class WheelOfFortuneGuess extends Guess {
        public Character value;

        public WheelOfFortuneGuess(Character value) {
            this.value = value;
        }
    }

    private final List<String> phrases = getPhrases();
    private Integer nextPhrase = 0;
    private final List<WheelOfFortunePlayer> players = new ArrayList<>();
    private Integer nextPlayer = 0;
    private WheelOfFortunePlayer activePlayer;

    private static List<String> getPhrases() {
        try {
            BufferedReader phraseReader = new BufferedReader(new FileReader("resources/phrases.txt"));
            List<String> phrases = new ArrayList<>(phraseReader.lines().toList());
            Collections.shuffle(phrases);
            return phrases;
        } catch(Exception exception) {
            System.out.println("EXCEPTION IN GETTING PHRASES: " + exception);
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        System.out.println("PLAYER GAME:");
        Game player = new WheelOfFortune(new WheelOfFortuneProbablePlayer());
        AllGamesRecord playerRecord = player.playAll();
        System.out.println("Average score: " + playerRecord.average());
        System.out.println("3 highest scores: ");
        for(GameRecord highGame : playerRecord.highGameList(3)) {
            System.out.println("\t" + highGame);
        }
        for(String playerID : playerRecord.playerGameRecords.keySet()) {
            System.out.println(playerID + "'s average score: " + playerRecord.average(playerID));
            System.out.println(playerID + "'s 3 highest scores: ");
            for(GameRecord highGame : playerRecord.highGameList(playerID, 3)) {
                System.out.println("\t" + highGame);
            }
        }


        System.out.println("AI GAME:");
        Game ai = new WheelOfFortune(List.of(new WheelOfFortuneRandomPlayer(), new WheelOfFortuneProbablePlayer(), new WheelOfFortuneEntropicPlayer()));
        ai.playAll();
        System.out.println("Average score: " + playerRecord.average());
        System.out.println("3 highest scores: ");
        for(GameRecord highGame : playerRecord.highGameList(3)) {
            System.out.println("\t" + highGame);
        }
        for(String playerID : playerRecord.playerGameRecords.keySet()) {
            System.out.println(playerID + "'s average score: " + playerRecord.average(playerID));
            System.out.println(playerID + "'s 3 highest scores: ");
            for(GameRecord highGame : playerRecord.highGameList(playerID, 3)) {
                System.out.println("\t" + highGame);
            }
        }
    }

    public WheelOfFortune() {
        this.players.add(new WheelOfFortuneHumanPlayer());
        this.activePlayer = this.players.get(this.nextPlayer++);
    }

    public WheelOfFortune(WheelOfFortunePlayer player) {
        this.players.add(player);
        this.activePlayer = this.players.get(this.nextPlayer++);
    }

    public WheelOfFortune(List<WheelOfFortunePlayer> players) {
        this.players.addAll(players);
        this.activePlayer = this.players.get(this.nextPlayer++);
    }

    @Override
    protected GameState initialGameState() {
        return new WheelOfFortuneGameState(this.phrases.get(this.nextPhrase++));
    }

    @Override
    protected Guess getGuess(GameState gameState) {
        assert gameState instanceof WheelOfFortuneGameState;
        return this.activePlayer.nextGuess((WheelOfFortuneGameState) gameState);
    }

    @Override
    protected void processGuess(GameState gameState, Guess guess) {
        assert gameState instanceof WheelOfFortuneGameState;
        assert guess instanceof WheelOfFortuneGuess;
        ((WheelOfFortuneGameState) gameState).processGuess((WheelOfFortuneGuess) guess);
    }

    @Override
    protected String getPlayerID() {
        return this.activePlayer.playerID();
    }

    @Override
    protected Integer getScore(GameState gameState) {
        return 10 - gameState.numberOfGuesses;
    }

    @Override
    public Boolean playNext() {
        if(this.nextPhrase < this.phrases.size()) {
            return this.activePlayer.playNext();
        } else {
            if(this.nextPlayer < this.players.size()) {
                this.nextPhrase = 0;
                this.activePlayer = this.players.get(this.nextPlayer++);
                return this.activePlayer.playNext();
            } else {
                return false;
            }
        }
    }
}