package db;

import application.Proposal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProposalServiceProvider implements ServiceProvider {

    private static Connection connection = null;

    public ProposalServiceProvider() {
        if (connection == null) ProposalServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Proposal> fetch(int offset, int limit) {
        String sqlAuction = "SELECT * FROM slotmachine.proposal ";
        if (limit != 0) sqlAuction = sqlAuction + "offset ? limit ?";

        PreparedStatement pstmt;
        List<Proposal> proposalList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(sqlAuction);
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

                Proposal proposal = new Proposal(auctionID, flightID, price, isBid, initialTime.toLocalDateTime(), desiredTime.toLocalDateTime());
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
    public List<Proposal> fetchAll() {
        return fetch(0, 0);
    }

    @Override
    public int getCount() {
        return fetchAll().size();
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

                Proposal proposal = new Proposal(auctionID, flightID, price, isBid, initialTime.toLocalDateTime(), desiredTime.toLocalDateTime());
                proposalList.add(proposal);
            }

            pstmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return proposalList;
    }

    public boolean post(Proposal proposal) {
        String sqlAuction = "INSERT INTO slotmachine.proposal (flight_id, price, bid, ask, initialtime, desiredtime) \n" +
                "VALUES ( ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = connection.prepareStatement(sqlAuction);
            pstm.setInt(1, proposal.getFlightID());
            pstm.setFloat(2, proposal.getPrice());
            pstm.setBoolean(3, proposal.isBid());
            pstm.setBoolean(4, !proposal.isBid());
            pstm.setTimestamp(5, Timestamp.valueOf(proposal.getInitialTime()));
            pstm.setTimestamp(6, Timestamp.valueOf(proposal.getDesiredTime()));

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
