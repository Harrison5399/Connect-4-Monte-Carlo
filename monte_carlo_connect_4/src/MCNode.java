import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MCNode implements MCTree {





    private Board s;
    private MCTree parent;
    private List<MCNode> children = new ArrayList<>();
    private int t;
    private int n;
    private boolean isTerminal = false;
    private int height;

    MCNode (Board b, int t, int n, int height, MCTree parent) {
        this.s = b;
        this.t = t;
        this.n = n;
        this.height = height;
        this.parent = parent;
    }

    MCNode (Board b, List<MCNode> children, MCTree parent) {
        this.s = b;
        this.t = 0;
        this.n = 0;
        this.height = 0;
        this.children = children;
        this.parent = parent;
    }

    MCNode(Board b)  {
        this.s = b;
        this.t = 0;
        this.n = 0;
        this.height = 0;
        this.children = new ArrayList<>();
        this.parent = new MCRoot();
    }

    MCNode () {
        this.s = new Board();
        this.t = 0;
        this.n = 0;
        this.height = 0;
        this.children = new ArrayList<>();
        this.parent = new MCRoot();
    }

    public int getT() {
        return this.t;
    }

    public int getN() {
        return this.n;
    }

    public Board getS() {
        return this.s;
    }
    public boolean getIsTerminal() {
        return this.isTerminal;
    }

    public void setT(int new_t) {
        this.t = new_t;
    }

    public void setN(int new_n) {
        this.n = new_n;
    }
    public void setTerminal(boolean new_t) {
        this.isTerminal = new_t;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    public List<MCNode> getChildren() {
        return this.children;
    }

    public MCTree getParent() {
        return this.parent;
    }

    public boolean isEmpty() {
        return false;
    }

    public int getHeight() {
        return this.height;
    }

    public MCNode find(Board b) {
        if (this.s.equals(b)) {
            return this;
        }
        else {

            for (MCNode child : this.children) {

                MCNode result = child.find(b);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    public void insert(Board b) {
        this.children.add(new MCNode(b, 0, 0, this.height+1, this));
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("t: ");
        sb.append(this.t);
        sb.append("\n");
        sb.append("n: ");
        sb.append(this.n);
        sb.append("\n");
        sb.append("isTerminal: ");
        sb.append(this.isTerminal);
        sb.append("\n");
        sb.append(this.s);

        return sb.toString();
    }




//    MCTree delete(Board b);


}
