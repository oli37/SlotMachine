package db;

import application.Auction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionServiceProvider implements ServiceProvider {

    private static Connection connection = null;

    public AuctionServiceProvider() {
        if (connection == null) AuctionServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Auction> fetch(int offset, int limit) {
        String sqlAuction = "SELECT * FROM slotmachine.auction ";
        if (limit != 0) sqlAuction = sqlAuction + "offset ? limit ?";

        PreparedStatement pstmt;
        List<Auction> auctionList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(sqlAuction);
            if (limit != 0) {
                pstmt.setInt(1, offset);
                pstmt.setInt(2, limit);
            }
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                int auctionID = res.getInt(1);
                int flightID1 = res.getInt(2);
                int flightID2 = res.getInt(3);
                boolean isBid = res.getBoolean(4);
                Timestamp initialTime = res.getTimestamp(6);
                Timestamp desiredTime = res.getTimestamp(7);

                Auction auction = new Auction(auctionID, flightID1, flightID2, isBid, initialTime.toLocalDateTime(), desiredTime.toLocalDateTime());
                auctionList.add(auction);
            }

            pstmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auctionList;
    }


    @Override
    public List<Auction> fetchAll() {
        return fetch(0, 0);
    }

    @Override
    public int getCount() {
        return fetchAll().size();
    }

    public boolean post(Auction auction) {
        String sqlAuction = "INSERT INTO slotmachine.auction \n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt;
        List<Auction> auctionList = new ArrayList<>();

        try {
            PreparedStatement pstm = connection.prepareStatement(sqlAuction);
            //pstm.setInt(1, auction.getAuctionID());
            pstm.setInt(2, auction.getFlightID1());
            pstm.setInt(3, auction.getFlightID2());
            pstm.setBoolean(4, auction.isBid());
            pstm.setBoolean(5, !auction.isBid());
            pstm.setTimestamp(6, Timestamp.valueOf(auction.getInitialTime()));
            pstm.setTimestamp(7, Timestamp.valueOf(auction.getDesiredTime()));
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
