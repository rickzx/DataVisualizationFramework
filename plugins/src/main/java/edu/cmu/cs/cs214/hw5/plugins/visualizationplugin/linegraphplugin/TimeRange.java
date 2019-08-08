package edu.cmu.cs.cs214.hw5.plugins.visualizationplugin.linegraphplugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The {@code TimeRange} class represents a time range with a start
 * and an end date
 *
 * @author Team6
 */
public final class TimeRange {
    private Date start;
    private Date end;

    /**
     * Initializes a {@code TimeRange} object with a start date and an end date
     *
     * @param start     start date
     * @param end       end date
     */
    TimeRange(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    /**
     *
     * @return  the start date
     */
    public Date getStart() {
        return new Date(start.getTime());
    }

    /**
     *
     * @return  the end date
     */
    public Date getEnd() {
        return new Date(end.getTime());
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return dateFormat.format(getStart()) + "," + dateFormat.format(getEnd());
    }
}
