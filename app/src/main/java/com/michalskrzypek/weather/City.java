package com.michalskrzypek.weather;

import android.graphics.Bitmap;

public class City {

    private String name;
    private String state;
    private String country;
    private String weather;
    private String temperature;
    private Bitmap icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return getName() + ", " + getState() + ", " + getCountry();
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

        public Builder name(String name) {
            this.city.name = name;
            return this;
        }

        public Builder state(String state) {
            this.city.state= state;
            return this;
        }

        public Builder country(String country) {
            this.city.country= country;
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
