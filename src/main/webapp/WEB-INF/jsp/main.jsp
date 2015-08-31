<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>
<c:set var="n"><portlet:namespace/></c:set>
<rs:aggregatedResources path="/resources.xml"/>

<div class="${n}faContainer finaid-portlet">

  <c:choose>
    <c:when test="${not empty terms}">
      <!-- Dropdown Select -->
      <div class="row">
        <div class="col-xs-12">
          <spring:message code="bar.text"/>
          <select class="form-control" id="dropdown">
            <c:forEach var="term" items="${terms}">
              <c:choose>
                <c:when test="${term.key == latestTerm.code}">
                  <option selected="true" value="${term.key}">${term.value.desc}</option>
                </c:when>
                <c:otherwise>
                  <option value="${term.key}">${term.value.desc}</option>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </select>
        </div>
      </div>

      <div id="error" class="row">
        <div class="col-xs-12">
          <div id="errorMsg"></div>
        </div>
      </div>

      <!-- Financial Aid Info -->
      <div class="financial-info">
        <div class="row">
          <div class="col-xs-12 col-md-6">
            <h3><spring:message code="req.head"/></h3>
            <p id="req" class="msg"><spring:message code="req.msg"/></p>
          </div>

          <div class="col-xs-12 col-md-6">
            <h3><spring:message code="status.head"/></h3>
            <p id="status" class="msg"><spring:message code="status.msg"/></p>
          </div>
        </div>

        <div class="row">
          <div class="col-xs-12">
            <h3><spring:message code="awards.head"/></h3>
            <div id="awards" class="msg"><spring:message code="awards.msg"/></div>
          </div>
        </div>
      </div>

      <c:if test="${not empty messages}">
        <!-- Display Messages -->
        <div class="row">
          <div class="col-xs-12" id="messages">
            <h3><spring:message code="messages.head"/></h3>
            <c:forEach items="${messages}" var="message">
              <p>${message}</p>
            </c:forEach>
          </div>
        </div>
      </c:if>
    </c:when>
    <c:otherwise>
    <%-- The user has no Terms --%>
      <jsp:directive.include file="/WEB-INF/jsp/fragments/info-dialog.jsp"/>
    </c:otherwise>
  </c:choose>

  <div class="row">
    <div class="col-xs-12 links">
      <a href="https://google.com"
          target="_blank" type="button" class="btn btn-primary">
          <spring:message code="finaid"/>
      </a>
    </div>
  </div>


</div><%-- end .finaid-portlet --%>

<portlet:resourceURL var="termJSONURL"/>

<!-- per Jasig/Apereo JS Standards -->
<script type="text/javascript">
    var portlets = portlets || {};
    up.jQuery().carousel || portlets.bootstrapjQuery || document.write('<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"><\/script>');
</script>

<!-- per Jasig/Apereo JS Standards -->
<script type="text/javascript">
    portlets["${n}"] = {};
    portlets["${n}"].jQuery = jQuery.noConflict(true);
    portlets.bootstrapjQuery = portlets.bootstrapjQuery || (up.jQuery().carousel ? up.jQuery : portlets["${n}"].jQuery);
</script>
<script type="text/javascript">

(function($, options) {

 // AJAX call to retrieve the FinancialInfo model for a given Term
  ${n}getFinancialInfo = function(code) {
    $.ajax({
      type: 'POST',
      url: '${termJSONURL}',
      data: {'code': code},
      success: function(data) {
        // Ensure the proper views are visible in case there was a previous error
        $('.${n}faContainer .financial-info').show();
        $('.${n}faContainer #errorMsg').html('').removeClass("alert alert-danger alert-error");

        var info = data.financialInfo;

        //Show Messages (if we are at the latest term)
        var selectedCode = $(options.elements.faDropdown).val();
        if (selectedCode === data.latestTerm) {
          $(options.elements.faMessages).show();
        } else {
          $(options.elements.faMessages).hide();
        }

        //Update Eligibility
        if (info.progress.status.length) {
          html = info.progress.status;
        } else {
          html = options.messages.statusMessage;
        }
        $(options.elements.faStatus).html(html);

        //Update Awards
        if (info.awards.length) {
          html = '<table class="table table-striped">' +
            '<tr>'+
            '<th>FUND</th>' +
            '<th>STATUS</th>' +
            '<th>OFFERED</th>' +
            '<th>PAID</th>' +
            '</tr>';

          $.each(info.awards, function(index, award) {
            html +=
              '<tr>' +
              '<td>' + award.fund + '</td>' +
              '<td>' + award.status + '</td>' +
              '<td>' + award.offeredAmt + '</td>' +
              '<td>' + award.paidAmt + '</td>' +
              '</tr>';
          });
          html += '</table>';
        } else {
          html = options.messages.awardsMessage;
        }

        $(options.elements.faAwards).html(html);

        //Update Holds
        if (info.holds.length) {
          html = '';
          $.each(info.holds, function(index, hold) {
            html += '<p>' + hold.req;
            if (hold.url !== "") {
              html += ' - <a href="' + hold.url + '" target="_blank">' + that.options.messages.holdsLinkMessage + '</a>';
            }
            html += '</p>';
          });
        } else {
          html = options.messages.holdsMessage;
        }
        $(options.elements.faHolds).html(html);

      },
      error: function(jqXHR, textStatus, errorThrown) {
        $('.${n}faContainer .financial-info').hide();
        $('.${n}faContainer #errorMsg').html('<spring:message code="error.msg"/>').addClass("alert alert-danger alert-error");
      }
    });
  };

  // On page load
  $(function() {
    // Load view with the current term's information initially
    ${n}getFinancialInfo('${latestTerm.code}');

    $(options.elements.faDropdown).change(function() {
      var code = $(this).val();
      ${n}getFinancialInfo(code); // implicity calls updateView() on success
    });
  });

})(portlets["${n}"].jQuery, {
  elements: {
    faDropdown: '.${n}faContainer #dropdown',
    faStatus: '.${n}faContainer #status',
    faAwards: '.${n}faContainer #awards',
    faHolds: '.${n}faContainer #req'
  },
  messages: {
    statusMessage: '<spring:message code="status.msg"/>',
    awardsMessage: '<spring:message code="awards.msg"/>',
    holdsLinkMessage: '<spring:message code="link"/>',
    holdsMessage: '<spring:message code="req.msg"/>'
  },
  data: {
    latestTerm: '${latestTerm.code}'
  }
});
</script>
