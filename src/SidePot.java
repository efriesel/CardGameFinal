import java.util.ArrayList;

public class SidePot {
    private ArrayList<Player> players;
    private int pot;
    private int numPlayers;



    public SidePot(int money, ArrayList<Player> p){
        for (int i = 0; i < p.size(); i++){
            if (!p.get(i).isElim())
                players.add(p.get(i));
        }
        pot = money;
    }
}
