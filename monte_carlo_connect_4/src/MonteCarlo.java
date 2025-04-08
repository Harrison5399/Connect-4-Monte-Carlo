import java.util.*;

public class MonteCarlo {

    /*
        this class stores game state and plays game,
        makes all Monte Carlo calculations,
        starts with a fresh game.

        traverses down the tree as you play and makes recalculations on same tree
     */
    /*
        1.) UCB1(s_1):
                - some map function to get UCB1 for all nodes,
                - UCB1, interchangeable with different exploitation/exploration terms

        2.) Node Expansion:
                - flow char described on paper
                - function to expand node for all possible moves

         3.) Rollout:
                - some recursive function to get some node's new random terminal state

         4.) Backpropagation:
                - update nodes above node chose to roll out

     */

    private MCNode current_state_node;
    private int player;
    private int first_player;
    private Random rand = new Random();
    private int iterations = 3000;

    public MonteCarlo(Board b) {
        this.current_state_node = new MCNode(b);
        this.current_state_node.setN(1);
        this.player = 1;
        this.first_player = 1;
    }

    public MonteCarlo(Board b, int player, int first_player, int iterations) {
        this.current_state_node = new MCNode(b);
        this.current_state_node.setN(1);
        this.player = player;
        this.first_player = first_player;
        this.iterations = iterations;
    }

    public int play() {

        for (int i = 0; i<this.iterations; i++) {
            Pair<MCNode, Integer> outcome = nodeExpansion(this.current_state_node);
            this.backpropagation(outcome.first(), outcome.second());

        }


        List<MCNode> node_children = this.current_state_node.getChildren();
        HashMap<MCNode, Double> nodeToUCB1 = new HashMap<>();

        for (MCNode n : node_children) {
            double ucb1Value = this.modified_UCB1(n);
            nodeToUCB1.put(n, ucb1Value);
        }


/*
        int ii = 1;
        for (Map.Entry<MCNode, Double> i : nodeToUCB1.entrySet()) {
            System.out.println("Child: " + ii + "\tt: " + i.getKey().getN() + "\tn: " + i.getKey().getN());
            ii++;
        }
*/


        MCNode bestChild = nodeToUCB1.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList().getLast().getKey();

        return this.current_state_node.getS().findColChanged(bestChild.getS());

    }



    /*
        exploratory/exploration function for each node
     */
    private double UCB1(MCNode node) {
        try {
            if (node.getN() != 0 || node.getT() != 0) {

                return (node.getT()) + (2 * Math.sqrt(Math.log(node.getParent().getN()) / node.getN()));
            }
            return Double.POSITIVE_INFINITY;
        }
        catch (MCIsRootE e) {
            return Double.NEGATIVE_INFINITY;
        }
    }

    public double modified_UCB1(MCNode node) {
        try {
            if (node.getN() != 0) {
                double dynamicC = (double) 50 * (1.0 / (1 + node.getHeight()));

                return ((double) node.getT() / node.getParent().getN()) + (Math.sqrt(dynamicC * Math.log(node.getParent().getN())) / node.getN());
            }
            return Double.POSITIVE_INFINITY;
        }
        catch (MCIsRootE e) {
            return Double.NEGATIVE_INFINITY;
        }
    }

    /*
        current = s_0
        if current == leaf node:
            current = max(child_nodes UCB1 values)
        else:
            if has been visited:
                add each possible action to current,
                current = first added action
            else:
                rollout

       first: node rolled out,
       second: rollout value,
               aka. terminal game state value (0/1)

     */
    private Pair<MCNode, Integer> nodeExpansion(MCNode node) {

        if (!node.isLeaf()) {

            List<MCNode> node_children = node.getChildren();
            HashMap<MCNode, Double> nodeToUCB1 = new HashMap<>();

            for (MCNode n : node_children) {
                double ucb1Value = this.modified_UCB1(n);
                nodeToUCB1.put(n, ucb1Value);
            }



            MCNode bestChild = nodeToUCB1.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .toList().getLast().getKey();

            return nodeExpansion(bestChild);

        }
        else {
            Board b = node.getS();
            int curr_player = b.get_next_turn(this.first_player);

            if (node.getIsTerminal()) {
                return new Pair<>(node, (curr_player + 1) % 2);
            }

            if (node.getN() == 0) {

                return new Pair<>(node, rollout(new Game(node.getS(), curr_player)));

            }
            else {

                List<Integer> valid_moves = b.get_valid_moves();

                for (int move : valid_moves) {

                    Pair<Board, Integer> childBoard = b.add_piece(move, curr_player);

                    node.insert(childBoard.first());

                    MCNode child = node.getChildren().getLast();


                    int row_added = childBoard.second();
                    boolean winner = childBoard.first().check_winner(row_added, move, curr_player);

                    if (winner) {
                        int v = 0;
                        if (curr_player == player) {
                            v = 1;
                        }

                        child.setT(v);
                        child.setTerminal(true);

                        this.backpropagation(child, v);
                    }
                }

                return nodeExpansion(node.getChildren().getFirst());
            }
        }

    }


    /*
        plays random states until reach terminal node, returns win state
     */
    private int rollout(Game g) {

        Board b = g.get_board_state();

        List<Integer> valid_moves = b.get_valid_moves();

        if (valid_moves.size() <= 0) {
            return 0;
        }

        int move = rand.nextInt(valid_moves.size());
        int game_outcome = g.do_turn(valid_moves.get(move));

        if (game_outcome == -1) {
            return rollout(g);
        }
        else if (game_outcome == -2) {
            throw new RuntimeException("valid moves random choice chose invalid choice.");
        }
        else {
            if (game_outcome == player) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    /*
        backpropagation from node (node that was rolled out),
        recalculates t and n values for parent nodes,
        node_increment = return of rollout
     */
    public void backpropagation(MCTree node, int node_increment) {
        try {

            node.setT(node.getT() + node_increment);
            node.setN(node.getN() + 1);

            backpropagation(node.getParent(), node_increment);

        } catch (MCIsRootE e) {
            return;
        }
    }

}
