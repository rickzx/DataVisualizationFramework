package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The {@code Post} interface outlines the core elements of a post in the framework.
 * A post should consist of a time stamp, a text content, and an optional
 * location.
 *
 * @author Team6
 */
public interface Post {
    /**
     * Get the time stamp of the post
     *
     * @return  The time stamp of the post
     */
    Date getTimeStamp();

    /**
     * Get the text content of the post
     *
     * @return  The text content of the post
     */
    String getContent();

    /**
     * Get the optional location of the post.
     *
     * @return  {@code Optional} object that contains the location information.
     */
    Optional<String> getLocation();
}
