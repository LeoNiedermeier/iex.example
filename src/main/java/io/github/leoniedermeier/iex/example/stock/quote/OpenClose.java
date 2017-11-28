package io.github.leoniedermeier.iex.example.stock.quote;

public class OpenClose {

    private Quote open;
    
    private Quote close;
    
    public void setOpen(Quote open) {
        this.open = open;
    }
    
    public void setClose(Quote close) {
        this.close = close;
    }
    public Quote getOpen() {
        return open;
    }
    
    public Quote getClose() {
        return close;
    }

    @Override
    public String toString() {
        return "OpenClose [open=" + open + ", close=" + close + "]";
    }
    
    
}
