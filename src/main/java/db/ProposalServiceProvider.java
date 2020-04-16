package db;

import application.Proposal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProposalServiceProvider implements ServiceProvider {

    private static Connection connection = null;
    private String query = "SELECT * FROM slotmachine.proposal ";
    private String post = "INSERT INTO slotmachine.proposal (price, delay, bid, ask, cf) VALUES (?, ?, ?, ?, ?)";

    public ProposalServiceProvider() {
        if (connection == null) ProposalServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Proposal> fetch(int offset, int limit) {
        query += "offset ? limit ?";

        PreparedStatement pstmt;
        List<Proposal> proposalList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(query);
            if (limit != 0) {
                pstmt.setInt(1, offset);
                pstmt.setInt(2, limit);
            }
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                int auctionID = res.getInt(1);
                int flightID = res.getInt(2);
                float price = res.getFloat(3);
                boolean isBid = res.getBoolean(4);
                Timestamp initialTime = res.getTimestamp(6);
                Timestamp desiredTime = res.getTimestamp(7);

                Proposal proposal = new Proposal();//(auctionID, flightID, price, isBid, initialTime.toLocalDateTime(), desiredTime.toLocalDateTime());
                proposalList.add(proposal);
            }

            pstmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proposalList;
    }

    @Override
    public List fetch() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean post(Object element) {
        return false;
    }


    public List<Proposal> fetchByFlightID(int flightId) {
        String sqlAuction2 = "SELECT * FROM slotmachine.proposal WHERE flight_id = ? ";

        PreparedStatement pstmt;
        List<Proposal> proposalList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(sqlAuction2);
            pstmt.setInt(1, flightId);

            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                int auctionID = res.getInt(1);
                int flightID = res.getInt(2);
                float price = res.getFloat(3);
                boolean isBid = res.getBoolean(4);
                Timestamp initialTime = res.getTimestamp(6);
                Timestamp desiredTime = res.getTimestamp(7);

                Proposal proposal = new Proposal();//auctionID, flightID, price, isBid, initialTime.toLocalDateTime(), desiredTime.toLocalDateTime());
                proposalList.add(proposal);
            }

            pstmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return proposalList;
    }

    private PreparedStatement wrap(Proposal prop, String cf) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(post);

            pstmt.setFloat(1, prop.getPrice());
            pstmt.setInt(2, prop.getDelay());
            pstmt.setBoolean(3, prop.isAsk());
            pstmt.setBoolean(4, !prop.isAsk());
            pstmt.setString(5, cf);

            return pstmt;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}


