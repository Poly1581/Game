import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class nTrieHead extends nTrie {
    private int length;
    private String hiddenWord = "";
    private String unknownCharacters = "abcdefghijklmnopqrstuvwxyz";
    private String includedCharacters = "";
    private String excludedCharacters = "";

    public static void main(String[] args) {
        nTrieHead test = new nTrieHead("******");
        test.update('a', "**a***");
    }

    /**
     * Constuctor - initialize hidden word, length of hidden word, and number of children. Load all words from text file.
     * @param hiddenWord the hidden word (all *'s at the beginning, mostly used for length)
     */
    public nTrieHead(String hiddenWord) {
        this.hiddenWord = hiddenWord;
        this.length = hiddenWord.length();
        this.numChildren = 0;
        loadWords();
    }

    /**
     * Try to load all words of the same length as hiddenWord from corresponding file
     */
    private void loadWords() {
        BufferedReader reader = null;
        try {
            String fileName = "..\\resources\\" + length + "words.txt";
            Path path = Paths.get(fileName);
            reader = Files.newBufferedReader(path);
        } catch (IOException | NullPointerException e) {
            System.out.println(e);
        }

        String word = "";
        boolean eof = false;
        do {
            try {
                word = reader.readLine();
            } catch (IOException e) {
                System.out.println(e);
                eof = true;
            }
            if(word == null || word.isEmpty()) {
                eof = true;
            } else {
                insert(word);
            }
        } while (!eof);
    }

    /**
     * Update the trie - if newHiddenWord is different from hiddenWord the guess must be in the word, otherwise the guess cannot be in the word.
     * Update included, excluded, and unknown characters accordingly. Prune the trie.
     * @param guess the previous guess
     * @param newHiddenWord the new hidden word after (possibly) including guess
     */
    public void update(char guess, String newHiddenWord) {
        if(guess == '*') {
            return;
        }
        if(newHiddenWord.equals(hiddenWord)) {
            excludedCharacters += guess;
        } else {
            includedCharacters += guess;
            hiddenWord = newHiddenWord;
        }
        int unknownGuessIndex = unknownCharacters.indexOf(guess);
        if(unknownGuessIndex != -1) {
            unknownCharacters = unknownCharacters.substring(0, unknownGuessIndex) + unknownCharacters.substring(unknownGuessIndex + 1);
        }
        prune();
    }

    /**
     * Prune the trie - both by word (i.e. only allow words that match known characters in the hidden word) and by characters
     * (i.e. allow no words that include excluded characters).
     */
    private void prune() {
        pruneWord(includedCharacters, hiddenWord);
        pruneCharacters(excludedCharacters);
    }

    /**
     * Get the entropy of the character guess in the trie.
     * @param guess a character whose corresponding trie entropy decrease is to be calculated
     */
    public double getEntropy(char guess) {
        double numWords = countWords();
        double numWordsWithout = countWordsWithout(guess);
        double numWordsWith = numWords - numWordsWithout;
        double probabilityWith = numWordsWith / numWords;
        double probabilityWithout = numWordsWithout / numWords;
        double totalEntropy = getEntropy();
        double entropyWithout = getEntropyWithout(guess);
        double entropyWith = totalEntropy - entropyWithout;
        return totalEntropy - (probabilityWith * entropyWith + probabilityWithout * entropyWithout);
    }

    /**
     * Get all possible letters in the trie (i.e. letters in possible words that have not been guessed yet)
     */
    public String getPossibleLetters() {
        return getPossibleLetters(includedCharacters);
    }
}