package org.apereo.finaid;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;


import org.apereo.finaid.mvc.models.FinancialInfo;
import org.apereo.finaid.dao.IFinancialInfo;
import org.apereo.finaid.mvc.models.ObjectFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service("mockstudent")
public class MockStudentDao implements IFinancialInfo, InitializingBean{
  private FinancialInfo marshalMockStudent;
  private FinancialInfo unmarshalMockStudent; 
  private static final ObjectFactory objectfactory = new ObjectFactory(); 
  public Resource mockData = new ClassPathResource("/mockdata/MockStudent.xml");

  public void setMockData(Resource mockData){
    this.mockData = mockData; 
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    JAXBContext jaxbContext = JAXBContext.newInstance(resources.mockdata.MockStudent.xml.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

    this.unmarshalMockStudent = (FinancialInfo) unmarshaller.unmarshal(mockData.getInputStream());
  }

  @Override
  public List<Award> getAwards(String termCode, long id){
    return this.unmarshalMockStudent;
  }

  @Override
  public List<Hold> getHolds(String termCode, long id){
    return this.unmarshalMockStudent;
  }

  @Override
  public Progress getProgress(String termCode, long id){
    return this.unmarshalMockStudent;
  }

  @Override
  public List<String> getMessages(String termCode, long id){
    return this.unmarshalMockStudent; 
  }

}

