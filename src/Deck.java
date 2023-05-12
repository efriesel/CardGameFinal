import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
public class Deck {
    //constants
    private static final ArrayList<Integer> POINT_ORDER = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,
            11,20,37,70,135,264,517));
    private static final ArrayList<String> RANK_ORDER = new ArrayList<>(Arrays.asList("2", "3", "4","5","6","7","8","9",
            "10","Jack", "Queen", "King", "Ace"));
    // instance variables for the point value of the hand
    private boolean hasTwoPair;
    private boolean hasThreePair;
    private boolean hasFullHouse;
    private boolean hasFourPair;
    private boolean hasFlush;
    private boolean hasStraight;
    // the total amount of points for the hand
    private int points;
    // the amount of points for the cards in the hand just based on high card
    private int pointTotal;
    // the name of the hand (ex. Ace High or Pair of Three's)
    private String name;
    // the ArrayList of cards
    private ArrayList<Card> cards;
    // the number of cards left (used with main deck)
    private int cardsLeft;
    /**
     * Constructor for creating a deck of cards with card creation
     * @param rank the array of ranks
     * @param suits the array of suits
     * @param point the array of point values
     */
    public Deck(String[] rank, String[] suits, int[] point){
        cards = new ArrayList<>();
        // number of cards in the deck at the start (52 in this case )
        cardsLeft = rank.length * suits.length;
        // manages the order of the images for image implementation
        int imageOrder = 0;
        // use a nested for loop to add a card of each rank for each suit
        for (int i = 0; i < rank.length; i++) {
            for (String suit : suits) {
                Card c = new Card(rank[i], suit, point[i],
                        new ImageIcon("Resources/Cards/" + imageOrder + ".png").getImage());
                imageOrder++;
                cards.add(c);
            }
        }
        // set the name of the deck to nothing (will not be used)
        name = "";
    }
    /**
     * Second Constructor (used more) for a hand Deck
     * only initializes cards
     */
    public Deck(){
        cards = new ArrayList<>();
    }

    /**
     * Gets the amount of cards in a deck of a 5 card hand
     * @return the amount of cards
     */
    public int getPoints() {
        setPoints();
        return points;
    }
    // Getter and Setter methods
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public ArrayList<Card> getCards(){
        return cards;
    }
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    public int getSize(){
        return cards.size();
    }
    public void setCardsLeft(int cardsLeft) {
        this.cardsLeft = cardsLeft;
    }
    /**
     * Burn method (skips current card
     */
    public void burn(){
        cardsLeft--;
    }
    /**
     * Adds a card to the deck
     * @param c Card
     */
    public void add(Card c){
        cards.add(c);
    }
    /**
     * This method will deal the first card of the deck then move to the next card
     * @return Card
     */
    public Card deal() {
        if (cards.isEmpty())
            return null;
        cardsLeft--;
        return cards.get(cardsLeft);
    }
    /**
     * This method will shuffle the deck of cards using the Math.random() function and swaping of cards
     */
    public void shuffle(){
        cardsLeft = cards.size();
        for (int i = cardsLeft - 1; i > 0; i--){
            int r = (int)(Math.random() * (i + 1));
            Card c = cards.get(i);
            c = cards.set(r, c);
            cards.set(i, c);
        }
    }
    /**
     * This function will set the value of a hand based on point value
     * Scale Probability used to give point values
     */
    private void setPoints(){
        sort();
        if (hasStraight() && hasFlush()){
            if (cards.get(cards.size() - 1).getPoint() == POINT_ORDER.get(POINT_ORDER.size() - 1))
                name = "Royal Flush of " + cards.get(0).getSuit();
            else
                name = "Straight Flush of " + cards.get(0).getSuit();
            points = 61928 + RANK_ORDER.indexOf(cards.get(0).getRank());
            return;
        }
        for (int i = 0; i < 5; i++) {
            pointTotal += cards.get(i).getPoint();
        }
        int[] pairs = hasPairs();
        if (pairs != null){
            if (hasFourPair){
                name = "Four Pair of " + RANK_ORDER.get(pairs[0]) + "'s";
                points = 55447 + pairs[0] * 518 + pointTotal - (POINT_ORDER.get(pairs[0]) * 4);
            }
            else if (hasFullHouse){
                name = "Full House of trip " + RANK_ORDER.get(pairs[0]) + "'s and dub " + RANK_ORDER.get(pairs[1]) +
                        "'s";
                points = 55265 + pairs[0] * 14 + pairs[1];
            }
            else if (hasThreePair){
                name = "Three Pair of " + RANK_ORDER.get(pairs[0]) + "'s";
                points = 45610 + pairs[0] * 782 + pointTotal - (POINT_ORDER.get(pairs[0]) * 3);
                if (pairs[0] > 11)
                    points = points - POINT_ORDER.get(11) + 135;

            }
            else if (hasTwoPair){
                name = "Two Pair of " + RANK_ORDER.get(pairs[0]) + "'s and " + RANK_ORDER.get(pairs[1]) + "'s";
                int multiplier = 0;
                for (int i = 0; i < pairs[0] - 1; i++){
                    multiplier += (pairs[0] - 1 - i);
                }
                multiplier += (pairs[1] + 1);
                points = 11422 + multiplier * 518;
            }
            else {
                name = "Pair of " + RANK_ORDER.get(pairs[0]) + "'s";
                points = 1001 + pairs[0] * 911 + pointTotal - POINT_ORDER.get(pairs[0]) * 2;
                if (pairs[0] > 10)
                    points = points - POINT_ORDER.get(pairs[0]) + 70;

            }
        }
        else if (hasFlush){
            name = "Flush of " + cards.get(0).getSuit();
            points = 55259 + pointTotal;
        }
        else if (hasStraight){
            name = "Straight";
            points = 55264 + RANK_ORDER.indexOf(cards.get(0).getRank());
        }
        else {
            name = cards.get(cards.size() - 1).getRank() + " High";
            points = pointTotal;
        }
    }
    /**
     * This method will use linear search to sort the cards
     * Although this method is O(n^2), there will only be 5 cards in a deck when this method is called
     */
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

    /**
     * This method will check if a hand of cards has a pair and return a detailed array that explains the contents
     * @return an Array of the indices of the ranks of the cards with a pair iff there is a pair
     * this method will also check if the hand has a fourPair, fullHouse, threePair, or twoPair
        * instance boolean variables will be updated as such
     */
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
                     }
                     else {
                         if (pairs[0] < RANK_ORDER.indexOf(cards.get(i).getRank())) {
                             int swap = pairs[0];
                             pairs[0] = RANK_ORDER.indexOf(cards.get(i).getRank());
                             pairs[1] = swap;
                         }
                         hasTwoPair = true;
                     }
                     return pairs;
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
    /**
     * This method will check if a hand has a flush
     * @return true iff all 5 cards have the same suit value
     */
    public boolean hasFlush(){
        String s = cards.get(0).getSuit();
        if (s.equals(cards.get(1).getSuit()) && s.equals(cards.get(2).getSuit()) && s.equals(cards.get(3).getSuit()) &&
                s.equals(cards.get(4).getSuit())) {
            hasFlush = true;
            return true;
        }
        return false;
    }

    /**
     * this method will check if the hand has a straight
     * @return true iff all the cards consecutively increase in RANK_ORDER indices
     */
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
