package application;

public class Proposal {
    private int proposalID;
    private float price;
    private int delay; //in minutes
    private boolean isAsk; //else Ask

    public Proposal() {
    }

    public Proposal( float price, int delay, boolean isAsk) {
        this.price = price;
        this.delay = delay;
        this.isAsk = isAsk;
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

    public boolean isAsk() {
        return isAsk;
    }

    public void setAsk(boolean ask) {
        isAsk = ask;
    }

    @Override
    public String toString() {
        return "Proposal{" +
                ", proposal_id=" + proposalID +
                ", price=" + price +
                ", delay=" + delay +
                ", isBid=" + isAsk +
                '}';
    }
}


