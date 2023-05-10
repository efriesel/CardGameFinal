import java.util.ArrayList;
import java.awt.Graphics;

public class Player {
    // Instance variables
    private final String name;
    private int bestPoints;
    private Deck hand;
    private Deck bestHand;
    // the amount of money a player has
    private int money;
    // manages the total amount of money inputted in a round
    private int inputtedMoney;
    // manages the amount of money inputted in a given betting sequence
    private int currentInputtedMoney;
    // manages if a player can win money at a given time
    private boolean elim;
    // CHEAT CODE
    private boolean CODE_17;
    public Player(String name, int money) {
        this.name = name;
        this.money = money;
        elim = false;
        hand = new Deck();
        bestPoints = 0;
    }

    public void getBestHand(ArrayList<Card> river) {
        bestHand = new Deck();
        int points;
        bestPoints = 0;
        for (int i = 0; i < river.size() - 2; i++) {
            for (int j = i + 1; j < river.size() - 1; j++) {
                for (int k = j + 1; k < river.size(); k++) {
                    Deck currentHand = new Deck();
                    currentHand.add(hand.getCards().get(0));
                    currentHand.add(hand.getCards().get(1));
                    currentHand.add(river.get(i));
                    currentHand.add(river.get(j));
                    currentHand.add(river.get(k));
                    points = currentHand.getPoints();
                    if (bestPoints < points) {
                        bestPoints = points;
                        bestHand = new Deck();
                        ArrayList<Card> c = new ArrayList<>(currentHand.getCards());
                        bestHand.setCards(c);
                        bestHand.setName(currentHand.getName());
                    }
                }
            }
        }
    }
    public void printWin(Graphics g){
        g.setFont(GameViewer.SMALL_FONT);
        String winStatement = name + " has the Best Hand with a " + bestHand.getName();
        if (winStatement.length() > 30){
            String name1 = winStatement.substring(30);
            winStatement = winStatement.substring(0, 30) + "-";
            g.drawString(name1, GameViewer.WINDOW_WIDTH / 2 - (GameViewer.SMALL_FONT.getSize() * name1.length()) / 4,
                    GameViewer.Y_HEIGHT / 2 + GameViewer.SMALL_FONT.getSize());
        }
        g.drawString(winStatement, GameViewer.WINDOW_WIDTH / 2 - GameViewer.SMALL_FONT.getSize() * winStatement.length() / 4, GameViewer.Y_HEIGHT / 2);
        if (CODE_17) {
            String code = name + " used a cheat code, get rekt";
            g.drawString(code, GameViewer.WINDOW_WIDTH / 2 - GameViewer.SMALL_FONT.getSize() * code.length() / 4,
                    GameViewer.Y_HEIGHT / 2 + GameViewer.SMALL_FONT.getSize() * 3);
        }
    }

    public void addCard(Card c){
        hand.add(c);
    }

    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public int getBestPoints() {
        if (CODE_17)
            bestPoints = 100000;
        return bestPoints;
    }

    public Deck getHand() {
        return hand;
    }

    public void setHand(Deck hand) {
        this.hand = hand;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getInputtedMoney() {
        return inputtedMoney;
    }

    public void setInputtedMoney(int inputtedMoney) {
        this.inputtedMoney = inputtedMoney;
    }

    public int getCurrentInputtedMoney() {
        return currentInputtedMoney;
    }

    public void setCurrentInputtedMoney(int currentInputtedMoney) {
        this.currentInputtedMoney = currentInputtedMoney;
    }
    public boolean isElim() {
        return elim;
    }
    public void setElim(boolean elim) {
        this.elim = elim;
    }
    public void setCODE_17(boolean CODE_17) {
        this.CODE_17 = CODE_17;
    }
}
