package edu.cmu.cs.cs214.hw5.plugins.dataplugin.amazonplugin;

import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFramework;
import edu.cmu.cs.cs214.hw5.framework.core.Post;
import edu.cmu.cs.cs214.hw5.framework.core.SimplePost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import edu.cmu.cs.cs214.hw5.framework.plugin.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPanel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The {@code AmazonPlugin} is a data plugin that reads the Amazon
 * product reviews. The plugin reads an ASIN or ISBN number which
 * can be found in the Product Details section on the Amazon product
 * page, and queries product reviews by web scrapping.
 *
 * @author Team6
 */
public class AmazonPlugin implements DataPlugin {
    private DataVisualizationFramework framework;
    private Document doc;
    private String asin;
    private ControlPanel controlPanel;

    private static final int DEFAULT_REVIEW_NUM = 20;

    @Override
    public String getPluginName() {
        return "Amazon Review Plugin";
    }

    @Override
    public void onRegister(DataVisualizationFramework dataVisualizationFramework) {
        this.framework = dataVisualizationFramework;
        this.controlPanel = new AmazonControlPanel("Amazon Control Panel", this);
        this.controlPanel.addControlPoint(
                new AmazonASINModule("10-character alphanumeric"));
        this.controlPanel.addControlPoint(
                new AmazonReviewNumModule(DEFAULT_REVIEW_NUM)
        );
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
    public boolean connect(Map<String, String> parameters) {
        String asin = parameters.get("ASIN/ISBN");
        String url = "https://www.amazon.com/product-review/" + asin;
        try {
            doc = Jsoup.connect(url).get();
            this.asin = asin;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getAccountName() {
        Element link = doc.select("a[data-hook='product-link']").first();
        return link.text();
    }

    @Override
    public List<Post> queryPosts(Map<String, String> parameters) {
        List<Post> posts = new ArrayList<>();
        int reviewNum = Integer.parseInt(parameters.get("ReviewNum"));
        int page = 1;

        while (true) {
            String url = String.format(
                    "https://www.amazon.com/product-reviews/%s/ref=cm_cr_getr_d_paging_btm_%d?pageNumber=%d",
                    asin, page, page);
            try {
                Document doc = Jsoup.connect(url).get();
                Elements reviewElements = doc.select(".review");
                if(reviewElements == null || reviewElements.isEmpty())
                    break;

                boolean isFull = false;

                for(Element reviewElement: reviewElements) {
                    if(posts.size() == reviewNum) {
                        isFull = true;
                        break;
                    }
                    Element titleElement = reviewElement.select(".review-title").first();
                    Element textElement = reviewElement.select(".review-text").first();
                    Element dateElement = reviewElement.select(".review-date").first();

                    if (titleElement != null && textElement != null && dateElement != null) {
                        posts.add(elementsToPost(titleElement, textElement, dateElement));
                    }
                }

                if (isFull)
                    break;
                else
                    page++;
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    private Post elementsToPost(Element titleElement, Element textElement, Element dateElement) throws ParseException {
        String content = titleElement.text() + textElement.text();
        SimpleDateFormat format = new SimpleDateFormat("MMMMM d, yyyy", Locale.US);
        Date date = format.parse(dateElement.text());
        return new SimplePost(date, content);
    }

    @Override
    public String toString() {
        return getPluginName();
    }
}
