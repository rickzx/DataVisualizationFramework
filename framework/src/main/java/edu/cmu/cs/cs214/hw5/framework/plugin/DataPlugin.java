package edu.cmu.cs.cs214.hw5.framework.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.Post;

import java.util.List;
import java.util.Map;

/**
 * The {@code DataPlugin} interface outlines the core methods of a data
 * input plugin. The {@code DataPlugin} extracts data from some source and
 * makes it available for processing by the data visualization framework.
 * <p>
 * The framework will first call {@code connect} method to establish a connection
 * to the source. If the connection is successful, the framework will then call
 * {@code queryPost} to get a list of posts.
 *
 * @author Team6
 */
public interface DataPlugin extends Plugin {
    /**
     * Connects to the source specified by {@code parameters} map
     *
     * @param parameters    The parameters used to connect to the source
     * @return              {@code true} if the connection is successful, otherwise {@code false}
     */
    boolean connect(Map<String, String> parameters);

    /**
     * Gets the name of the account
     *
     * @return  the name of the account
     */
    String getAccountName();

    /**
     * Queries posts from the established connection. The specifications of the query
     * is passed in by a {@code parameters} map
     *
     * @param parameters    The parameters that specifies the details of the query
     * @return              A list of posts queried from the source
     */
    List<Post> queryPosts(Map<String, String> parameters);
}
