package application;

public class Proposal {
    private int proposalID;
    private float price;
    private int delay; //in minutes
    private boolean isBid; //else Ask

    public Proposal() {
    }

    public Proposal( float price, int delay, boolean isBid) {
        this.price = price;
        this.delay = delay;
        this.isBid = isBid;
    }


    public int getProposalID() {
        return proposalID;
    }

    public void setProposalID(int proposalID) {
        this.proposalID = proposalID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isBid() {
        return isBid;
    }

    public void setBid(boolean bid) {
        isBid = bid;
    }

    @Override
    public String toString() {
        return "Proposal{" +
                ", proposal_id=" + proposalID +
                ", price=" + price +
                ", delay=" + delay +
                ", isBid=" + isBid +
                '}';
    }
}


