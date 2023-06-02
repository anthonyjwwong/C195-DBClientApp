package wgu.dbclientapp.model;

/**
 * User class to hold the logged in user.
 */
public class User {
    private String username;

    /**
     * Constructor for User
     * @param username username
     */
    public User(String username) {
        this.username = username;

    }

    /**
     * Getter for user
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     * @param username username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }


}
