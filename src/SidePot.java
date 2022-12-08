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

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    private int numPlayers;



    public SidePot(int money, ArrayList<Player> p){
        players = new ArrayList<Player>();
        for (int i = 0; i < p.size(); i++){
            if (!p.get(i).isElim())
                players.add(p.get(i));
        }
        pot = money;
        numPlayers = p.size();
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }
}
