public class WheelOfFortuneEntropicPlayer implements WheelOfFortunePlayer {
    private TrieBot bot = null;

    @Override
    public WheelOfFortune.WheelOfFortuneGuess nextGuess(WheelOfFortune.WheelOfFortuneGameState gameState) {
        if(bot == null) {
            bot = new TrieBot(gameState.hiddenPhrase);
        } else {
            bot.update(gameState.hiddenPhrase);
        }
        return new WheelOfFortune.WheelOfFortuneGuess(bot.getGuess());
    }

    @Override
    public Boolean playNext() {
        return true;
    }

    @Override
    public String playerID() {
        return "Entropic Player";
    }

    @Override
    public void reset() {
        bot = null;
    }
}
