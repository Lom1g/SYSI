package fr.lomig.mycarto;

public class SpotModel {

    private String title;
    private String description;
    private float latitude;
    private float longitude;
    private String category;

    public SpotModel() {
    }

    public SpotModel(String title, String description, float latitude, float longitude, String category) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category=category;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCategory(){return category;}
}
