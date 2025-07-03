package net.hiddenpass.hiddenpass.models;

import java.math.BigDecimal;
import java.util.List;

public class SecurePasswordEntity {
    private double entropy;
    private List<String> feedBack;
    private BigDecimal guesses;

    public SecurePasswordEntity(double entropy, List<String> feedBack, BigDecimal guesses) {
        this.entropy = entropy;
        this.feedBack = feedBack;
        this.guesses = guesses;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public List<String> getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(List<String> feedBack) {
        this.feedBack = feedBack;
    }

    public BigDecimal getGuesses() {
        return guesses;
    }

    public void setGuesses(BigDecimal guesses) {
        this.guesses = guesses;
    }
}
