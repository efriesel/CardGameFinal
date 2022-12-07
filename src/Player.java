import javax.swing.text.AbstractDocument;
import java.nio.file.AccessDeniedException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    private Deck hand;

    private Deck bestHand;

    private Deck currentHand;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    private int money;
    private int points;

    private String name;




    private boolean elim;

    public Player(String name, int money) {;
        this.name = name;
        this.money = money;
        points = 0;
        elim = false;
    }

    public Deck getBestHand(Deck hand, Deck river){
        Deck bestHand = new Deck();
        int bestPoints = 0;
        int points;
        for (int i = 0; i < river.getSize() - 2; i++){
            currentHand = new Deck();
            currentHand.add(hand.getCards().get(0));
            currentHand.add(hand.getCards().get(1));
            for (int j = i + 1; j < river.getSize() - 1; j++){
                for (int k = j + 1; k < river.getSize(); k++){
                    currentHand.add(river.getCards().get(i));
                    currentHand.add(river.getCards().get(j));
                    currentHand.add(river.getCards().get(k));
                    points = currentHand.getPoints();
                    if (bestPoints < points){
                        bestPoints = points;
                        bestHand = new Deck();
                        for (int l = 0; l < 5; l++){
                            bestHand.add(currentHand.getCards().get(i));
                        }
                    }
                }
            }
        }
        return bestHand;
    }

//    public Player(String name, ArrayList<Card> deck){
//        this.name = name;
//        hand = new Deck(deck);
//
//    }
    public void setDeck(ArrayList<Card> deck){
        hand = new Deck(deck);
    }
    public String getName(){
        return name;
    }

    public Deck getHand() {
        return hand;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int add){
        points += add;
    }

    public boolean isElim() {
        return elim;
    }

    public void setElim(boolean elim) {
        this.elim = elim;
    }


    public void printHand(){
        System.out.print("[ ");
        for (Card c : hand.getCards()){
            System.out.print(c);
            System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Press enter to continue");
    }
    public static void printHand(Deck d){
        Scanner s = new Scanner(System.in);
        System.out.print("[ ");
        for (Card c : d.getCards()){
            System.out.print(c);
            System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Press enter to continue");
        s.nextLine();
    }

    public void addCard(Card c){
        hand.add(c);
    }

    public String toString(){
        return name;
    }
}
