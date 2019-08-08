package edu.cmu.cs.cs214.hw5.plugins.dataplugin.twitterplugin;

import edu.cmu.cs.cs214.hw5.framework.core.Post;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TwitterPluginTest {
    private TwitterPlugin twitterPlugin;
    private Map<String, String> parameters;

    @Before
    public void setUp() {
        twitterPlugin = new TwitterPlugin();
        parameters = new HashMap<>();
        parameters.put("User", "CarnegieMellon");
        parameters.put("PostNum", "20");

    }

    @Test
    public void testConnect() {
        boolean isConnect = twitterPlugin.connect(parameters);
        assertTrue(isConnect);
    }

    @Test
    public void testGetAccountName() {
        twitterPlugin.connect(parameters);
        assertEquals("CarnegieMellon", twitterPlugin.getAccountName());
    }

    @Test
    public void testQuery() {
        twitterPlugin.connect(parameters);
        List<Post> posts = twitterPlugin.queryPosts(parameters);
        assertNotNull(posts);
        assertEquals(20, posts.size());
    }
}