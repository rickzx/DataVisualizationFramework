package edu.cmu.cs.cs214.hw5.plugins.dataplugin.twitterplugin;

import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPanel;
import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFramework;
import edu.cmu.cs.cs214.hw5.framework.core.Post;
import edu.cmu.cs.cs214.hw5.framework.core.SimplePost;
import edu.cmu.cs.cs214.hw5.framework.plugin.DataPlugin;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@code TwitterPlugin} is a data plugin that reads Tweets from
 * a Twitter account. The plugin reads Twitter account name and queries
 * Tweets from the Twitter4j API.
 *
 * @author Team6
 */
public class TwitterPlugin implements DataPlugin {
    private Twitter twitter;
    private DataVisualizationFramework framework;
    private User user;
    private ControlPanel controlPanel;

    private static final int MAX_TWEET_PER_PAGE = 50;

    /**
     * Initializes the {@code TwitterPlugin} object by setting up the Twitter4j instance.
     */
    public TwitterPlugin() {
        twitter = getTwitterInstance();
    }

    private static Twitter getTwitterInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Config.CONSUMER_KEY)
                .setOAuthConsumerSecret(Config.CONSUMER_SECRET)
                .setOAuthAccessToken(Config.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(Config.ACCESS_TOKEN_SECRET);

        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    @Override
    public String getPluginName() {
        return "Twitter Plugin";
    }

    @Override
    public void onRegister(DataVisualizationFramework dataVisualizationFramework) {
        this.framework = dataVisualizationFramework;
        this.controlPanel = new TwitterControlPanel("Twitter Control Panel", this);
        controlPanel.addControlPoint(new TwitterInputModule("Put Twitter Account Name Here"));
        controlPanel.addControlPoint(new TwitterPostNumModule(MAX_TWEET_PER_PAGE));
    }

    @Override
    public boolean connect(Map<String, String> parameters) {
        try {
            user = twitter.showUser(parameters.get("User"));
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getAccountName() {
        return user.getScreenName();
    }

    @Override
    public List<Post> queryPosts(Map<String, String> parameters) {
        List<Status> statuses = new ArrayList<>();
        int postNum = Integer.parseInt(parameters.get("PostNum"));
        int iters = postNum / MAX_TWEET_PER_PAGE;
        int rem = postNum % MAX_TWEET_PER_PAGE;
        Paging p = new Paging(1, MAX_TWEET_PER_PAGE);

        try {
            for(int i = 1; i < iters + 1; i++) {
                p.setPage(i);
                statuses.addAll(twitter.getUserTimeline(user.getId(), p));
            }

            p.setPage(iters + 1);
            statuses.addAll(twitter.getUserTimeline(user.getId(), p).subList(0, rem));


        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return statuses.stream().map(s -> statusToPost(s))
                .collect(Collectors.toList());
    }

    private Post statusToPost(Status status) {
        if (status.getPlace() != null) {
            return new SimplePost(status.getCreatedAt(), status.getText(), status.getPlace().toString());
        } else {
            return new SimplePost(status.getCreatedAt(), status.getText());
        }
    }

    @Override
    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    @Override
    public DataVisualizationFramework getFramework() {
        return framework;
    }

    @Override
    public String toString() {
        return getPluginName();
    }
}
