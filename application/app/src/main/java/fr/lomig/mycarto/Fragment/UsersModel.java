package fr.lomig.mycarto.Fragment;

public class UsersModel {

    private String name;
    private String email;
    private String rank;
    private long points;

    private UsersModel() {
    }

    private UsersModel(String name, String email, String rank, long points) {
        this.name = name;
        this.email = email;
        this.rank = rank;
        this.points = points;

    }

    public String getName() {
        return name;
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
