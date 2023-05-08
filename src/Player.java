import javax.swing.text.AbstractDocument;
import java.nio.file.AccessDeniedException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.awt.Graphics;

public class Player {
    private String name;
    private int bestPoints;
    private Deck hand;
    private Deck currentHand;
    private Deck bestHand;
    private int money;
    private int inputtedMoney;
    private int currentInputtedMoney;
    private boolean elim;


    public Player(String name, int money) {
        this.name = name;
        this.money = money;
        elim = false;
        hand = new Deck();
        bestPoints = 0;
    }
    public Player(String name, ArrayList<Card> deck){
        this.name = name;
        hand = new Deck(deck);
    }

    public void getBestHand(ArrayList<Card> river) {
        bestHand = new Deck();
        int points;
        bestPoints = 0;
        for (int i = 0; i < river.size() - 2; i++) {
            for (int j = i + 1; j < river.size() - 1; j++) {
                for (int k = j + 1; k < river.size(); k++) {
                    currentHand = new Deck();
                    currentHand.add(hand.getCards().get(0));
                    currentHand.add(hand.getCards().get(1));
                    currentHand.add(river.get(i));
                    currentHand.add(river.get(j));
                    currentHand.add(river.get(k));
                    points = currentHand.getPoints();
                    if (bestPoints < points) {
                        bestPoints = points;
                        bestHand = new Deck();
                        ArrayList<Card> c = new ArrayList<>();
                        c.addAll(currentHand.getCards());
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
        g.drawString(winStatement, GameViewer.WINDOW_WIDTH / 2 - GameViewer.SMALL_FONT.getSize() * winStatement.length() / 4, GameViewer.Y_HEIGHT / 2);
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
}
