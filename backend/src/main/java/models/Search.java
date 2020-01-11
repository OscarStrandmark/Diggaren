package models;

public class Search {

    private String auth;
    private String type;
    private String query;

    public Search(String auth, String type, String query) {
        this.auth = auth;
        this.type = type;
        this.query = query;
    }

    public String getAuth() {
        return auth;
    }

    public String getType() {
        return type;
    }

    public String getQuery() {
        return query;
    }
}
