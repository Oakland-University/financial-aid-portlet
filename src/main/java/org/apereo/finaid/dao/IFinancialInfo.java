package org.apereo.finaid.dao;

import java.util.List;

import org.apereo.finaid.mvc.models.FinancialInfo;
import org.apereo.finaid.mvc.models.Progress;
import org.apereo.finaid.mvc.models.Award;
import org.apereo.finaid.mvc.models.Term;
import org.apereo.finaid.mvc.models.Hold;

public interface IFinancialInfo {

  public List<Award> getAwards(String termCode, long id);

  public List<Hold> getHolds(String termCode, long id);

  public Progress getProgress(String termCode, long id);

  public List<String> getMessages(String termCode, long id);

}
