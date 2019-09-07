package application;

import java.time.LocalDateTime;

public class Proposal {
    private int auctionID;
    private int flightID;
    private float price;
    private boolean isBid; //else Ask
    private LocalDateTime initialTime;
    private LocalDateTime desiredTime;


    public Proposal(int auctionID,
                    int flightID,
                    float price,
                    boolean isBid,
                    LocalDateTime initialTime,
                    LocalDateTime desiredTime) {
        this.auctionID = auctionID;
        this.flightID = flightID;
        this.price = price;
        this.isBid = isBid;
        this.initialTime = initialTime;
        this.desiredTime = desiredTime;
    }

    public Proposal(int flightID,
                    float price,
                    boolean isBid,
                    LocalDateTime initialTime,
                    LocalDateTime desiredTime) {
        this.auctionID = this.hashCode(); //temporary
        this.flightID = flightID;
        this.price = price;
        this.isBid = isBid;
        this.initialTime = initialTime;
        this.desiredTime = desiredTime;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public int getFlightID() {
        return flightID;
    }


    public float getPrice() {
        return price;
    }

    public boolean isBid() {
        return isBid;
    }

    public LocalDateTime getInitialTime() {
        return initialTime;
    }

    public LocalDateTime getDesiredTime() {
        return desiredTime;
    }

    @Override
    public String toString() {
        return "Proposal{" +
                "auctionID=" + auctionID +
                ", flightID=" + flightID +
                ", price=" + price +
                ", isBid=" + isBid +
                ", initialTime=" + initialTime +
                ", desiredTime=" + desiredTime +
                '}';
    }
}
