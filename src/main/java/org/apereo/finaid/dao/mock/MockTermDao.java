package org.apereo.finaid.dao.mock;

import java.util.Map;
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

@Repository
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.INTERFACES)
public class MockTermDao implements ITermDao {

  protected final Log logger = LogFactory.getLog(getClass());

  public MockTermDao() { } //empty constructor

  @Cacheable(value="termDaoCache", key="{ #root.methodName, #id }")
  public Map<String, Term> getAllTerms(long id) {
    return Collections.emptyMap();
  }

  // Compares the current Term with the List of all Terms and returns the oldest Term that
  // has not already passed. For example: the current term if it exists, the soonest
  // future Term if it doesn't, or null.
  @Cacheable(value="termDaoCache", key="{ #root.methodName, #id }")
  public Term getLatestTerm(long id) {
    return new Term();
  }

  public boolean doesTermExist(String termCode, long id) {
    return Boolean.TRUE;
  }

}
