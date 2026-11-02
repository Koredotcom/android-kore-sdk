package kore.botssdk.models;

import java.util.ArrayList;
import java.util.List;

public class EventIceServersModel
{
    // The event API has used both `url` and `urls`; `urls` may be a string or an array.
    // Object keeps Gson from failing when the server sends either JSON shape.
    private Object url;
    private Object urls;
    private String username;
    private String credential;

    public String getUrl() {
        return firstUrl(url);
    }

    public String getCredential() {
        return credential;
    }

    public String getUrls() {
        return firstUrl(urls);
    }

    public String getUsername() {
        return username;
    }

    public List<String> getIceUrls() {
        ArrayList<String> result = new ArrayList<>();
        appendUrls(result, urls);
        appendUrls(result, url);
        return result;
    }

    private static String firstUrl(Object value) {
        if (value instanceof String) {
            String url = ((String) value).trim();
            return url.isEmpty() ? null : url;
        }
        if (value instanceof List) {
            for (Object item : (List<?>) value) {
                if (item instanceof String && !((String) item).trim().isEmpty()) {
                    return ((String) item).trim();
                }
            }
        }
        return null;
    }

    private static void appendUrls(List<String> destination, Object value) {
        if (value instanceof String) {
            String url = ((String) value).trim();
            if (!url.isEmpty() && !destination.contains(url)) destination.add(url);
        } else if (value instanceof List) {
            for (Object item : (List<?>) value) {
                if (item instanceof String) {
                    String url = ((String) item).trim();
                    if (!url.isEmpty() && !destination.contains(url)) destination.add(url);
                }
            }
        }
    }
}
