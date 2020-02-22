package db;

import application.Role;
import application.UserLogin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServiceProvider {

    private static Connection connection = null;

    public LoginServiceProvider() {
        if (connection == null) LoginServiceProvider.connection = new DbManager().getConnection();
    }

    public boolean isAuthenticated(UserLogin userLogin) {

        String user = userLogin.getUserName();
        String pwhash = userLogin.getPwHash();
        PreparedStatement pstmt = null;
        int res = 0;

        try {
            String query = "SELECT COUNT(*) FROM slotmachine.user WHERE username = ? AND pwhash = ?";

            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user);
            pstmt.setString(2, pwhash);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                res = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res == 1;
    }

    public Role getUserRole(String user) {

        PreparedStatement pstmt = null;
        String res = null;

        try {
            String query = "SELECT role FROM slotmachine.user WHERE username = ?";

            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                res = rs.getString(1);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Role.valueOf(res);
    }

    public String getAirline(String user) {

        PreparedStatement pstmt = null;
        String res = null;

        try {
            String query = "SELECT airline FROM slotmachine.user WHERE username = ?";

            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                res = rs.getString(1);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
