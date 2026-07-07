package util;

import java.util.Objects;

public class UrlMethode {
    private String url;
    private String methode;

    public UrlMethode(String url, String methode) {
        this.url = url;
        this.methode = methode.toUpperCase();
    }

    public String getUrl() {
        return url;
    }

    public String getMethode() {
        return methode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UrlMethode)) {
            return false;
        }

        UrlMethode autre = (UrlMethode) obj;
        return Objects.equals(url, autre.url) && Objects.equals(methode, autre.methode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, methode);
    }
}
