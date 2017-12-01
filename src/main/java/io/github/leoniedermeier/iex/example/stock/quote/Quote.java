package io.github.leoniedermeier.iex.example.stock.quote;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

public class Quote {

    public static class LongToLocalDateTimeConverter extends StdConverter<Long, LocalDateTime> {

        @Override
        public LocalDateTime convert(final Long value) {
            return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    private Double price;

    // der Wert wird als Millisekunden geliefert. Am einfachsten kann man das mit
    // einem Converter umwandeln
    @JsonDeserialize(converter = io.github.leoniedermeier.iex.example.stock.quote.Quote.LongToLocalDateTimeConverter.class)
    private LocalDateTime time;

    public Quote() {
        super();
    }

    public Quote(final LocalDateTime time, final Double price) {
        super();
        this.time = time;
        this.price = price;
    }

    public Double getPrice() {
        return this.price;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setPrice(final Double price) {
        this.price = price;
    }

    public void setTime(final LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Quote [price=" + this.price + ", time=" + this.time + "]";
    }
}
