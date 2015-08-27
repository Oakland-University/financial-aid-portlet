package org.apereo.finaid.dao.mock;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import javax.annotation.PostConstruct;

import org.apereo.finaid.dao.IFinancialInfo;
import org.apereo.finaid.mvc.models.FinancialInfo;
import org.apereo.finaid.mvc.models.Progress;
import org.apereo.finaid.mvc.models.Award;
import org.apereo.finaid.mvc.models.Term;
import org.apereo.finaid.mvc.models.Hold;

import org.springframework.core.io.ClassPathResource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

@Repository
public class MockFinancialInfo implements IFinancialInfo {

  protected final Log logger = LogFactory.getLog(getClass());
  private final String MOCK_DATA_PATH = "mock-data/mock-info.json";
  private Map<String, FinancialInfo> financialInfo;

  @PostConstruct
  public void init() {
    ClassPathResource mockData = new ClassPathResource(MOCK_DATA_PATH);
    try {
      ObjectMapper mapper = new ObjectMapper();
      financialInfo = mapper.readValue(
          mockData.getInputStream(),
          new TypeReference<Map<String, FinancialInfo>>(){}
        );
    } catch (JsonGenerationException|JsonMappingException e) {
      logger.error(e);
    } catch (IOException e) {
      logger.error(e);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  @Cacheable(value="financialAidCache", key="{ #root.methodName, #term, #id }")
  public Progress getProgress(String term, long id) {
    return financialInfo.get(term).getProgress();
  }

  @Cacheable(value="financialAidCache", key="{ #root.methodName, #term, #id }")
  public List<Award> getAwards(String term, long id) {
    return financialInfo.get(term).getAwards();
  }

  @Cacheable(value="financialAidCache", key="{ #root.methodName, #term, #id }")
  public List<Hold> getHolds(String term, long id) {
    return financialInfo.get(term).getHolds();
  }

  @Cacheable(value="financialAidCache", key="{ #root.methodName, #term, #id }")
  public List<String> getMessages(String term, long id) {
    return Collections.emptyList();
  }

}
