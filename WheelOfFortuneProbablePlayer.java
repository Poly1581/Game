import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class WheelOfFortuneProbablePlayer implements WheelOfFortunePlayer {

    Map<Character, Double> characterFrequencies;

    public static Map<Character, Double> getCharacterFrequencies() {
        Map<Character, Double> characterFrequencies = new HashMap<>();
        characterFrequencies.put('a', 0.082);
        characterFrequencies.put('b', 0.015);
        characterFrequencies.put('c', 0.028);
        characterFrequencies.put('d', 0.043);
        characterFrequencies.put('e', 0.127);
        characterFrequencies.put('f', 0.022);
        characterFrequencies.put('g', 0.020);
        characterFrequencies.put('h', 0.061);
        characterFrequencies.put('i', 0.070);
        characterFrequencies.put('j', 0.0015);
        characterFrequencies.put('k', 0.0077);
        characterFrequencies.put('l', 0.040);
        characterFrequencies.put('m', 0.024);
        characterFrequencies.put('n', 0.067);
        characterFrequencies.put('o', 0.075);
        characterFrequencies.put('p', 0.019);
        characterFrequencies.put('q', 0.00095);
        characterFrequencies.put('r', 0.060);
        characterFrequencies.put('s', 0.063);
        characterFrequencies.put('t', 0.091);
        characterFrequencies.put('u', 0.028);
        characterFrequencies.put('v', 0.0098);
        characterFrequencies.put('w', 0.024);
        characterFrequencies.put('x', 0.0015);
        characterFrequencies.put('y', 0.020);
        characterFrequencies.put('z', 0.00074);
        return characterFrequencies;
    }

    public WheelOfFortuneProbablePlayer() {
        reset();
    }

    @Override
    public WheelOfFortune.WheelOfFortuneGuess nextGuess(WheelOfFortune.WheelOfFortuneGameState gameState) {
        Double maxFrequency = Double.NEGATIVE_INFINITY;
        Character guess = null;
        for(Character remainingGuess : gameState.remainingGuesses) {
            Double guessFrequency = characterFrequencies.get(remainingGuess);
            if(guessFrequency > maxFrequency) {
                maxFrequency = guessFrequency;
                guess = remainingGuess;
            }
        }
        return new WheelOfFortune.WheelOfFortuneGuess(guess);
    }

    @Override
    public Boolean playNext() {
        return true;
    }

    @Override
    public String playerID() {
        return "Probable Player";
    }

    @Override
    public void reset() {
        this.characterFrequencies = getCharacterFrequencies();
    }
}
