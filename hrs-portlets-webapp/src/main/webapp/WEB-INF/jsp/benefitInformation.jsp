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
    <c:choose>
    <c:when test="${enrollmentFlag == 'O'}">
      <div class="dl-banner-link">
        <spring:message code="benefit.summary.enrollment.O.message" text="You have a benefit enrollment opportunity. Please enroll online by clicking the following link: "/>
        <a target="_blank" href="${hrsUrls['Open Enrollment/Hire Event']}"><spring:message code="open.enrollment" text="Open Enrollment" /></a>
        <c:if test="${isMadisonUser}">
          ${learnMoreEBenefitGuide}
        </c:if>
      </div>
    </c:when>
    <c:when test="${enrollmentFlag == 'H'}">
      <div class="dl-banner-link">
        You have a benefit enrollment opportunity. Please enroll online by clicking the following link.
        <a target="_blank" href="${hrsUrls['Open Enrollment/Hire Event']}">Benefits Enrollment</a>. A Benefit Enrollment
        Deadlines worksheet is available in the Statements tab which details your enrollment deadlines by plan.
        <c:if test="${isMadisonUser}">
          ${learnMoreEBenefitGuide}
        </c:if>
      </div>
    </c:when>
    </c:choose>
    <div class="dl-help-link">
      <a href="${helpUrl}" target="_blank">Help</a>
    </div>
  </div>

  <hrs:notification/>

  <div id="${n}dl-tabs" class="dl-tabs ui-tabs ui-widget ui-widget-content ui-corner-all inner-nav-container">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all inner-nav">
      <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#${n}dl-benefits">Summary</a></li>
      <li class="ui-state-default ui-corner-top"><a href="#${n}dl-benefit-statements">Statements</a></li>
      <li class="ui-state-default ui-corner-top"><a href="#${n}dl-dependents">Dependents</a></li>
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
      <div class="container-fluid row">
        <div class='col-xs-4 col-xs-offset-2'>
            <a href="${hrsUrls['Benefits Summary']}" target="_blank" class="btn btn-default">View Benefits Summary Detail</a>
        </div>
        <div class='col-xs-3'>
            <a href="${hrsUrls['Update TSA Deductions']}" target="_blank" class="btn btn-default">Update TSA Deductions</a>
        </div>
      </div>
    </div>
    <div id="${n}dl-benefit-statements" class="dl-benefit-statements ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide">
      <div class="fl-pager">
        <hrs:pagerNavBar position="top" showSummary="${true}" />
        <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
          <table class="dl-table table" tabindex="0" aria-label="Benefit Information Statement table">
            <thead>
              <tr rsf:id="header:">
                <th scope="col" class="flc-pager-sort-header dl-col-5p" rsf:id="year"><a href="javascript:;">Year</a></th>
                <th scope="col" class="flc-pager-sort-header" rsf:id="name"><a href="javascript:;">Statement</a></th>
              </tr>
            </thead>
            <tbody>
                <tr rsf:id="row:" class="dl-clickable">
                  <td headers="year" class="dl-data-text"><a href="#" target="_blank" rsf:id="year"></td>
                  <td headers="name" class="dl-data-text"><a href="#" target="_blank" rsf:id="name"></td>
                </tr>
            </tbody>
          </table>
        </div>
        <hrs:pagerNavBar position="bottom" />
        <div class="${n}-dl-benefit-statement-links dl-benefit-statement-links">
            <div class="container-fluid row">
                <div class='col-xs-6'>
                    <a href="https://uwservice.wisc.edu/help/wrs-benefits-statement.php" target="_blank" class="btn btn-default">ETF Annual Statement of Benefits: Enclosures and Explanation</a>
                </div>
                <div class='col-xs-6'>
                    <a href="https://uwservice.wisc.edu/help/benefits-statement.php" target="_blank" class="btn btn-default">Explanation of Staff Benefits Statement</a>
                </div>
            </div>
        </div>
      </div>
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
      <div class="container-fluid row">
        <div class='col-xs-3 col-xs-offset-2'>
            <a href="${hrsUrls['Dependent Information']}" target="_blank" class="btn btn-default">View Dependent Details</a>
        </div>
        <div class='col-xs-3 col-xs-offset-1'>
            <a href="${hrsUrls['Dependent Coverage']}" target="_blank" class="btn btn-default">View Dependent Coverage</a>
        </div>
      </div>
    </div>
  </div>
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

        var benefitStatementUrl = dl.util.templateUrl("${benefitsPdfUrl}");
        dl.pager.init("#${n}dl-benefit-statements", {
          model: {
              /* sortKey: "name",
              sortDir: 1 */
          },
          columnDefs: [
              dl.pager.linkColDef("year", benefitStatementUrl, {sortable: true}),
              dl.pager.linkColDef("name", benefitStatementUrl, {sortable: true})
          ],
          dataList: {
              url: "${benefitStatementsUrl}",
              dataKey: "report",
              dataLoadErrorMsg: "${genericErrorMessage}",
              dataLoadCallback: function (data) {
                  if (data == undefined || data.length == 0) {
                      //Hide the ${n}-dl-benefit-statement-links
                      $('.${n}-dl-benefit-statement-links').hide();

                  }
              }
          }
        });

        var opt = 0;

        if("statements" === "${tab}") {
            opt = 1;
        } else if ("dependents" === "${tab}") {
            opt = 2;
        }

        $("#${n}dl-tabs").tabs({
            show: function(event, ui) {
                $.log("Showing tab: " + ui.index);
                dl.pager.show(ui.panel);
            }
        });

        $("#${n}dl-tabs").tabs("select",opt);

        dl.util.clickableContainer("#${n}dl-benefit-summary");
    });
})(dl_v1.jQuery, dl_v1);
</rs:compressJs>
</script>
