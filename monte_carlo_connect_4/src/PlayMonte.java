public class PlayMonte {

    public static void main(String[] args) {

        Game g = new Game();

        /*
            you are player 0
            set first_player for you first
            set first_player for Monte first
         */
        int first_player = 1;
        g.setTurn(first_player);

        while (g.get_has_win() == -1) {

            if (g.get_turn() == 0) {
                g.ask_and_do_turn();
            }

            else if (g.get_turn() == 1) {
                MonteCarlo mc = new MonteCarlo(g.get_board_state(), 1, first_player, 3000);

                int montes_play = mc.play();

                System.out.println("Monte played: " + montes_play);

                g.do_turn(montes_play);
            }

            System.out.println(g.get_board_state());
        }

        System.out.println("Winner: " + g.get_has_win());

    }

}
