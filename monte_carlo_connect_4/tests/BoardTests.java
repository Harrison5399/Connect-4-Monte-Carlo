import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BoardTests {

    public static void main(String[] args) {
        /*
            play normal game
         */

        Board b = new Board();

        System.out.println(b);

        Game game = new Game(b);

        System.out.println(game.get_has_win());

        while (game.get_has_win() == -1) {
            game.ask_and_do_turn();
        }

    }

    @Test
    public void MCTest() {

        Board original_b = new Board();

        MCNode mc = new MCNode(original_b, new ArrayList<>(), new MCRoot());

        System.out.println(original_b);

        Board expand = new Board();

        for (int i=0; i<7; i++) {
            Board newBoard = original_b.add_piece(i, 0).first();

            if (i == 2) {
                expand = newBoard;
            }

            mc.insert(newBoard);
        }

        System.out.println("original_b children:");
        List<MCNode> children = mc.getChildren();

        for (MCNode child : children) {
            System.out.println(child.getS());
        }

        System.out.println("\n\n");
        System.out.println("expand:");
        System.out.println(expand);

        MCNode expanded_node = mc.find(expand);

        for (int i=0; i<7; i++) {
            Board newBoard = expand.add_piece(i, 1).first();

            expanded_node.insert(newBoard);

        }

        System.out.println("\n\n");
        System.out.println("expand_children");

        children = expanded_node.getChildren();

        MCNode childcild = new MCNode();

        for (MCNode child : children) {
            System.out.println(child.getS());
            childcild = child;
        }

        System.out.println("check if kept:");

        System.out.println(mc.find(expand).getChildren().getFirst().getS());


        System.out.println("\n\n");
        System.out.println("expanded_node.getParent():");
        try {
            MCTree parent = expanded_node.getParent();
            System.out.println(parent.getS());
        }
        catch (MCIsRootE e) {
            System.out.println("is root error");
        }

        System.out.println("\n\n");
        System.out.println("childchild:");
        System.out.println(childcild.getS());

        System.out.println("childchild's parent (expanded node)");
        try {
            MCTree parent = childcild.getParent();
            System.out.println(parent.getS());
        } catch (MCIsRootE e) {
            System.out.println("is root error");
        }




    }

    @Test
    public void playTest() {

        Board b = new Board();

        b = b.add_piece(0, 0).first();
        b = b.add_piece(0, 0).first();
//        b = b.add_piece(0, 0).first();
//        b = b.add_piece(1, 1).first();
        b = b.add_piece(1, 1).first();
        b = b.add_piece(1, 1).first();

        System.out.println("inputed monte carlo board");
        System.out.println(b);

        MonteCarlo mc = new MonteCarlo(b);

        mc.play();

    }
}
