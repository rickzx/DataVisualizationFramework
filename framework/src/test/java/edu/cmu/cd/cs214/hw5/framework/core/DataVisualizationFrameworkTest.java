package edu.cmu.cd.cs214.hw5.framework.core;

import edu.cmu.cs.cs214.hw5.framework.core.*;
import edu.cmu.cs.cs214.hw5.framework.nlp.GoogleNLP;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DataVisualizationFrameworkTest {
    private Post post;
    private AnalyzedPost analyzedPost;
    private Date date;
    private String text;
    private String location;
    private GoogleNLP googleNLP;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        date = new Date();
        text = "Hello world!";
        location = "Pittsburgh";
        googleNLP = new GoogleNLP(Config.CREDENTIAL_FILE);
    }

    @Test
    public void testCreatePost()  {
        post = new SimplePost(date, text, location);
        assertNotNull(post);
        assertEquals(date, post.getTimeStamp());
        assertEquals(text, post.getContent());
        assertEquals(location, post.getLocation().orElse(null));
    }

    @Test
    public void testAnalyzePost() {
        post = new SimplePost(date, text, location);
        analyzedPost = new AnalyzedPost(post, googleNLP.analyzeSentiment(post.getContent()));
        assertTrue(analyzedPost.getSentiment().getScore() > -1.0);
        assertTrue(analyzedPost.getSentiment().getScore() < 1.0);
        assertTrue(analyzedPost.getSentiment().getMagnitude() > 0);
    }

    @Test
    public void testLoadAccount() {
        post = new SimplePost(date, text, location);
        analyzedPost = new AnalyzedPost(post, googleNLP.analyzeSentiment(post.getContent()));
        List<AnalyzedPost> analyzedPosts = new ArrayList<>();
        analyzedPosts.add(analyzedPost);
        Account account = new Account("User", analyzedPosts);
        assertNotNull(account);
        assertEquals(account.getAccountName(), "User");
        assertEquals(account.getPosts(), analyzedPosts);
    }
}