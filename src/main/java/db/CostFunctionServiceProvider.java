package db;

import application.CostFunction;
import application.Proposal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CostFunctionServiceProvider implements ServiceProvider {

    private static Connection connection = null;
    private String queryCFNames =
            "SELECT cf_name " +
                    "FROM  slotmachine.costfunction AS cf " +
                    "ORDER BY cf.cf_name ";
    private String queryCFNamesByOwner =
            "SELECT cf_name " +
                    "FROM  slotmachine.costfunction AS cf " +
                    "WHERE cf.owner = ? " +
                    "ORDER BY cf.cf_name ";
    private String queryCF =
            "SELECT prp.ask, prp.bid, prp.delay, prp.price, prp.proposal_id " +
                    "FROM slotmachine.costfunction AS cf " +
                    "JOIN slotmachine.proposal AS prp ON prp.cf = cf.cf_name " +
                    "WHERE cf.cf_name = ? ";
    private String count = "SELECT COUNT(*) FROM slotmachine.costfunction";
    private String postCF = "INSERT INTO slotmachine.costfunction (cf_name, owner) VALUES (?, ?)";
    private String postProp = "INSERT INTO slotmachine.proposal (price, delay, bid, ask, cf) VALUES (?, ?, ?, ?, ?)";


    public CostFunctionServiceProvider() {
        if (connection == null) CostFunctionServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<String> fetch(int offset, int limit) {

        queryCFNames = queryCFNames + "offset ? limit ?";
        PreparedStatement pstmt;
        List<String> list = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(queryCFNames);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, limit);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("cf_name");
                list.add(name);
            }

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<String> fetch() {
        PreparedStatement pstmt;
        List<String> list = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(queryCFNames);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("cf_name");
                list.add(name);
            }

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> fetch(String owner) {

        PreparedStatement pstmt;
        List<String> list = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(queryCFNamesByOwner);
            pstmt.setString(1, owner);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("cf_name");
                list.add(name);
            }

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public CostFunction fetchCF(String cfName) {
        PreparedStatement pstmt;
        CostFunction cf = new CostFunction(cfName);
        try {
            pstmt = connection.prepareStatement(queryCF);
            pstmt.setString(1, cfName);
            ResultSet rs = pstmt.executeQuery();
            cf.setName(cfName);

            while (rs.next()) {
                cf.addProposal(unwrap(rs));
            }

            pstmt = connection.prepareStatement(queryCF);

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cf;
    }

    @Override
    public int getCount() {
        PreparedStatement pstmt;
        int counter = 0;
        try {
            pstmt = connection.prepareStatement(count);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                counter = rs.getInt(1);
            }

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }

    @Override
    public boolean post(Object element) {
        assert element instanceof CostFunction;
        CostFunction cf = (CostFunction) element;


        List<String> list = new ArrayList<>();

        try {
            PreparedStatement pstmtCF = wrap(cf);
            assert pstmtCF != null;
            pstmtCF.executeUpdate();
            pstmtCF.close();


            for (Proposal p : cf.getProposalList()) {
                PreparedStatement pstmtProp = wrap(p, cf.getName());
                assert pstmtProp != null;
                pstmtProp.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private PreparedStatement wrap(CostFunction cf) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(postCF);
            pstmt.setString(1, cf.getName());
            pstmt.setString(2, cf.getOwner());
            return pstmt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private PreparedStatement wrap(Proposal prop, String cf) {
        try {

            PreparedStatement pstmt = connection.prepareStatement(postProp);
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

    private Proposal unwrap(ResultSet rs) {
        Proposal prop = new Proposal();
        try {
            prop.setProposalID(rs.getInt("proposal_id"));
            prop.setPrice(rs.getFloat("price"));
            prop.setDelay(rs.getInt("delay"));
            prop.setAsk(rs.getBoolean("bid"));

            return prop;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
