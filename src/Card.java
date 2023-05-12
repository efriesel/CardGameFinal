import java.awt.Image;
public class Card {
    // instance variables
    private final String rank;
    private final String suit;
    private final int point;
    private final Image image;

    /**
     * Constructor
     * @param rank: String, the rank of the card
     * @param suit: String, the suit of the card
     * @param point: int, the value of the card
     * @param image: Image, the image correlating to the card
     */
    public Card(String rank, String suit, int point, Image image){
        this.rank = rank;
        this.suit = suit;
        this.point = point;
        this.image = image;
    }
    // getter and setter methods
    public Image getImage(){
        return image;
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
