package com.michalskrzypek.jsondemo;

import android.graphics.Bitmap;

public class City {

    private String fullName;
    private String weather;
    private String temperature;
    private Bitmap icon;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static class Builder {

        private City city;

        public Builder() {
            this.city = new City();
        }

        public City build() {
            return this.city;
        }

        public Builder fullName(String fullName) {
            this.city.fullName = fullName;
            return this;
        }

        public Builder weather(String weather) {
            this.city.weather = weather;
            return this;
        }

        public City.Builder temperature(String temperature) {
            this.city.temperature = temperature;
            return this;
        }

        public City.Builder icon(Bitmap icon) {
            this.city.icon = icon;
            return this;
        }
    }
}
