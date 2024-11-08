public class TrieBot {
    private int numWords;
    private char previousGuess = '*';
    private String previousGuesses = "";
    private int[] lengths;
    private nTrieHead[] tries;
    private String[] hiddenWords;

    public static void main(String[] args) {
        TrieBot test = new TrieBot("**** ** *** ***");
        for(int i = 0; i < test.tries.length; i++) {
            System.out.println("Word has " + test.lengths[i] + " letters");
            System.out.println("There are " + test.tries[i].countWords() + " words");
        }
        test.previousGuess = 'a';
        test.update("*a** ** *** a**");
        System.out.println("AFTER UPDATE:-----------------------------------");
        for(int i = 0; i < test.tries.length; i++) {
            System.out.println("Word has " + test.lengths[i] + " letters");
            System.out.println("There are " + test.tries[i].countWords() + " words");
        }
    }

    /**
     * Constructor - splits hidden phrase into hidden words, constructs corresponding tries, and initializes them.
     * @param hiddenPhrase the hidden phrase to construct the bot with
     */
    public TrieBot(String hiddenPhrase) {
        hiddenWords = hiddenPhrase.split(" ");
        numWords = hiddenWords.length;
        tries = new nTrieHead[numWords];
        lengths = new int[numWords];
        for(int i = 0; i < numWords; i++) {
            String hiddenWord = hiddenWords[i];
            lengths[i] = hiddenWord.length();
            tries[i] = new nTrieHead(hiddenWords[i]);
        }
    }

    /**
     * Split the hidden phrase into hidden words and update each trie with its corresponding hidden word
     * @param hiddenPhrase the hidden phrase after a guess
     */
    public void update(String hiddenPhrase) {
        hiddenWords = hiddenPhrase.split(" ");
        for(int i = 0; i < numWords; i++) {
            tries[i].update(previousGuess, hiddenWords[i]);
        }
    }

    /**
     * Get the index of child corresponding to char c
     * @param c the character whose child index is to be returned
     * @return the index of child c
     */
    private int getCharIndex(char c) {
        c = Character.toLowerCase(c);
        return (int) c - (int)'a';
    }

    /**
     * Update previous guess and previous guesses.
     * @param guess the character to be guessed
     */
    private void updateGuesses(char guess) {
        previousGuess = guess;
        previousGuesses += guess;
    }

    /**
     * Get the (hopefully) optimal guess based on the forest. First consider un-guessed characters in known words,
     * then consider which character decreases forest entropy most out of those characters remaining.
     */
    public char getGuess() {
        // Look for known characters and guess them first
        for(nTrieHead word : tries) {
            if(word.countWords() == 1) {
                String possibleLetters = word.getPossibleLetters();
                for(int i = 0; i < possibleLetters.length(); i++) {
                    char guess = possibleLetters.charAt(i);
                    if(previousGuesses.indexOf(guess) == -1) {
                        updateGuesses(guess);
                        return guess;
                    }
                }
            }
        }

        // Find best guess from remaining un-guessed characters based on maximum entropy reduction
        double[] entropies = new double[26];
        for(nTrieHead word : tries) {
            String possibleLetters = word.getPossibleLetters();
            for(int i = 0; i < possibleLetters.length(); i++) {
                char possibleLetter = possibleLetters.charAt(i);
                if(previousGuesses.indexOf(getCharIndex(possibleLetter)) == -1) {
                    entropies[getCharIndex(possibleLetter)] += word.getEntropy(possibleLetter);
                }
            }
        }
        char bestGuess = ' ';
        double bestEntropy = Double.MIN_VALUE;
        for(char c = 'a'; c <= 'z'; c++) {
            if(entropies[getCharIndex(c)] > bestEntropy) {
                bestEntropy = entropies[getCharIndex(c)];
                bestGuess = c;
            }
        }
        updateGuesses(bestGuess);
        return bestGuess;
    }
}
