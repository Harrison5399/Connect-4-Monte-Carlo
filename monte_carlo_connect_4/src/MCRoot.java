public class MCRoot implements MCTree {
    @Override
    public Board getS() throws MCIsRootE {
        throw new MCIsRootE();
    }

    @Override
    public int getT() throws MCIsRootE {
        throw new MCIsRootE();
    }

    @Override
    public int getN() throws MCIsRootE {
        throw new MCIsRootE();
    }

    @Override
    public MCTree getParent() throws MCIsRootE {
        throw new MCIsRootE();
    }

    public  void setT(int new_t) throws MCIsRootE {
        throw new MCIsRootE();
    }

    public  void setN(int new_n) throws MCIsRootE {
        throw new MCIsRootE();
    }
}
