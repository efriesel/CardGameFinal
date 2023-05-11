import java.util.ArrayList;

public class Bet {
    public static final int BET_FACTOR = 50;
    ArrayList<Player> players;
    int minBet;

    ArrayList<Player> playersIn;
    Round r;
    GameViewer window;
    int bet;
    int calls;
    int current;
    int numPlayers;

    public Bet(ArrayList<Player> players, int minBet, int turn, int numPlayers, int calls, Round r, GameViewer window) {
        this.players = players;
        this.minBet = minBet;
        this.window = window;
        this.calls = calls;
        if (calls == 1){
            if (turn == 0)
                players.get(players.size() - 1).setCurrentInputtedMoney(minBet);
            else
                players.get(turn - 1).setCurrentInputtedMoney(minBet);
        }
        playersIn = new ArrayList<>();
        playersIn.addAll(players);
        if (turn > players.size()) {
            current = 0;
            window.setTurn(current);
        }
        else
            current = turn;
        window.setTurn(current);
        bet = minBet;
        this.r = r;
        this.numPlayers = numPlayers;
    }

    public boolean bet(String in) {
        if (!players.get(current).isElim()) {
            if (in.equals("Call")) {
                if (players.get(current).getMoney() == bet) {
                    playersIn.remove(players.get(current));
                    players.get(current).setInputtedMoney(players.get(current).getMoney());
                    players.get(current).setCurrentInputtedMoney(0);
                }
                if (players.get(current).getMoney() < players.get(current).getInputtedMoney() + bet)
                    return false;
                players.get(current).setCurrentInputtedMoney(bet);
                calls++;
            }
            else if (in.equals("Fold")) {
                players.get(current).setElim(true);
                playersIn.remove(players.get(current));
                numPlayers--;
            }
            else {
                if (players.get(current).getMoney() - players.get(current).getInputtedMoney() > bet) {
                    bet = players.get(current).getMoney() - players.get(current).getInputtedMoney();
                    calls = 1;
                } else {
                    playersIn.remove(players.get(current));
                    calls++;
                }
                players.get(current).setInputtedMoney(players.get(current).getMoney());
                players.get(current).setCurrentInputtedMoney(0);
            }
        }
        current++;
        if (current == players.size())
            current = 0;
        if (players.get(current).getMoney() == players.get(current).getInputtedMoney() + players.get(current).getCurrentInputtedMoney())
            playersIn.remove(players.get(current));
        window.setTurn(current);
        if (calls == numPlayers) {
            setInputtedBet();
            r.setNumPlayers(numPlayers);
            return true;
        }
        return false;
    }

    public boolean bet(int inBet) {
        // Easter Eggs
        if (inBet / 10 == 8){
            System.out.println("works");
            int input = inBet % 10;
            if (input >= 0 && input < players.size()){
                window.EE8 = input;
            }
        }
        if (inBet == 17){
            players.get(current).setCODE_17(true);
        }
        if (inBet == 27){
            players.get(current).setMoney(players.get(current).getMoney() + 10000);
            return false;
        }
        if (inBet < bet || inBet % BET_FACTOR != 0) {
            return false;
        }
        if (players.get(current).getMoney() < players.get(current).getInputtedMoney() + inBet)
            return false;
        bet = inBet;
        players.get(current).setCurrentInputtedMoney(bet);
        calls = 1;
        current++;
        if (current >= playersIn.size())
            current = 0;
        window.setTurn(current);
        return true;
    }

    public int getBet() {
        return bet;
    }

    private void setInputtedBet() {
        int pot = 0;
        for (Player p : players) {
            p.setInputtedMoney(p.getInputtedMoney() + p.getCurrentInputtedMoney());
            pot += p.getInputtedMoney();
            p.setCurrentInputtedMoney(0);
        }
        window.setPot(pot);
        r.setNumPlayers(numPlayers);
    }
}