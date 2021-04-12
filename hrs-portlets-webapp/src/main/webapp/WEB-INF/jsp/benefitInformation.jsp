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
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/header.jsp"%>

<div id="${n}dl-benefit-summary" class="fl-widget portlet dl-benefit-summary hrs">
  <div class="dl-banner-links">
    <sec:authorize ifAnyGranted="ROLE_VIEW_NEW_HIRE_BENEFITS">
      <div class="dl-banner-link">
        You have a benefit enrollment opportunity.
        <a target="_blank" href="${hrsUrls['Open Enrollment/Hire Event']}">Enroll now</a>
        <c:if test="${not empty learnMoreLink}">
          <a
            aria-label="Learn more about benefit enrollment"
            href="${learnMoreLink}"
            target="_blank" rel="noopener noreferrer">
            Learn more
          </a>
        </c:if>
      </div>
    </sec:authorize>

    <sec:authorize ifAnyGranted="ROLE_VIEW_OPEN_ENROLL_BENEFITS">
      <div class="dl-banner-link">
        You have a benefit enrollment opportunity.
        <a target="_blank" href="${hrsUrls['Open Enrollment/Hire Event']}">Enroll now</a>
        <c:if test="${not empty learnMoreLink}">
          <a
            aria-label="Learn more about annual benefit enrollment"
            href="${learnMoreLink}"
            target="_blank" rel="noopener noreferrer">
            Learn more
          </a>
        </c:if>
      </div>
    </sec:authorize>


    <hrs:helpLink appContext="Benefits" />
  </div>

  <hrs:notification/>

  <div id="${n}dl-tabs" class="dl-tabs ui-tabs ui-widget ui-widget-content ui-corner-all inner-nav-container">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all inner-nav">

    <%--
      set initial tab selection based on optional portlet request param
    --%>
    <c:choose>
      <c:when test="${requestedContent eq 'ETF WRS Statements of Benefits'}">
        <%-- Reflect deep link to ETF WRS SoB tab --%>
        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-benefits">Summary</a></li>

        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-benefit_enrollment_confirmation_statements">Benefit Enrollment Confirmation Statements</a></li>

        <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#${n}dl-etf_wrs_statements_of_benefits">ETF WRS Statements of Benefits</a></li>

        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-dependents">Dependents</a></li>
      </c:when>
      <c:when test="${requestedContent eq 'Benefit Enrollment Confirmation Statements'}">
        <%-- Reflect deep link to Benefit Enrollment Confirmation Statements tab --%>
        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-benefits">Summary</a></li>

        <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#${n}dl-benefit_enrollment_confirmation_statements">Benefit Enrollment Confirmation Statements</a></li>

        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-etf_wrs_statements_of_benefits">ETF WRS Statements of Benefits</a></li>

        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-dependents">Dependents</a></li>
      </c:when>
      <c:otherwise>
        <%-- default to Summary as default tab. --%>
        <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#${n}dl-benefits">Summary</a></li>

        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-benefit_enrollment_confirmation_statements">Benefit Enrollment Confirmation Statements</a></li>

        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-etf_wrs_statements_of_benefits">ETF WRS Statements of Benefits</a></li>

        <li class="ui-state-default ui-corner-top"><a href="#${n}dl-dependents">Dependents</a></li>
      </c:otherwise>
    </c:choose>

    </ul>
    <div id="${n}dl-benefits" class="dl-benefits ui-tabs-panel ui-widget-content ui-corner-bottom">
      <div class="coverage-header">
        <span>Coverage as of the last pay period</span>
      </div>
      <div class="fl-pager">
        <hrs:pagerNavBar position="top" showSummary="${true}" />
        <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
          <table class="dl-table table" tabindex="0" aria-label="Benefit Information Summary table">
            <thead>
              <tr rsf:id="header:">
                <th scope="col" class="flc-pager-sort-header" rsf:id="name"><a href="javascript:;">Benefit</a></th>
                <th scope="col" class="flc-pager-sort-header" rsf:id="coverage"><a href="javascript:;">Coverage</a></th>
              </tr>
            </thead>
            <tbody>
                <tr rsf:id="row:">
                  <td headers="name" class="dl-data-text"><span rsf:id="name"></span></td>
                  <td headers="coverage" class="dl-data-text"><span rsf:id="coverage"></span></td>
                </tr>
            </tbody>
          </table>
        </div>
        <hrs:pagerNavBar position="bottom" />
      </div>

      <sec:authorize ifNotGranted="ROLE_UW_EMPLOYEE_ACTIVE">
        <div class="fl-widget hrs-notification-wrapper alert alert-info">
          <div class="hrs-notification-content">Some links in this MyUW Benefit
            Information app will not work for you because the Human Resources
            System (HRS) does not assign you the active employee role. This
            affects the 'View Benefits Summary Detail' and
            'Update TSA Deductions' buttons on this tab. You may
            <a href="https://kb.wisc.edu/helpdesk/"
              target="_blank" rel="noopener noreferrer">
              contact the Help Desk</a>
            for assistance.</div>
        </div>
      </sec:authorize>

      <div class="container-fluid row">
        <%-- when URL available from portlet pref, use that --%>
        <%-- else use URL from URLs web service if available --%>
        <%-- implied otherwise: simply drop the button --%>
        <c:choose>
          <c:when test="${not empty prefs['benefitsSummaryUrl']
            && not empty prefs['benefitsSummaryUrl'][0]}">
            <div class='col-xs-4 col-xs-offset-2'>
              <a href="${prefs['benefitsSummaryUrl'][0]}"
                target="_blank" rel="noopener noreferer"
                class="btn btn-default">
                  View Benefits Summary Detail
              </a>
            </div>
          </c:when>
          <c:when test="${not empty hrsUrls['Benefits Summary']}">
            <div class='col-xs-4 col-xs-offset-2'>
              <a href="${hrsUrls['Benefits Summary']}"
                target="_blank" rel="noopener noreferer"
                class="btn btn-default">
                  View Benefits Summary Detail
              </a>
            </div>
          </c:when>
        </c:choose>
        <div class='col-xs-3'>
            <a href="${hrsUrls['Update TSA Deductions']}" target="_blank" class="btn btn-default">Update TSA Deductions</a>
        </div>
      </div>
    </div>

    <div id="${n}dl-benefit_enrollment_confirmation_statements" class="dl-benefit_enrollment_confirmation_statements ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide">
      <c:choose>

        <c:when test="${statementError}">
          <p>Sorry! MyUW was unable to load or display your benefit enrollment confirmation statements.
            Contact the Help Desk as needed.</p>
        </c:when>

        <c:when test="${empty enrollmentStatements}">
          <p>You have no benefit enrollment confirmation statements.</p>
        </c:when>

        <c:otherwise>

          <div class="fl-pager">
            <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
              <table class="dl-table table" tabindex="0" aria-label="Benefit Enrollment Confirmation Statement table">
                <thead>
                  <tr>
                    <th scope="col" class="flc-pager-sort-header dl-col-5p">Year</th>
                    <th scope="col" class="flc-pager-sort-header">Statement</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${enrollmentStatements}" var="enrollmentStatement">
                    <tr>
                      <td class="dl-data-text">
                        <a href="${enrollmentStatement.url}" title="${enrollmentStatement.name}"
                          target="_blank" rel="noopener noreferrer">
                          ${enrollmentStatement.year}
                        </a>
                      </td>
                      <td class="dl-data-text">
                        <a href="${enrollmentStatement.url}"
                          target="_blank" rel="noopener noreferrer">
                          ${enrollmentStatement.name}
                        </a>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>

        </c:otherwise>
      </c:choose>
    </div>

    <div id="${n}dl-etf_wrs_statements_of_benefits" class="dl-etf_wrs_statements_of_benefits ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide">
      <c:choose>

        <c:when test="${statementError}">
          <p>Sorry! MyUW was unable to load or display your WRS Statements of Benefits.
            Contact the Help Desk as needed.</p>
        </c:when>

        <c:when test="${empty etfStatements}">
          <p>You have no WRS Statements of Benefits.</p>
        </c:when>

        <c:otherwise>
          <div class="fl-pager">
            <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
              <table class="dl-table table" tabindex="0"
                aria-label="ETF WRS Statements of Benefits table">
                <thead>
                  <tr>
                    <th scope="col" class="flc-pager-sort-header dl-col-5p">Year</th>
                    <th scope="col" class="flc-pager-sort-header">Statement</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="etfStatement" items="${etfStatements}">
                    <tr class="dl-clickable">
                      <td class="dl-data-text">
                        <a href="${etfStatement.url}" target="_blank" title="${etfStatement.name}">
                          ${etfStatement.year}</a>
                      </td>
                      <td class="dl-data-text">
                        <a href="${etfStatement.url}" target="_blank">${etfStatement.name}</a>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
            <div class="${n}-dl-benefit-statement-links dl-benefit-statement-links">
              <div class="container-fluid row">
                <div class='col-xs-6'>
                  <a href="https://uwservice.wisc.edu/help/wrs-benefits-statement.php" target="_blank" class="btn btn-default">
                    ETF Annual Statement of Benefits: Enclosures and Explanation</a>
                </div>
              </div>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <div id="${n}dl-dependents" class="dl-dependents ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide">
      <%-- coverage-header removed as of commit 96f7dd6 --%>
      <div class="fl-pager">
        <hrs:pagerNavBar position="top" showSummary="${true}" />
        <div class="fl-container-flex dl-pager-table-data fl-pager-data">
          <table class="dl-table">
            <thead>
              <tr rsf:id="header:">
                <th scope="col" class="flc-pager-sort-header" rsf:id="name"><a href="javascript:;">Name</a></th>
                <th scope="col" class="flc-pager-sort-header" rsf:id="relationship"><a href="javascript:;">Relationship</a></th>
              </tr>
            </thead>
            <tbody>
                <tr rsf:id="row:" class="dl-clickable">
                  <td headers="name" class="dl-data-text"><span rsf:id="name"></span></td>
                  <td headers="relationship" class="dl-data-text"><span rsf:id="relationship"></span></td>
                </tr>
            </tbody>
          </table>
        </div>
        <hrs:pagerNavBar position="bottom" />
      </div>

      <sec:authorize ifNotGranted="ROLE_UW_EMPLOYEE_ACTIVE">
        <div class="fl-widget hrs-notification-wrapper alert alert-info">
          <div class="hrs-notification-content">Some links in this MyUW Benefit
            Information app will not work for you because the Human Resources
            System (HRS) does not assign you the active employee role. This
            affects the 'View/Update Dependent Information' and
            'View Dependent Coverage' buttons on this tab. You may
            <a href="https://kb.wisc.edu/helpdesk/"
              target="_blank" rel="noopener noreferrer">
              contact the Help Desk</a>
            for assistance.</div>
        </div>
      </sec:authorize>

      <div class="container-fluid row">
        <div class='col-xs-3 col-xs-offset-2'>
          <c:choose>
              <%-- if the new Dependent/Beneficiary Info URL is available,
              use it --%>
            <c:when test="${not empty hrsUrls['Dependent/Beneficiary Info']}">
              <a href="${hrsUrls['Dependent/Beneficiary Info']}"
                target="_blank" rel="noopener noreferer"
                class="btn btn-default">View/Update Dependent Information</a>
            </c:when>
            <%-- otherwise if the old information URL is available,
              use it --%>
            <c:when test="${not empty hrsUrls['Dependent Information']}">
                <a href="${hrsUrls['Dependent Information']}" target="_blank" class="btn btn-default">View/Update Dependent Information</a>
            </c:when>
          </c:choose>
        </div>
        <div class='col-xs-3 col-xs-offset-1'>
            <a href="${hrsUrls['Dependent Coverage']}" target="_blank" class="btn btn-default">View Dependent Coverage</a>
        </div>
      </div>
    </div>
  </div>

  <%@ include file="/WEB-INF/jsp/footer.jsp"%>

</div>

<portlet:resourceURL var="benefitSummaryUrl" id="benefitSummary" escapeXml="false"/>

<portlet:resourceURL var="benefitStatementsUrl" id="benefitStatements" escapeXml="false"/>

<portlet:resourceURL var="benefitsPdfUrl" id="benefits.pdf" escapeXml="false">
    <portlet:param name="mode" value="TMPLT_*.docType_TMPLT"/>
    <portlet:param name="year" value="TMPLT_*.year_TMPLT"/>
    <portlet:param name="docId" value="TMPLT_*.docId_TMPLT"/>
</portlet:resourceURL>

<script type="text/javascript">
<rs:compressJs>
(function($, dl) {
    $(function() {
        dl.pager.init("#${n}dl-benefits", {
          columnDefs: [
            dl.pager.colDef("name", {sortable: true}),
            dl.pager.colDef("coverage", {sortable: true})
          ],
          dataList: {
              url: "${benefitSummaryUrl}",
              dataKey: "benefits",
              dataLoadErrorMsg: "${genericErrorMessage}"
          }
        });

        dl.pager.init("#${n}dl-dependents", {
          columnDefs: [
            dl.pager.colDef("name", {sortable: true}),
            dl.pager.colDef("relationship", {sortable: true})
          ],
          dataList: {
              url: "${benefitSummaryUrl}",
              dataKey: "dependents",
              dataLoadErrorMsg: "${genericErrorMessage}"
          }
        });

        $("#${n}dl-tabs").tabs({
            show: function(event, ui) {
                $.log("Showing tab: " + ui.index);
                dl.pager.show(ui.panel);
            }
        });

        dl.util.clickableContainer("#${n}dl-benefit-summary");
    });
})(dl_v1.jQuery, dl_v1);
</rs:compressJs>
</script>
