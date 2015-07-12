package com.athvillar.tausy.top10downloader;

/**
 * Created by TAUSY on 7/3/2015.
 */
public class Application {

    private String name;
    private String artist;
    private String releaseDate;

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getName() {

        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String toString(){
        return "Name :" + this.getName() + "\n" +
               "Artist :" + this.getArtist() + "\n" +
               "Release Date :" + this.getReleaseDate() + "\n";
    }

}
