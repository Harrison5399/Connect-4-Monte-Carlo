import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private Board board_state;
    private int turn;
    private int has_win = -1;
    private Random rand = new Random();

    public Game(Board board_state) {
        this.board_state = board_state;
        this.turn = this.rand.nextInt(2);
    }

    public Game(Board board_state, int turn) {
        this.board_state = board_state;
        this.turn = turn;
    }

    public Game() {
        this.board_state = new Board();
        this.turn = this.rand.nextInt(2);
    }

    public void setBoard_state(Board b) {
        this.board_state = b;
    }
    public void setTurn(int t) { this.turn = t; }
    public int getTurn() { return this.turn; }

    public Board get_board_state() {
        return this.board_state;
    }

    public int get_turn() {
        return this.turn;
    }

    public int get_has_win() {
        return this.has_win;
    }


    /*
        returns -2 for invalid choice, -1 for no winner, 0/1 for winner
     */
    public int do_turn(int choice) {

        try {
            Pair<Board, Integer> p = this.board_state.add_piece(choice, this.turn);

            this.setBoard_state(p.first());

            boolean is_winner = this.board_state.check_winner(p.second() , choice, this.turn);
            if (is_winner) {
                this.has_win = turn;
                return turn;
            }

            this.flip_turn();

            return -1;
        } catch (InputMismatchException e) {
            return -2;
        }


    }

    private void flip_turn() {
        this.turn = (this.turn + 1) % 2;
    }

    public void ask_and_do_turn() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("player " + this.turn + " enter column move: ");
        int col = scanner.nextInt();

        int returned = do_turn(col);

        if (returned == -2) {
            System.out.println("Invalid choice.");
            this.ask_and_do_turn();
        }
        else {
            System.out.println(this.board_state);
        }
    }



}
