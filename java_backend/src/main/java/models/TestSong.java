package models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestSong {
	private String name;
	private String artist;
	
	public TestSong() {}
	
	public TestSong(String name, String artist) {
		super();
		this.name = name;
		this.artist = artist;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}

	
}
