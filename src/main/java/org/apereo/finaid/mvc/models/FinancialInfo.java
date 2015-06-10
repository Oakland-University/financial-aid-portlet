package org.apereo.finaid.mvc.models;

import java.util.List;

import org.apereo.finaid.mvc.models.Award;
import org.apereo.finaid.mvc.models.Hold;
import org.apereo.finaid.mvc.models.Progress;

public class FinancialInfo {

    private List<Award> awards;
    private List<Hold> holds;
    private Progress progress;

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setHolds(List<Hold> holds) {
        this.holds = holds;
    }

    public List<Hold> getHolds() {
        return holds;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public Progress getProgress() {
        return progress;
    }

}
