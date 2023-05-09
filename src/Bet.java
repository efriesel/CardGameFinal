import java.util.ArrayList;
import java.util.Scanner;

public class Bet {
    ArrayList<Player> players;
    int minBet;

    ArrayList<Player> playersIn;
    Round r;
    GameViewer window;
    int bet;
    int calls;
    int current;
    int numPlayers;

    public Bet(ArrayList<Player> players, int minBet, int turn, int numPlayers, Round r, GameViewer window) {
        this.players = players;
        this.minBet = minBet;
        this.window = window;
        calls = 0;
        playersIn = new ArrayList<>();
        playersIn.addAll(players);
        current = turn;
        window.setTurn(current);
        bet = minBet;
        this.r = r;
        this.numPlayers = numPlayers;
    }

    public boolean bet(String in) {
        if (in.equals("Call")) {
            if (!playersIn.contains(players.get(current)) && !players.get(current).isElim()) {
                current++;
                window.setTurn(current);
                return false;
            }
            if (players.get(current).getMoney() < players.get(current).getInputtedMoney() + bet)
                return false;
            players.get(current).setCurrentInputtedMoney(bet);
            calls++;
        } else if (in.equals("Fold")) {
            playersIn.get(current).setElim(true);
            playersIn.remove(current);
            numPlayers--;
        } else {
            if (players.get(current).getMoney() - players.get(current).getInputtedMoney() > bet)
                bet = players.get(current).getMoney() - players.get(current).getInputtedMoney();
            else
                playersIn.remove(current);
            players.get(current).setInputtedMoney(players.get(current).getMoney());
        }
        current++;
        if (numPlayers == 1){
            return true;
        }
        if (current == playersIn.size())
            current = 0;
        if (playersIn.size() <= 1) {
            setInputtedBet();
            r.setNumPlayers(numPlayers);
            return true;
        }
        if (playersIn.get(current).getMoney() == playersIn.get(current).getInputtedMoney() + players.get(current).getCurrentInputtedMoney())
            playersIn.remove(current);
        if (playersIn.size() <= 1) {
            setInputtedBet();
            r.setNumPlayers(numPlayers);
            return true;
        }
        window.setTurn(current);
        if (calls == playersIn.size()) {
            setInputtedBet();
            r.setNumPlayers(numPlayers);
            return true;
        }
        return false;
    }

    public boolean bet(int inBet) {
        if (inBet < bet || inBet % 10 != 0) {
            return false;
        }
        if (players.get(current).getMoney() < players.get(current).getInputtedMoney() + inBet)
            return false;
        bet = inBet;
        players.get(current).setCurrentInputtedMoney(bet);
        calls = 1;
        current++;
        if (current == playersIn.size())
            current = 0;
        window.setTurn(current);
        return true;
    }

    public ArrayList<Player> getPlayersIn() {
        return playersIn;
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
    }
}
