package org.apereo.finaid.mvc.models;

public class Hold {

    private String req;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getUrl() {
        return this.url;
    }

    public String getReq() {
        return this.req;
    }

}
