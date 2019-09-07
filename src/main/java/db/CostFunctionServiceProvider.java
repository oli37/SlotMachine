package db;

import application.CostFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CostFunctionServiceProvider implements ServiceProvider {


    private static Connection connection = null;

    public CostFunctionServiceProvider() {
        if (connection == null) CostFunctionServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<CostFunction> fetch(int offset, int limit) {
        String sqlCostFunction = "SELECT * \n" +
                "FROM  slotmachine.costfunction AS cf\n" +
                "ORDER BY cf.cf_name\n";

        if (limit != 0) sqlCostFunction = sqlCostFunction + "offset ? limit ?";

        PreparedStatement pstmt;
        List<CostFunction> cfList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(sqlCostFunction);
            if (limit != 0) {
                pstmt.setInt(1, offset);
                pstmt.setInt(2, limit);
            }
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                var cf = getCostFunction(res);
                cfList.add(cf);
            }

            pstmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cfList;
    }

    @Override
    public List<CostFunction> fetchAll() {
        return fetch(0, 0);
    }

    @Override
    public int getCount() {
        return fetchAll().size();
    }

    public List<CostFunction> fetchByCfName(String name) {
        String sqlCostFunction = "SELECT * FROM slotmachine.costfunction WHERE cf_name = ? ";

        PreparedStatement pstmt;
        List<CostFunction> cfList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(sqlCostFunction);
            pstmt.setString(1, name);
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                var cf = getCostFunction(res);
                cfList.add(cf);
            }

            pstmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cfList;
    }


    public boolean post(CostFunction cf) {

        String sqlCostFunctionPost = "INSERT INTO slotmachine.costfunction\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlCostFunctionPost);
            pstmt.setString(1, cf.getName());
            pstmt.setDouble(2, cf.getT1());
            pstmt.setDouble(3, cf.getT2());
            pstmt.setDouble(4, cf.getT3());
            pstmt.setDouble(5, cf.getT4());
            pstmt.setDouble(6, cf.getT5());
            pstmt.setDouble(7, cf.getT6());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private CostFunction getCostFunction(ResultSet res) {
        try {
            String name = res.getString(1);
            float t1 = res.getFloat(2);
            float t2 = res.getFloat(3);
            float t3 = res.getFloat(4);
            float t4 = res.getFloat(5);
            float t5 = res.getFloat(6);
            float t6 = res.getFloat(7);

            return new CostFunction(name, t1, t2, t3, t4, t5, t6);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
