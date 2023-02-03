import java.util.ArrayList;

public class SidePot {
    private ArrayList<Player> players;

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    private int pot;

    private int bet;
    public int getBet() {
        return bet;
    }



    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    private int numPlayers;



    public SidePot(int bet, ArrayList<Player> p){
        players = new ArrayList<Player>();
        for (int i = 0; i < p.size(); i++){
            players.add(p.get(i));
        }
        pot = bet;
        numPlayers = p.size();
    }
    public SidePot(int bet, Player p){
        players = new ArrayList<Player>();
        players.add(p);
        this.bet = bet;
        this.pot = bet;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }
}
