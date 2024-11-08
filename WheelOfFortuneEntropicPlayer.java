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
        return null;
    }

    @Override
    public String playerID() {
        return "";
    }

    @Override
    public void reset() {
        bot = null;
    }
}
