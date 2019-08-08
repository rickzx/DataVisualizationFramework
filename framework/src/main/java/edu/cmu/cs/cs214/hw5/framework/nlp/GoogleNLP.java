package edu.cmu.cs.cs214.hw5.framework.nlp;

// Imports the Google Cloud client library
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The {@code GoogleNLP} class is used by the framework to perform sentiment
 * analysis on posts. The {@code GoogleNLP} class uses Google Natural Language API
 * for sentiment analysis.
 *
 */
public class GoogleNLP {
    private LanguageServiceClient language;

    /**
     * Initializes the {@code GoogleNLP} from a credential file. The credential
     * is provided by Google Cloud as an authorization for using their Google
     * Natural Language service.
     *
     * @param credentialFile    Name of the credential file. Should be in json format.
     * @throws IOException      if error occurs when reading the credential file
     * @throws URISyntaxException   if error occurs when reading the credential file
     */
    public GoogleNLP(String credentialFile) throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = new URI(classLoader.getResource(credentialFile).toString());
        File file = new File(uri.getPath());

        GoogleCredentials myCredentials = GoogleCredentials.fromStream(new FileInputStream(file))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        LanguageServiceSettings languageServiceSettings =
                LanguageServiceSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(myCredentials))
                        .build();
        language = LanguageServiceClient.create(languageServiceSettings);
    }

    /**
     * Performs sentiment analysis on text contents.
     *
     * @param text      The text to be analyzed
     * @return          {@code Sentiment} of the text
     */
    public edu.cmu.cs.cs214.hw5.framework.core.Sentiment analyzeSentiment(String text) {
        Document doc = Document.newBuilder()
                .setContent(text).setType(Type.PLAIN_TEXT).build();
        Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
        return new edu.cmu.cs.cs214.hw5.framework.core.Sentiment(sentiment.getScore(), sentiment.getMagnitude());
    }
}
