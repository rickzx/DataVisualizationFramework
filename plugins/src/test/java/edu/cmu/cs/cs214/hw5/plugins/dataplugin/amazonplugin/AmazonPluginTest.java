package edu.cmu.cs.cs214.hw5.plugins.dataplugin.amazonplugin;

import edu.cmu.cs.cs214.hw5.framework.core.Post;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AmazonPluginTest {
    private AmazonPlugin amazonPlugin;
    private Map<String, String> parameters;

    @Before
    public void setUp() {
        amazonPlugin = new AmazonPlugin();
        parameters = new HashMap<>();
        parameters.put("ASIN/ISBN", "0131872486");
        parameters.put("ReviewNum", "10");

    }
    @Test
    public void testConnect() {
        boolean isConnect = amazonPlugin.connect(parameters);
        assertTrue(isConnect);
    }

    @Test
    public void testGetAccountName() {
        amazonPlugin.connect(parameters);
        assertEquals("Thinking in Java (4th Edition)", amazonPlugin.getAccountName());
    }

    @Test
    public void testQueryPosts() {
        amazonPlugin.connect(parameters);
        List<Post> posts = amazonPlugin.queryPosts(parameters);
        assertNotNull(posts);
        assertEquals(10, posts.size());
    }
}