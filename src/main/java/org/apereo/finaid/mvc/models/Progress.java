package org.apereo.finaid.mvc.models;

import org.apereo.finaid.mvc.models.Term;

public class Progress {

    private String status;

    public Progress() { }

    public Progress(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
