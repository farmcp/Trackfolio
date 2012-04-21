package net.cs76.projects.student80815298;

public class Stock {
	String ticker;
	double price;
	String change;
	String chartUrlSmall;
	String chartUrlLarge;
	long lastUpdated;
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double d) {
		this.price = d;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getChartUrlSmall() {
		return chartUrlSmall;
	}
	public void setChartUrlSmall(String chartUrlSmall) {
		this.chartUrlSmall = chartUrlSmall;
	}
	public String getChartUrlLarge() {
		return chartUrlLarge;
	}
	public void setChartUrlLarge(String chartUrlLarge) {
		this.chartUrlLarge = chartUrlLarge;
	}
	public long getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
