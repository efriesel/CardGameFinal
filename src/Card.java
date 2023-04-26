import java.awt.Image;
public class Card {
    private static final String[] SUITS = {"Spades", "Clubs", "Hearts", "Diamonds"};
    private String rank;
    private String suit;
    private int point;
    private Image image;

    public Card(String rank, String suit, int point, Image image){
        this.rank = rank;
        this.suit = suit;
        this.point = point;
        this.image = image;
    }
    public String getRank() {
        return rank;
    }
    public String getSuit() {
        return suit;
    }
    public int getPoint() {
        return point;
    }
    public String toString(){
        return rank + " of " + suit;
    }
}
