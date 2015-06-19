package org.apereo.finaid.dao.mock;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.apereo.finaid.dao.ITermDao;
import org.apereo.finaid.mvc.models.Term;

import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//Mock data
import javax.annotation.PostConstruct;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.core.io.ClassPathResource;

@Repository
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.INTERFACES)
public class MockTermDao implements ITermDao {

  protected final Log logger = LogFactory.getLog(getClass());
  private final String MOCK_DATA_PATH = "mock-data/mock-terms.json";
  private TreeMap<String, Term> terms;

  @PostConstruct
  public void init() {
    ClassPathResource mockData = new ClassPathResource(MOCK_DATA_PATH);
    try {
      ObjectMapper mapper = new ObjectMapper();
      terms = mapper.readValue(
          mockData.getInputStream(),
          new TypeReference<TreeMap<String, Term>>(){}
        );
    } catch (JsonGenerationException|JsonMappingException e) {
      logger.error(e);
    } catch (IOException e) {
      logger.error(e);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  public MockTermDao() { } //empty constructor

  @Cacheable(value="termDaoCache", key="{ #root.methodName, #id }")
  public Map<String, Term> getAllTerms(long id) {
    return terms.descendingMap();
  }

  // Compares the current Term with the List of all Terms and returns the oldest Term that
  // has not already passed. For example: the current term if it exists, the soonest
  // future Term if it doesn't, or null.
  @Cacheable(value="termDaoCache", key="{ #root.methodName, #id }")
  public Term getLatestTerm(long id) {
    return terms.get( terms.lastKey() );
  }

  public boolean doesTermExist(String termCode, long id) {
    return terms.containsKey(termCode);
  }

}
