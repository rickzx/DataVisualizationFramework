package edu.cmu.cs.cs214.hw5.framework.core;

/**
 * The {@code SentimentType} enum class represents the type of the sentiment.
 * Sentiment type could be very negative, negative, neutral, positive, very positive.
 *
 * @author Team6
 */
public enum SentimentType {
    VERY_NEGATIVE("Very Negative (-1.0 ~ -0.6)"),
    NEGATIVE("Negative (-0.6 ~ -0.2)"),
    NEUTRAL("Neutral (-0.2 ~ 0.2)"),
    POSITIVE("Positive (0.2 ~ 0.6)"),
    VERY_POSITIVE("Very Positive (0.6 ~ 1.0)");

    private String string;

    SentimentType(String repr){string = repr;}

    @Override
    public String toString() {
        return string;
    }
}
