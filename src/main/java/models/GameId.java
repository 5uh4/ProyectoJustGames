package models;

public class GameId {

	 private String platform;
	    private int id;

	    public GameId(String platform, int id) {
	        this.platform = platform;
	        this.id = id;
	    }

	    public String getPlatform() {
	        return platform;
	    }

	    public void setPlatform(String platform) {
	        this.platform = platform;
	    }

	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    @Override
	    public String toString() {
	        return "GamePrice [platform=" + platform + ", id=" + id + "]";
	    }
}
