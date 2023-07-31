package shgo.innowise.trainee.songmicroservice.enricherservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * Enum for parsing dates with different precision from Spotify API.
 */
@Getter
@AllArgsConstructor
public enum DatePrecision {

    YEAR("year", new DateTimeFormatterBuilder().appendPattern("yyyy")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter()),
    MONTH("month", new DateTimeFormatterBuilder().appendPattern("yyyy-MM")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter()),
    DAY("day", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private final String code;
    private final DateTimeFormatter formatter;

    /**
     * Get value of date precision by code.
     *
     * @param code code of date precision object
     * @return date precision
     */
    public static DatePrecision valueOfByCode(String code) {
        for (DatePrecision datePrecision : values()) {
            if (datePrecision.code.equals(code)) {
                return datePrecision;
            }
        }
        throw new IllegalArgumentException("Object with code '" + code + "' cannot be found");
    }
}
