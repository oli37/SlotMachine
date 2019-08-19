package application;

import java.time.LocalDateTime;

public class Auction {
    private int auctionID;
    private int flightID1;
    private int flightID2;
    private boolean isBid; //else ask
    private LocalDateTime initialTime;
    private LocalDateTime desiredTime;


    public Auction(int flightID1, int flightID2, boolean isBid, LocalDateTime initialTime, LocalDateTime desiredTime) {
        this.flightID1 = flightID1;
        this.flightID2 = flightID2;
        this.isBid = isBid;
        this.initialTime = initialTime;
        this.desiredTime = desiredTime;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public int getFlightID1() {
        return flightID1;
    }

    public int getFlightID2() {
        return flightID2;
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
}
