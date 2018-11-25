package com.flickrly.flickrly.models;

import org.threeten.bp.Instant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Photo implements Serializable {
    private String tags;

    private String author;

    private String title;

    private String description;

    private Instant dateTaken;

    private String link;

    private String authorId;


    private Instant published;

    private Media media;

    public String getTags() {
        return tags;
    }

    public List<String> getListOfTags() {
        if (getTags() == null || getTags().trim().length() == 0) {
            return new ArrayList<>();
        }
        List<String> l = new ArrayList<>();
        String[] splat = getTags().split("\\s+");
        for (String s : splat) {
            String str = s.trim();
            if (str.length() > 0) {
                l.add(str);
            }
        }
        return l;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Instant getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Instant dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Instant getPublished() {
        return published;
    }

    public void setPublished(Instant published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "[tags = " + tags + ", author = " + author + ", title = " + title +
                ", description = " + description + ", date_taken = " + dateTaken +
                ", link = " + link + ", author_id = " + authorId +
                ", published = " + published + ", media = " + media + "]";
    }
}
	