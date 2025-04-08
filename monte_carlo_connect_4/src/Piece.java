/*
-1 : no player there
0 : player 0 there
1 : player 1 there
 */
public class Piece {

    private int player;

    public Piece(int player) {
        this.player = player;
    }

    public Piece() {
        this.player = -1;
    }

    public int getPlayer() {
        return this.player;
    }

    public String toString() {
        return String.valueOf(this.player);
    }

}