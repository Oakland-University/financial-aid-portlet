package org.apereo.finaid.mvc.portlet;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apereo.finaid.mvc.IViewSelector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.ModelAndView;

/**
 * HelpController is a simple controller that displays the help interface
 */
@Controller
@RequestMapping("HELP")
public class HelpController {

    protected final Log logger = LogFactory.getLog(getClass());

    private IViewSelector viewSelector;

    @Autowired(required = true)
    public void setViewSelector(IViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }

    /**
     * Returns the help view.  The help view is a very simple JSP, so we don't
     * both returning a model.
     *
     * @param request
     * @param response
     * @return
     */
	@RenderMapping
	public ModelAndView showHelpView(final RenderRequest request, final RenderResponse response) {
        // determine if the request represents a mobile browser and set the
        // view name accordingly
        final boolean isMobile = viewSelector.isMobile(request);
        final String viewName = isMobile ? "help-jQM" : "help";
        final ModelAndView mav = new ModelAndView(viewName);

        if(logger.isDebugEnabled()) {
            logger.debug("Using view name " + viewName + " for help view");
        }
        return mav;
    }

    @ActionMapping
    public void doAction() {
        // no-op action mapping to prevent accidental calls to this URL from
        // crashing the portlet
    }
}
