package org.apereo.finaid.dao.mock;

import java.util.List;
import java.util.Collections;

import org.apereo.finaid.dao.IFinancialInfo;
import org.apereo.finaid.mvc.models.FinancialInfo;
import org.apereo.finaid.mvc.models.Progress;
import org.apereo.finaid.mvc.models.Award;
import org.apereo.finaid.mvc.models.Term;
import org.apereo.finaid.mvc.models.Hold;

import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Repository
public class MockFinancialInfo implements IFinancialInfo {

  protected final Log logger = LogFactory.getLog(getClass());

  @Cacheable(value="financialAidCache", key="{ #root.methodName, #term, #id }")
  public Progress getProgress(String term, long id) {
    return new Progress("Your status has not been reviewed yet");
  }

  @Cacheable(value="financialAidCache", key="{ #root.methodName, #term, #id }")
  public List<Award> getAwards(String term, long id) {
    return Collections.emptyList();
  }

  @Cacheable(value="financialAidCache", key="{ #root.methodName, #term, #id }")
  public List<Hold> getHolds(String term, long id) {
    return Collections.emptyList();
  }

  @Cacheable(value="financialAidCache", key="{ #root.methodName, #term, #id }")
  public List<String> getMessages(String term, long id) {
    return Collections.emptyList();
  }

}
