package com.example.clientapplication;

public class Comment {
    public String commentID, announcementID, comment, id, email, dateCreated;
    public User author;
    public Comment(User user, String id, String email, String commentID, String announcementID, String comment, String dateCreated) {
        this.author = user;
        this.commentID = commentID;
        this.announcementID = announcementID;
        this.comment = comment;
        this.id = id;
        this.email = email;
        this.dateCreated = dateCreated;
    }
}
