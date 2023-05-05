import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
public class Deck {


    private boolean hasTwoPair;

    private boolean hasThreePair;

    private boolean hasFullHouse;

    private boolean hasFourPair;

    private int pointTotal;
    //this is going to run setPoints too many times.
    public int getPoints() {
        setPoints();
        return points;
    }

    private int points;

    private boolean hasFlush;
    private boolean hasStraight;

    String name;
    private ArrayList<Card> cards;
    public void setCardsLeft(int cardsLeft) {
        this.cardsLeft = cardsLeft;
    }
    private int cardsLeft;
    private static final ArrayList<Integer> POINT_ORDER = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,
                                                                            11,20,37,70,135,264,517));
    private static final ArrayList<String> RANK_ORDER = new ArrayList<>(Arrays.asList("2", "3", "4","5","6","7","8","9",
                                                                                "10","Jack", "Queen", "King", "Ace"));
    public Deck(String[] rank, String[] suits, int[] point){
        cards = new ArrayList<Card>();
        cardsLeft = rank.length * suits.length;
        int imageOrder = 0;
        for (int i = 0; i < rank.length; i++) {
            for (int j = 0; j < suits.length; j++) {
                Card c = new Card(rank[i], suits[j], point[i], new ImageIcon("Resources/Cards/" + Integer.toString(imageOrder) + ".png").getImage());
                imageOrder++;
                cards.add(c);
            }
        }

    }
    public Deck(ArrayList<Card> c){
        cards = new ArrayList<Card>();
        for (int i = 0; i < c.size(); i++)
            cards.add(c.get(i));
    }
    public Deck(){
        cards = new ArrayList<Card>();
    }
    public ArrayList<Card> getCards(){
        return cards;
    }
    public void burn(){
        cardsLeft--;
    }
    public int getSize(){
        return cards.size();
    }
    public void add(Card c){
        cards.add(c);
    }
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    public boolean isEmpty(){
        return cardsLeft == 0;
    }
    public int getCardsLeft() {
        return cardsLeft;
    }
    public Card deal() {
        if (cards.isEmpty())
            return null;
        cardsLeft--;
        return cards.get(cardsLeft);

    }
    public void shuffle(){
        cardsLeft = cards.size();
        for (int i = cardsLeft - 1; i > 0; i--){
            int r = (int)(Math.random() * (i + 1));
            Card c = cards.get(i);
            c = cards.set(r, c);
            cards.set(i, c);
        }
    }
    private void setPoints(){
        sort();
        if (hasStraight() && hasFlush()){
            points = 61928 + RANK_ORDER.indexOf(cards.get(0).getRank());
            return;
        }
        for (int i = 0; i < 5; i++) {
            pointTotal += cards.get(i).getPoint();
        }
        int[] pairs = hasPairs();
        if (pairs != null){
            if (hasFourPair){
                points = 55447 + pairs[0] * 518 + pointTotal - (POINT_ORDER.get(pairs[0]) * 4);
            }
            else if (hasFullHouse){
                points = 55265 + pairs[0] * 14 + pairs[1];
            }
            else if (hasFlush){
                points = 55259 + pointTotal;
            }
            else if (hasStraight){
                points = 55264 + RANK_ORDER.indexOf(cards.get(0).getRank());
            }
            else if (hasThreePair){
                points = 45610 + pairs[0] * 782 + pointTotal - (POINT_ORDER.get(pairs[0]) * 3);
                if (pairs[0] > 11)
                    points = points - POINT_ORDER.get(11) + 135;

            }
            else if (hasTwoPair){
                int multiplier = 0;
                for (int i = 0; i < pairs[0] - 1; i++){
                    multiplier += (pairs[0] - 1 - i);
                }
                multiplier += (pairs[1] + 1);
                points = 11422 + multiplier * 518;
            }
            else {
                points = 1001 + pairs[0] * 911 + pointTotal - POINT_ORDER.get(pairs[0]) * 2;
                if (pairs[0] > 10)
                    points = points - POINT_ORDER.get(pairs[0]) + 70;

            }
        }
        else if (hasFlush){
            points = 55259 + pointTotal;
        }
        else if (hasStraight){
            points = 55264 + RANK_ORDER.indexOf(cards.get(0).getRank());
        }
        else
            points = pointTotal;

    }


    public void sort() {
        for (int i = 0; i < cards.size(); i++){
            int min = Integer.MAX_VALUE;
            int minPlace = 0;
            for (int j = i; j < cards.size(); j++) {
                if (cards.get(j).getPoint() <= min) {
                    min = cards.get(j).getPoint();
                    minPlace = j;
                }
            }
            Card c = cards.remove(minPlace);
            cards.add(i, c);
        }
    }
    public int[] hasPairs(){
        int current = 0;
        int[] pairs = new int[2];
        for (int i = 0; i < cards.size() - 1; i++){
            int value = cards.get(i).getPoint();
            if (i < cards.size() - 3 && value == cards.get(i + 1).getPoint() && value == cards.get(i + 2).getPoint() &&
                                                                                value == cards.get(i + 3).getPoint()){
                hasFourPair = true;
                pairs[current] = RANK_ORDER.indexOf(cards.get(i).getRank());
                return pairs;
            }

            if (i < cards.size() - 2 && value == cards.get(i + 1).getPoint() && value == cards.get(i + 2).getPoint()) {
                if (current == 1){
                    hasFullHouse = true;
                    int swap = pairs[0];
                    pairs[0] = RANK_ORDER.indexOf(cards.get(i).getRank());
                    pairs[1] = swap;
                    return pairs;
                }
                pairs[0] = RANK_ORDER.indexOf(cards.get(i).getRank());
                hasThreePair = true;
                current++;
                i += 2;
            }
            else if (value == cards.get(i + 1).getPoint()){
                 if (current == 1)
                 {
                     if (hasThreePair) {
                         hasFullHouse = true;
                         pairs[current] = RANK_ORDER.indexOf(cards.get(i).getRank());
                         return pairs;
                     }
                     else {
                         if (pairs[0] < RANK_ORDER.indexOf(cards.get(i).getRank())) {
                             int swap = pairs[0];
                             pairs[0] = RANK_ORDER.indexOf(cards.get(i).getRank());
                             pairs[1] = swap;
                         }
                         hasTwoPair = true;
                         return pairs;
                     }
                 }
                 else {
                     pairs[0] = RANK_ORDER.indexOf(cards.get(i).getRank());
                     current++;
                 }

                 i++;
            }
        }
        if (current == 1){
            return pairs;
        }
        return null;
    }

    public boolean hasFlush(){
        String s = cards.get(0).getSuit();
        if (s.equals(cards.get(1).getSuit()) && s.equals(cards.get(2).getSuit()) && s.equals(cards.get(3).getSuit()) &&
                                                                                    s.equals(cards.get(4).getSuit())) {
            hasFlush = true;
            return true;
        }
        return false;
    }
    public boolean hasStraight(){
        int value = RANK_ORDER.indexOf(cards.get(0).getRank());
        for (int i = 1; i < 5; i++){
            if (value + i != RANK_ORDER.indexOf(cards.get(i).getRank()))
                return false;
        }
        hasStraight = true;
        return true;
    }




}
