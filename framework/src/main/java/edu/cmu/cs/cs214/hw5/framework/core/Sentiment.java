package edu.cmu.cs.cs214.hw5.framework.core;

/**
 * The {@code Sentiment} class represents the sentiment analyzed by NLP API.
 * A sentiment indicates attitude and the emotional state of the writer.
 * It consists of a score (-1.0 ~ 1.0), and a magnitude (0 ~ +inf).
 *
 * @author Team6
 */
public final class Sentiment {
    private final double score;
    private final double magnitude;

    private static final double TYPE_POINT_1 = -1.0;
    private static final double TYPE_POINT_2 = -0.6;
    private static final double TYPE_POINT_3 = -0.2;
    private static final double TYPE_POINT_4 = 0.2;
    private static final double TYPE_POINT_5 = 0.6;

    /**
     * Initializes a {@code Sentiment} object from a score and a magnitude
     *
     * @param score         The score of the sentiment, from -1.0 to 1.0
     * @param magnitude     The magnitude of the sentiment, from 0.0 to positive infinity
     */
    public Sentiment(double score, double magnitude) {
        this.score = score;
        this.magnitude = magnitude;
    }

    /**
     * Get the score of the sentiment
     *
     * @return  the score of the sentiment
     */
    public double getScore() {
        return score;
    }

    /**
     * Get the magnitude of the sentiment
     *
     * @return  the magnitude of the sentiment
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * Get the sentiment type of the sentiment. Sentiment type
     * could be very negative, negative, neutral, positive, very positive
     *
     * @return  the sentiment type of the sentiment
     */
    public SentimentType getSentimentType() {
        if (TYPE_POINT_1 <= score && score < TYPE_POINT_2)
            return SentimentType.VERY_NEGATIVE;
        else if (TYPE_POINT_2 <= score && score < TYPE_POINT_3)
            return SentimentType.NEGATIVE;
        else if (TYPE_POINT_3 <= score && score < TYPE_POINT_4)
            return SentimentType.NEUTRAL;
        else if (TYPE_POINT_4 <= score && score < TYPE_POINT_5)
            return SentimentType.POSITIVE;
        else
            return SentimentType.VERY_POSITIVE;
    }
}
