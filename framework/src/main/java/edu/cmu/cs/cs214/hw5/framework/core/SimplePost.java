package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.Date;
import java.util.Optional;

/**
 * The {@code SimplePost} represents a simple post without a sentiment.
 * The data plugin is responsible for returning a series of SimplePosts
 * to the framework.
 *
 * @author Team6
 */
public final class SimplePost implements Post {
    private final Date timeStamp;
    private final String content;
    private final Optional<String> location;

    /**
     * Initializes a new {@code SimplePost} object from a timestamp and a text content.
     *
     * @param timeStamp     Date when the post was posted
     * @param content       The text content of the post
     */
    public SimplePost(Date timeStamp, String content) {
        this.timeStamp = timeStamp;
        this.content = content;
        this.location = Optional.empty();
    }

    /**
     * Initializes a new {@code SimplePost} object from a timestamp, a text content and a location.
     *
     * @param timeStamp     Date when the post was posted
     * @param content       The text content of the post
     * @param location      The location where the post was posted
     */
    public SimplePost(Date timeStamp, String content, String location) {
        this.timeStamp = timeStamp;
        this.content = content;
        this.location = Optional.of(location);
    }

    @Override
    public Date getTimeStamp() {
        return timeStamp;
    }

    @Override
    public Optional<String> getLocation() {
        return location;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        if (location.isPresent()) {
            return String.format("Post{Timestamp: %s, Location: %s, Content: %s}", timeStamp, location.get(), content);
        } else {
            return String.format("Post{Timestamp: %s, Content: %s}", timeStamp, content);
        }
    }
}
