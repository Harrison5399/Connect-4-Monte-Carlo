import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

// TODO:
// needs to be made immutable

public class Board {

    private List<List<Piece>> board_state;
    private int height = 6;
    private int width = 7;

    public Board() {
        this.board_state = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            List<Piece> row = new ArrayList<>(width);
            for (int j = 0; j < width; j++) {
                row.add(new Piece());
            }
            this.board_state.add(row);
        }
    }

    public Board(List<List<Piece>> board_state) {
        this.board_state = board_state;
    }

    public List<List<Piece>> getBoardState() {
        return this.board_state;
    }

    private Piece get_piece(int row, int col) {
        return this.board_state.get(row).get(col);
    }

    private void set_piece(int row, int col, int player) {
        this.board_state.get(row).set(col, new Piece(player));
    }

    /*
        returns only non-played spaces (filters -1)
     */
    private List<Piece> get_col(int col) {
        List<Piece> r_arr = new ArrayList<>();
        for (int i=height-1; i>=0; i--) {
            if (this.get_piece(i, col).getPlayer() == -1) {
                break;
            }

            r_arr.add(this.get_piece(i, col));
        }
        return r_arr;
    }

    public Board copyOf() {
        List<List<Piece>> newBoardState = new ArrayList<>();
        for (List<Piece> row : this.board_state) {
            newBoardState.add(new ArrayList<>(row));
        }

        return new Board(newBoardState);
    }


    /*
        returns pair,
        first : new Board
        second : row added,
     */
    public Pair<Board, Integer> add_piece(int col, int player) {
        Board newBoard = this.copyOf();

        if (!newBoard.is_valid(col)) {
            throw new InputMismatchException("Play was outside of board");
        }

        List<Piece> col_arr = newBoard.get_col(col);
        int row = height - col_arr.size() - 1;

        newBoard.board_state.get(row).set(col, new Piece(player));

        return new Pair<>(newBoard, row);
    }

    public boolean check_winner(int row, int col, int player) {
        // horizontal
        if (check_direction(row, col, player, 0, 1) + check_direction(row, col, player, 0, -1) >= 3) {
            return true;
        }
        // vertical
        if (check_direction(row, col, player, 1, 0) + check_direction(row, col, player, -1, 0) >= 3) {
            return true;
        }
        // diagonal (bottom-left to top-right)
        if (check_direction(row, col, player, 1, 1) + check_direction(row, col, player, -1, -1) >= 3) {
            return true;
        }
        // diagonal (bottom-right to top-left)
        if (check_direction(row, col, player, 1, -1) + check_direction(row, col, player, -1, 1) >= 3) {
            return true;
        }

        return false;
    }

    private int check_direction(int row, int col, int player, int slope_row, int slope_col) {
        int count = 0;
        int r = row + slope_row;
        int c = col + slope_col;

        while (this.is_valid(r, c) && this.get_piece(r, c).getPlayer() == player) {
            count++;
            r += slope_row;
            c += slope_col;
        }

        return count;
    }

    private boolean is_valid(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    public boolean is_valid(int col) {
        return this.get_col(col).size() < height || col > height || col < 0;
    }

    public List<Integer> get_valid_moves() {
        List<Integer> r_list = new ArrayList<>();
        for (int i=0; i<width; i++) {
            if (this.is_valid(i)) {
                r_list.add(i);
            }
        }
        return r_list;
    }

    public int get_next_turn(int first_turn) {
        int count_0 = 0;
        int count_1 = 0;

        for (List<Piece> row : this.board_state) {

            for (Piece p : row) {
                if (p.getPlayer() == 1) {
                    count_1++;
                } else if (p.getPlayer() == 0) {
                    count_0++;
                }
            }

        }


        int diff = Math.abs(count_1 - count_0);

        if (diff == 0 || diff == 1) {
            if (first_turn == 0) {
                return diff;
            } else if (first_turn == 1) {
                return Math.abs(diff - 1);
            } else {
                throw new InputMismatchException("get_next_turn input was not 0 or 1");
            }
        }
        throw new InputMismatchException("0/1 counts differ > 1");

    }

    public int findColChanged(Board child) {


        for (int i=0; i<width; i++) {
            List<Piece> parent_col = this.get_col(i);
            List<Piece> child_col = child.get_col(i);

            if (!parent_col.equals(child_col)) {
                return i;
            }
        }

        return -1;
    }

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLACK = "\u001B[30m";


    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<width; i++) {
            sb.append("|");
            sb.append(" ");
            sb.append(YELLOW);
            sb.append(i);
            sb.append(RESET);
        }
        sb.append("|\n");

        for (List<Piece> r : board_state) {
            sb.append("----------------------\n");
            for (Piece c : r) {
                sb.append("|");
                if (c.toString().length() == 1) {
                    sb.append(" ");
                }

                if (c.getPlayer() == 0) {
                    sb.append(RED);
                }
                else if (c.getPlayer() == 1) {
                    sb.append(GREEN);
                }
                else {
                    sb.append(BLACK);
                }
                sb.append(c);
                sb.append(RESET);
            }
            sb.append("|");
            sb.append("\n");
        }
        sb.append("----------------------\n");

        for (int i=0; i<width; i++) {
            sb.append("|");
            sb.append(" ");
            sb.append(YELLOW);
            sb.append(i);
            sb.append(RESET);        }
        sb.append("|\n");

        return sb.toString();
    }

    public boolean equals(Board b) {
         for (int i=0; i<width; i++) {
             List<Piece> this_col = this.get_col(i);
             List<Piece> b_col = b.get_col(i);

             if (!this_col.equals(b_col)) {
                 return false;
             }
         }

         return true;
    }
}
