/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apereo.finaid.mvc.portlet;

import java.util.Map;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apereo.finaid.mvc.models.Term;
import org.apereo.finaid.mvc.models.FinancialInfo;
import org.apereo.finaid.dao.ITermDao;
import org.apereo.finaid.dao.IFinancialInfo;
import org.apereo.finaid.service.StudentIdService;
import org.apereo.finaid.mvc.IViewSelector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Main portlet view.
 */
@Controller
@RequestMapping("VIEW")
public class MainController {

    protected final Log logger = LogFactory.getLog(getClass());

    private IViewSelector viewSelector;

    @Autowired
    private ITermDao termDao;

    @Autowired
    private IFinancialInfo financialInfoDao;

    @Autowired
    private StudentIdService studentIdService;

    @Autowired(required = true)
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }

    @RenderMapping
    public ModelAndView showMainView(final RenderRequest request, final RenderResponse response) {
        // determine if the request represents a mobile browser and set the
        // view name accordingly
        final ModelAndView mav = new ModelAndView("main");

        final long id = studentIdService.getId(request);

        // Get all terms for the dropdown
        Map<String, Term> terms = termDao.getAllTerms(id);

        // Get the latest term to display initially
        Term latestTerm = termDao.getLatestTerm(id);

        if (latestTerm != null) {
            List<String> messages = financialInfoDao.getMessages(latestTerm.getCode(), id);
            mav.addObject("messages", messages);
        }

        // Add objects to the view
        mav.addObject("latestTerm", latestTerm);
        mav.addObject("terms", terms);

        if(logger.isDebugEnabled()) {
            logger.debug("Rendering main view");
        }

        return mav;
    }

    @ResourceMapping
    public ModelAndView getFinancialInfoJSON(@RequestParam("code") String code,
            ResourceRequest request, ResourceResponse response) {
        ModelAndView model = new ModelAndView("json");

      final long id = studentIdService.getId(request);

      if (!termDao.doesTermExist(code, id)) {
        throw new IllegalArgumentException(
            String.format("Cannot process TERM: %s for USER: %s", 
              code, request.getRemoteUser())
            );
      }

      FinancialInfo financialInfo = new FinancialInfo();
      financialInfo.setAwards(financialInfoDao.getAwards(code, id));
      financialInfo.setHolds(financialInfoDao.getHolds(code, id));
      financialInfo.setProgress(financialInfoDao.getProgress(code, id));

      model.addObject("financialInfo", financialInfo);

      return model; // returned as JavaScript Object Notation
    }



    @ActionMapping
    public void doAction() {
        // no-op action mapping to prevent accidental calls to this URL from
        // crashing the portlet
    }

}
