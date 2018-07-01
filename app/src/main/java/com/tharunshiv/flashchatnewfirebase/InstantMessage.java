package com.tharunshiv.flashchatnewfirebase;
// User genereated class
public class InstantMessage {

    private String message;
    private String author;

    // create a constructor by generating on using the right click
    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    // we need a second contructor, according to Firebase
    // refer doc


    public InstantMessage() {

    }

    // generate this too

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
