package jpa.basic.book.shop.domain;

public class Movie extends Item {

    private String director;
    private String actor;

    public Movie(String director, String actor) {
        this.director = director;
        this.actor = actor;
    }
}
