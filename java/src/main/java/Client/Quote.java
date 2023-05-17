/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import yahoofinance.histquotes.Interval;
import okhttp3.Request;


public class Quote {
    private String symbol;
    private BigDecimal price;
    private BigDecimal previousClose;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private Long volume;
    private Date timestamp;
    private List<HistoricalQuote> historicalQuotes;

    public Quote(String symbol, BigDecimal price, BigDecimal previousClose, BigDecimal open, BigDecimal high, BigDecimal low, Long volume, Date timestamp, List<HistoricalQuote> historicalQuotes) {
        this.symbol = symbol;
        this.price = price;
        this.previousClose = previousClose;
        this.open = open;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.timestamp = timestamp;
        this.historicalQuotes = historicalQuotes;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getChange() {
        return price.subtract(previousClose);
    }

    public BigDecimal getChangePercent() {
        return price.subtract(previousClose).divide(previousClose, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getDayRange() {
        return high.subtract(low);
    }

    public BigDecimal getYearRange() {
        return high.subtract(low);
    }

    public BigDecimal getMarketCap() {
        return price.multiply(BigDecimal.valueOf(volume));
    }

    public List<HistoricalQuote> getHistoricalQuotes() {
        return historicalQuotes;
    }

    public List<HistoricalQuote> getHistoricalQuotes(Date startDate, Date endDate, Interval interval) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);
        String intervalStr = interval.toString().toLowerCase();

        String query = String.format("https://query1.finance.yahoo.com/v8/finance/chart/%s?interval=%s&period1=%s&period2=%s",
                symbol, intervalStr, startDate.getTime() / 1000, endDate.getTime() / 1000);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(query)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        String responseBody = response.body().toString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode chartNode = rootNode.path("chart");
        JsonNode resultNode = chartNode.path("result");
        JsonNode quoteNode = resultNode.get(0);
        JsonNode timestampNode = quoteNode.path("timestamp");
        JsonNode indicatorsNode = quoteNode.path("indicators");
        JsonNode quoteDataNode = indicatorsNode.get(0).path("quote");
        List<HistoricalQuote> historicalQuotes = new ArrayList<>();

        for (int i = 0; i < timestampNode.size(); i++) {
            Long timestamp = timestampNode.get(i).asLong() * 1000;
            Date date = new Date(timestamp);
            BigDecimal open = quoteDataNode.get(i).get("open").decimalValue();
            BigDecimal close = quoteDataNode.get(i).get("close").decimalValue();
            BigDecimal high = quoteDataNode.get(i).get("high").decimalValue();
            BigDecimal low = quoteDataNode.get(i).get("low").decimalValue();
            Long volume = quoteDataNode.get(i).get("volume").asLong();
            historicalQuotes.add(new HistoricalQuote(date, open, close, high, low, volume));
        }

        return historicalQuotes;
    }


    public static class HistoricalQuote {
        private Date date;
        private BigDecimal open;
        private BigDecimal close;
        private BigDecimal high;
        private BigDecimal low;
        private Long volume;

        public HistoricalQuote(Date date, BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low, Long volume) {
            this.date = date;
            this.open = open;
            this.close = close;
            this.high = high;
            this.low = low;
            this.volume = volume;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getClose() {
            return close;
        }

        public void setClose(BigDecimal close) {
            this.close = close;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }
    }
}
