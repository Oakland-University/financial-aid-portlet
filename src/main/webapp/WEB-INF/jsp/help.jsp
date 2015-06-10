<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>

<h1>Financial Aid Help</h1>

<p><spring:message code="help.one"/></p>
<p><a href="<spring:message code='help.url'/>">Financial Services</a></p>
<p><spring:message code="help.back"/></p>

<portlet:renderURL var="viewUrl" portletMode="view"/>
<p><a href="${ viewUrl }">Back</a></p>
