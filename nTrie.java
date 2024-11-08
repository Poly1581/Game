public class nTrie {
    protected int numChildren;
    protected final int NUMLETTERS = 26;
    protected boolean isTerminal = false;
    protected nTrie[] children = new nTrie[NUMLETTERS];
    protected static double[] letterProbabilities = {0.082, 0.015, 0.028, 0.043, 0.127, 0.022, 0.020, 0.061, 0.070, 0.0015, 0.0077, 0.040, 0.024, 0.067, 0.075, 0.019, 0.00095, 0.060, 0.063, 0.091, 0.028, 0.0098, 0.024, 0.0015, 0.020, 0.00074};

    /**
     * Takes a character and returns the index of the child corresponding to that character
     * @param c a character to be converted to an index
     * @return the index of character c in child array
     */
    protected int getCharIndex(char c) {
        c = Character.toLowerCase(c);
        return (int) c - (int)'a';
    }

    /**
     * Takes a child index and returns the child corresponding to that index
     * @param childIndex the index of the child to be returned
     * @return the child at the childIndex
     */
    protected nTrie getChild(int childIndex) {
        return children[childIndex];
    }

    /**
     * Takes a character corresponding to a child and returns that child.
     * @param childCharacter the character of the child to be returned
     * @return the child corresponding to childCharacter
     */
    protected nTrie getChild(char childCharacter) {
        return getChild(getCharIndex(childCharacter));
    }

    /**
     * Takes a child index and returns the child corresponding to that index
     * @param childIndex the index of a child
     * @return the child at childIndex
     */
    protected boolean hasChild(int childIndex) {
        return getChild(childIndex) != null;
    }

    /**
     * Takes a character and returns the child corresponding to that character.
     * @param childCharacter the character of the child to be returned
     * @return the child corresponding to childCharacter
     */
    protected boolean hasChild(char childCharacter) {
        return getChild(childCharacter) != null;
    }

    /**
     * Initialize a new nTrie and store it at the index given. Return the child.
     * @param childIndex the index at which to create the child
     * @return the new nTrie child
     */
    protected nTrie makeChild(int childIndex) {
        children[childIndex] = new nTrie();
        return getChild(childIndex);
    }

    /**
     * Initialize a new child and store it at the index corresponding to childCharacter
     * @param childCharacter the character according to the child to be created
     * @return the new child
     */
    protected nTrie makeChild(char childCharacter) {
        return makeChild(getCharIndex(childCharacter));
    }

    /**
     * Recursively insert word into trie. Check if node has child corresponding to first character.
     * If so, insert remainder of word into that child, otherwise, make a new child and insert
     * the suffix into that child.
     * @param word the word to be inserted into the trie (in recursive calls, the suffix)
     */
    protected void insert(String word) {
        if(word.isEmpty()) {
            isTerminal = true;
            return;
        }

        char firstChar = word.charAt(0);
        String suffix = word.substring(1);

        if(hasChild(firstChar)) {
            getChild(firstChar).insert(suffix);
        } else {
            numChildren++;
            makeChild(firstChar).insert(suffix);
        }
    }

    /**
     * Prune (remove) words that do not match the word or excluded characters. Children are pruned if:
     *      1) The first character in the word is not * and the child does not correspond to that character.
     *      2) The first character in the word is * and the child corresponds to an excluded character.
     *      3) After pruning, the child has no children and is not terminal (the words in the suffix trie were all removed)
     * @param includedCharacters the characters known to be in the word
     * @param word the word to prune the tree with (normally the hidden word or a substring thereof)
     */
    protected void pruneWord(String includedCharacters, String word) {
        if(word.isEmpty()) {
            return;
        }

        char firstChar = Character.toLowerCase(word.charAt(0));
        String suffix = word.substring(1);
        boolean isCollapsed = firstChar != '*';
        //Prune collapsed letters
        if(isCollapsed) {
            for(char c = 'a'; c <= 'z'; c++) {
                if(c != firstChar) {
                    if(hasChild(c)) {
                        numChildren--;
                        children[getCharIndex(c)] = null;
                    }
                }
            }
        //Prune uncollapsed letters
        } else {
            for(char c = 'a'; c <= 'z'; c++) {
                if(includedCharacters.indexOf(c) != -1) {
                    if(hasChild(c)) {
                        numChildren--;
                        children[getCharIndex(c)] = null;
                    }
                }
            }
        }
        //Recursively prune children
        for(char c = 'a'; c <= 'z'; c++) {
            if(hasChild(c)) {
                nTrie child = getChild(c);
                child.pruneWord(includedCharacters, suffix);
                if(!child.isTerminal && child.numChildren == 0) {
                    numChildren--;
                    children[getCharIndex(c)] = null;
                }
            }
        }
    }

    /**
     * Prune trie according to excluded characters. Prune if:
     *      1) Child character is not in excluded word and is not terminal and ,after pruning, has no children.
     *      2) Child character is in excluded character.
     * @param excludedCharacters the characters known not to be in the word
     */
    protected void pruneCharacters(String excludedCharacters) {
        for(char c = 'a'; c <= 'z'; c++) {
            if(hasChild(c)) {
                if(excludedCharacters.indexOf(c) == -1) {
                    children[getCharIndex(c)].pruneCharacters(excludedCharacters);
                    if(!children[getCharIndex(c)].isTerminal &&  children[getCharIndex(c)].numChildren == 0) {
                        numChildren--;
                        children[getCharIndex(c)] = null;
                    }
                } else {
                    numChildren--;
                    children[getCharIndex(c)] = null;
                }
            }
        }
    }

    /**
     * Recursively count the total number of words that are contained in a trie. If a node is terminal,
     * return 1 word (all words end in a terminal node). If a node is not terminal, find the number of
     * words that each child contains and add them up. Return the sum of words in child nodes of a node.
     * @return the number of words a node contains
     */
    protected int countWords() {
        if(isTerminal) {
            return 1;
        }
        int sum = 0;
        for(nTrie child : children) {
            if(child != null) {
                sum += child.countWords();
            }
        }
        return sum;
    }

    /**
     * Count the number of words without a given character. If a node is terminal, return 1, as all
     * words end in a terminal node. Otherwise, sum the words without the given character for all child
     * nodes that do not correspond to the character guess.
     * @param guess the character to be excluded from the words counted
     * @return the number of words that exclude char guess contained in node.
     */
    protected int countWordsWithout(char guess) {
        if(isTerminal) {
            return 1;
        }
        int sum = 0;
        for(char c = 'a'; c <= 'z'; c++) {
            if(hasChild(c) && c != guess) {
                sum += getChild(c).countWordsWithout(guess);
            }
        }
        return sum;
    }

    /**
     * Get the first word contained in a node (alphabetically). Find the first child that a node has
     * (note: this is guaranteed because pruning removes all nodes without children), and return
     * the character corresponding to that child prepended to the word (or suffix) contained in that child.
     * @return the word (or suffix) contained in the child.
     */
    protected String getWord() {
        String word = "";
        for(char c = 'a'; c <= 'z'; c++) {
            if(hasChild(c)) {
                word += c;
                word += getChild(c).getWord();
                break;
            }
        }
        return word;
    }

    /**
     * Get the probability of a given child (or conversely letter).
     * @return the letter probablity of the given child.
     */
    protected double getProbability(int childIndex) {
        return letterProbabilities[childIndex];
    }

    /**
     * Get the probability of a given character.
     * @return the probability of childCharacter
     */
    protected double getProbability(char childCharacter) {
        return getProbability(getCharIndex(childCharacter));
    }

    /**
     * Compute the total probability of all children (as the sum of their corresponding letters' frequencies
     * @return the sum of frequencies for all children
     */
    protected double totalLetterProbability() {
        double sum = 0;
        for(int i = 0; i < NUMLETTERS; i++) {
            if(hasChild(i)) {
                sum += letterProbabilities[i];
            }
        }
        return sum;
    }

    /**
     * Calculate the entropy of a given node (H = Sum p_i*log_2(p_i))
     * @return the entropy of the node
     */
    protected double getEntropy() {
        double totalEntropy = 0;
        double totalProbability = totalLetterProbability();
        for(int i = 0; i < NUMLETTERS; i++) {
            if(hasChild(i)) {
                double probability = getProbability(i)/totalProbability;
                totalEntropy -= probability * Math.log(probability);
                totalEntropy += getChild(i).getEntropy();
            }
        }
        return totalEntropy;
    }

    /**
     * Calculate the entropy of a node excluding all words containing character guess
     * @param guess a given character
     * @return the entropy of the node without words containing guess
     */
    protected double getEntropyWithout(char guess) {
        double totalEntropy = 0;
        double charIndex = getCharIndex(guess);
        double totalProbability = totalLetterProbability();
        if(hasChild(guess)) {
            totalProbability -= getProbability(guess);
        }
        for(char c = 'a'; c <= 'z'; c++) {
            if(hasChild(c) && c != guess) {
                double probability = getProbability(c)/totalProbability;
                totalEntropy -= probability * Math.log(probability);
                totalEntropy += getChild(c).getEntropyWithout(guess);
            }
        }
        return totalEntropy;
    }

    /**
     * Get all letters in words contained in node
     * @param includedLetters letters included in the hidden word (to be excluded)
     * @return the letters of the words in the node
     */
    protected String getPossibleLetters(String includedLetters) {
        if(isTerminal) {
            return "";
        }
        String possibleLetters = "";
        for(char c = 'a'; c <= 'z'; c++) {
            if(hasChild(c) && includedLetters.indexOf(c) == -1) {
                possibleLetters += c;
            }
        }
        for(nTrie child : children) {
            if(child == null) {
                continue;
            }
            String childPossibleLetters = child.getPossibleLetters(includedLetters);
            for(int i = 0; i < childPossibleLetters.length(); i++) {
                char c = childPossibleLetters.charAt(i);
                if(possibleLetters.indexOf(c) == -1) {
                    possibleLetters += c;
                }
            }
        }
        return possibleLetters;
    }
}