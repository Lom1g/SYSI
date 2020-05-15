package fr.lomig.mycarto.Fragment;

public class UsersModel {

    private String fName;
    private String email;
    private String rank;
    private long points;

    private UsersModel() {
    }

    private UsersModel(String fName, String email, String rank, long points) {
        this.fName = fName;
        this.email = email;
        this.rank = rank;
        this.points = points;

    }

    public String getfName() {
        return fName;
    }

    public String getEmail() {
        return email;
    }

    public String getRank() {
        return rank;
    }

    public long getPoints() {
        return points;
    }
}
