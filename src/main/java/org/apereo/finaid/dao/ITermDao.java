package org.apereo.finaid.dao;

import java.util.Map;

import org.apereo.finaid.mvc.models.Term;

public interface ITermDao {

  public Map<String, Term> getAllTerms(long id);

  public Term getLatestTerm(long id);

  public boolean doesTermExist(String termCode, long id);

}
