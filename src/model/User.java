package model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String passwordHash;
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.wallet = new Wallet(username, this.passwordHash);
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean checkPassword(String password) {
        return passwordHash.equals(hashPassword(password));
    }

    public Wallet getWallet() {
        return wallet;
    }

    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }
}
