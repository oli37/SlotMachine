package application;

import db.LoginServiceProvider;
import utils.Utility;

import java.time.Instant;

public class UserLogin {

    private String userName;
    private String pwHash;
    private Role role;
    private Instant whenAuthenticated;
    private String airlineAlias;


    public UserLogin(String userName, String password, Role role, String airlineAlias) {
        this.userName = userName;
        this.role = role;
        this.pwHash = Utility.hash(password);
        this.airlineAlias = airlineAlias;
        this.whenAuthenticated = Instant.now();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPassword(String pw) {
        this.pwHash = Utility.hash(pw);
    }

    public Role getRole() {
        return role;
    }

    public Instant getWhenAuthenticated() {
        return whenAuthenticated;
    }

    public void setWhenAuthenticated(Instant whenAuthenticated) {
        this.whenAuthenticated = whenAuthenticated;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAirlineAlias() {
        return airlineAlias;
    }

    public void setAirlineAlias(String airlineAlias) {
        this.airlineAlias = airlineAlias;
    }

    public boolean isAirline() {
        return role.equals(Role.AIRLINE);
    }
}

;