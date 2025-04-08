public interface MCTree {
    abstract Board getS() throws MCIsRootE;
    abstract int getT() throws MCIsRootE;
    abstract int getN() throws MCIsRootE;
    abstract MCTree getParent() throws MCIsRootE;
    abstract void setT(int new_t) throws MCIsRootE;
    abstract void setN(int new_n) throws MCIsRootE;
}
