import java.awt.Image;
public class Card {
    private final String rank;
    private final String suit;
    private final int point;
    private final Image image;

    public Card(String rank, String suit, int point, Image image){
        this.rank = rank;
        this.suit = suit;
        this.point = point;
        this.image = image;
    }
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
