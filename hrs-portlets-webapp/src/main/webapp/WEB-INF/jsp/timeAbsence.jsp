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


<sec:authorize
  ifAnyGranted="ROLE_REDIRECT_TO_HRS_FLUID_TIME">
  <c:choose>
    <c:when test="${not empty hrsUrls['Fluid Time']}">
      <script>
        window.location.replace("${hrsUrls['Fluid Time']}");
      </script>
    </c:when>
    <c:otherwise>
       <p>Error. Try again later or contact the Help Desk.</p>
       <p>Technical details: you are in MyUW role
         ROLE_REDIRECT_TO_HRS_FLUID_TIME, which means MyUW should redirect you
         to the HRS URL named `Fluid Time`, but that URL is not present in the
         MyUW inventory of HRS URLs.</p>
    </c:otherwise>
   </c:choose>
</sec:authorize>


<sec:authorize
  ifNotGranted="ROLE_REDIRECT_TO_HRS_FLUID_TIME">

<c:set var="showJobTitle" value="${fn:length(personalData.jobs) > 1}"/>

<div id="${n}dl-time-absence" class="fl-widget portlet dl-time-absence hrs">
  <div>
    <div class="dl-banner-links">
      <hrs:helpLink appContext="Time and Absence" />
    </div>

    <div id="${n}leaveReportingNotice" style="display: none;">
      <%-- style changes as side effect of Outstanding Missing Leave Report statement callback. --%>
      <c:if test="${not empty leaveReportingNotice}">
        <div class="fl-widget hrs-notification-wrapper alert alert-info">
          <div class="hrs-notification-content">${leaveReportingNotice}</div>
        </div>
      </c:if>
    </div>

    <hrs:notification/>

    <sec:authorize
      ifAnyGranted="ROLE_TIMESHEET_BUTTON"
      ifNotGranted="ROLE_UW_DYN_AM_PUNCH_TIME">
      <%-- sec:authorize attributes are ANDed, as in user must fulfill all of
        them. So this targets users who see the timesheet button but who do not
        have UW_DYN_AM_PUNCH_TIME --%>
      <div id="${n}nonDynPunchTimesheetNotification">
        <c:if test="${not empty nonDynPunchTimesheetNotification}">
          <div class="fl-widget hrs-notification-wrapper alert alert-info">
            <div class="hrs-notification-content">
              ${nonDynPunchTimesheetNotification}
            </div>
          </div>
        </c:if>
      </div>
    </sec:authorize>

    <sec:authorize
      ifAllGranted="ROLE_UW_DYN_AM_PUNCH_TIME">
      <div id="${n}dynPunchTimesheetNotification">
        <c:if test="${not empty dynPunchTimesheetNotification}">
          <div class="fl-widget hrs-notification-wrapper alert alert-info">
            <div class="hrs-notification-content">
              ${dynPunchTimesheetNotification}
            </div>
          </div>
        </c:if>
      </div>
    </sec:authorize>

  </div>

  <div>
    <c:if test="${not empty hrsUrls['Year End Leave Balances']}">
        <div class="dl-link">
            <a href="${hrsUrls['Year End Leave Balances']}"
               target="_blank"><spring:message code="label.yearEndLeaveBalance"
               text="University Staff end of year leave balance"/></a><br/>
        </div>
    </c:if>
    <sec:authorize ifAnyGranted="ROLE_ENTER_EDIT_CANCEL_OWN_ABSENCES">
      <div class="dl-link">
        <a class="btn btn-primary" href="${hrsUrls['Request Absence']}" target="_blank">Enter Absence</a>
      </div>
    </sec:authorize>

    <sec:authorize ifAnyGranted="ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL">
      <c:if test="${not empty hrsUrls['Classic ESS Abs Bal']}">
        <div class="dl-link">
          <a class="btn btn-primary" href="${hrsUrls['Classic ESS Abs Bal']}"
            target="_blank" rel="noopener noreferrer">
            View Leave Balances</a>
        </div>
      </c:if>
    </sec:authorize>

    <sec:authorize ifAnyGranted="ROLE_ENTER_EDIT_CANCEL_OWN_ABSENCES">
      <c:choose>
        <c:when test="${not empty hrsUrls['Time and Absence Edit/Cancel']}">
          <div class="dl-link">
            <a class="btn btn-primary"
              href="${hrsUrls['Time and Absence Edit/Cancel']}"
              target="_blank" rel="noopener noreferer">
              Edit/Cancel Absence
            </a>
          </div>
        </c:when>
        <c:when test="${not empty prefs['editCancelAbsenceUrl']
        && not empty prefs['editCancelAbsenceUrl'][0]}">
          <div class="dl-link">
            <a class="btn btn-primary"
              href="${prefs['editCancelAbsenceUrl'][0]}"
              target="_blank" rel="noopener noreferer">
              Edit/Cancel Absence
            </a>
          </div>
        </c:when>
      </c:choose>
    </sec:authorize>
    <sec:authorize ifAnyGranted="ROLE_TIMESHEET_BUTTON">
      <div class="dl-link">
        <div style='display: inline-block;'>
          <a class="btn btn-primary" href="${hrsUrls['Timesheet']}" target="_blank">Timesheet</a>
        </div>
        <div class="timesheet-notice">
          <sec:authorize
            ifAllGranted="ROLE_UW_DYN_AM_PUNCH_TIME">
            <c:if test="${not empty dynPunchTimesheetNotice}">
              ${dynPunchTimesheetNotice}
            </c:if>
            <c:if test="${not empty timesheetNotice}">
              <hr/>
            </c:if>
          </sec:authorize>
          <c:if test="${not empty timesheetNotice}">
            ${timesheetNotice}
          </c:if>
        </div>
        <br/>
      </div>
    </sec:authorize>
    <sec:authorize ifAnyGranted="ROLE_VIEW_WEB_CLOCK">
      <div class="dl-link">
        <a  class="btn btn-primary"
          href="${hrsUrls['Web Clock']}"
          target="_blank" rel="noopener noreferer">
          Web Clock</a><br/>
      </div>
    </sec:authorize>
  </div>

  <div id="${n}dl-tabs" class="dl-tabs ui-tabs ui-widget ui-widget-content ui-corner-all inner-nav-container">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all inner-nav">
      <c:set var="activeTabStyle" value="ui-tabs-selected ui-state-active"/>
      <sec:authorize ifAllGranted="ROLE_VIEW_ABSENCE_HISTORIES">
        <li class="ui-state-default ui-corner-top ${activeTabStyle}"><a href="#${n}dl-absence">Absence</a></li>
        <c:set var="activeTabStyle" value=""/>
      </sec:authorize>

      <c:if test="${empty hrsUrls['Classic ESS Abs Bal']
      || empty employeeRoles['ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL']}">
        <li class="ui-state-default ui-corner-top ${activeTabStyle}">
          <a href="#${n}dl-leave-balance">Leave Balances</a></li>
          <c:set var="activeTabStyle" value=""/>
      </c:if>

      <sec:authorize ifAnyGranted="ROLE_VIEW_TIME_ENTRY_HISTORY">
        <li class="ui-state-default ui-corner-top ${activeTabStyle}"><a href="#${n}dl-time-entry">Time Entry</a></li>
        <c:set var="activeTabStyle" value=""/>
      </sec:authorize>
      <li class="ui-state-default ui-corner-top ${activeTabStyle}"><a href="#${n}dl-absence-statements">Leave Reports</a></li>
      <c:set var="activeTabStyle" value=""/>
    </ul>

    <c:set var="hiddenTabStyle" value=""/>
    <sec:authorize ifAllGranted="ROLE_VIEW_ABSENCE_HISTORIES">

      <!-- style subsequent panels as hidden
        because this was the default active tab-->
      <c:set var="hiddenTabStyle" value="ui-tabs-hide"/>

      <div id="${n}dl-absence" class="dl-absence ui-tabs-panel ui-widget-content ui-corner-bottom">
        <div class="fl-pager">
          <hrs:pagerNavBar position="top" showSummary="${true}" />
          <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
            <table class="dl-table table">
              <thead>
                <tr rsf:id="header:">
                  <th scope="col" class="flc-pager-sort-header" rsf:id="name"><a href="javascript:;">Name</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="status"><a href="javascript:;">Status</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="start"><a href="javascript:;">Start</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="end"><a href="javascript:;">End</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="total"><a href="javascript:;">Total</a></th>
                  <c:if test="${showJobTitle}">
                    <th scope="col" class="flc-pager-sort-header" rsf:id="title"><a href="javascript:;">Job Title</a></th>
                  </c:if>
                </tr>
              </thead>
              <tbody>
                  <tr rsf:id="row:">
                    <td headers="name" class="dl-data-text"><span rsf:id="name"></span></td>
                    <td headers="status" class="dl-data-text"><span rsf:id="status"></span></td>
                    <td headers="start" class="dl-data-date"><span rsf:id="start"></span></td>
                    <td headers="end" class="dl-data-date"><span rsf:id="end"></span></td>
                    <td headers="total" class="dl-data-number"><span rsf:id="total"></span></td>
                    <c:if test="${showJobTitle}">
                      <td headers="title" class="dl-data-text"><span rsf:id="title"></span></td>
                    </c:if>
                  </tr>
              </tbody>
            </table>
          </div>
          <hrs:pagerNavBar position="bottom" />
        </div>
      </div>
    </sec:authorize>


    <%-- There are two reasons the Leave Balances tab will be included in an
      employee experience of Time and Absence. Either reason will do.
      1. The HRS URL `Classic ESS Abs Bal` is not defined, and so Time and Absence would not know how to link the employee to the new implementation of leave balances anyway. OR
      2. The employee lacks ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL

      This is the identical logic (`test`) of that above for deciding whether to
      include the Leave Balances tab label among the tab label list items.

      This is the opposite logic of that above to decide whether to include the
      `View Leave Balances` button.
    --%>
    <c:if test="${empty hrsUrls['Classic ESS Abs Bal']
      || empty employeeRoles['ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL']}">
    <div id="${n}dl-leave-balance" class="dl-leave-balance ui-tabs-panel ui-widget-content ui-corner-bottom ${hiddenTabStyle}">

      <!-- style subsequent panels as hidden
        if this was the default active tab-->
      <c:set var="hiddenTabStyle" value="ui-tabs-hide"/>

      <div class="balance-header">
        <c:choose>
          <c:when test="${not empty prefs['payrollInformationFName']
            && not empty prefs['payrollInformationFName'][0]}">
            <span>These leave balances are as of your most recent Earnings Statement in
              <a href="/web/exclusive/${prefs['payrollInformationFName'][0]}">
                Payroll Information</a>.</span>
          </c:when>
          <c:otherwise>
            <span>These leave balances are as of your most recent Earnings Statement.</span>
          </c:otherwise>
        </c:choose>

      </div>
      <div class="fl-pager">
        <hrs:pagerNavBar position="top" showSummary="${true}" />
        <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
          <table class="dl-table table" tabindex="0" aria-label="Leave balance detail table">
            <thead>
              <tr rsf:id="header:">
                <th scope="col" class="flc-pager-sort-header" rsf:id="entitlement"><a href="javascript:;">Entitlement</a></th>
                <th scope="col" class="flc-pager-sort-header" rsf:id="balance"><a href="javascript:;">Balance</a></th>
                <c:if test="${showJobTitle}">
                  <th scope="col" class="flc-pager-sort-header" rsf:id="title"><a href="javascript:;">Job Title</a></th>
                </c:if>
              </tr>
            </thead>
            <tbody>
              <tr rsf:id="row:">
                <td headers="entitlement" class="dl-data-text"><span rsf:id="entitlement"></span></td>
                <td headers="balance" class="dl-data-number"><span rsf:id="balance"></span></td>
                <c:if test="${showJobTitle}">
                  <td headers="title" class="dl-data-text"><span rsf:id="title"></span></td>
                </c:if>
              </tr>
            </tbody>
          </table>
        </div>
        <hrs:pagerNavBar position="bottom" />
      </div>
    </div>
    </c:if>

    <sec:authorize ifAnyGranted="ROLE_VIEW_TIME_ENTRY_HISTORY">
      <div id="${n}dl-time-entry" class="dl-time-entry ui-tabs-panel ui-widget-content ui-corner-bottom ${hiddenTabStyle}">

      <!-- style subsequent panels as hidden
        if this was the default active tab-->
        <c:set var="hiddenTabStyle" value="ui-tabs-hide"/>

        <div class="fl-pager">
          <hrs:pagerNavBar position="top" showSummary="${true}" />
          <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
            <table class="dl-table table" tabindex="0" aria-label="Time Entry details table">
              <thead>
                <tr rsf:id="header:">
                  <th scope="col" class="flc-pager-sort-header" rsf:id="date"><a href="javascript:;">Date</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="status"><a href="javascript:;">Status</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="total"><a href="javascript:;">Total</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="type"><a href="javascript:;">Type</a></th>
                  <c:if test="${showJobTitle}">
                    <th scope="col" class="flc-pager-sort-header" rsf:id="title"><a href="javascript:;">Job Title</a></th>
                  </c:if>
                </tr>
              </thead>
              <tbody>
                  <tr rsf:id="row:">
                    <td headers="date" class="dl-data-date"><span rsf:id="date"></span></td>
                    <td headers="status" class="dl-data-text"><span rsf:id="status"></span></td>
                    <td headers="total" class="dl-data-number"><span rsf:id="total"></span></td>
                    <td headers="type" class="dl-data-text"><span rsf:id="type"></span></td>
                    <c:if test="${showJobTitle}">
                      <td headers="title" class="dl-data-text"><span rsf:id="title"></span></td>
                    </c:if>
                  </tr>
              </tbody>
            </table>
          </div>
          <hrs:pagerNavBar position="bottom" />
        </div>
        <div>
          <div class="dl-link center">
            <a href="${hrsUrls['Payable time detail']}" target="_blank" class="btn btn-default">View Details</a>
            <a href="https://uwservice.wisc.edu/docs/forms/time-missed-punch.pdf" target="_blank" class="btn btn-default">Missed Punch Form</a>
          </div>
        </div>
      </div>
    </sec:authorize>

    <div id="${n}dl-absence-statements" class="dl-absence-statements ui-tabs-panel ui-widget-content ui-corner-bottom ${hiddenTabStyle}">

      <!-- style subsequent panels as hidden
        if this was the default active tab -->
        <c:set var="hiddenTabStyle" value="ui-tabs-hide"/>

      <div id="${n}dl-leave-statements">
      	<p class="padded-paragraph">
          <a id="${n}oustandingMissingLeaveReports" style="display: none;" target="_blank" ng-href="#" class="btn btn-default">My Unsubmitted Reports</a>
      	</p>
        <div class="fl-pager">
          <hrs:pagerNavBar position="top" showSummary="${true}" />
          <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
            <table class="dl-table table">
              <thead>
                <tr rsf:id="header:">
                  <th scope="col" class="flc-pager-sort-header" rsf:id="payPeriod"><a href="javascript:;">Pay Period</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="leaveStatementLink"><a href="javascript:;">Leave Statement</a></th>
                  <th scope="col" class="flc-pager-sort-header" rsf:id="leaveFurloughReportLinks"><a href="javascript:;">Leave Report</a></th>
                </tr>
              </thead>
              <tbody>
                  <tr rsf:id="row:">
                    <td headers="payPeriod" class="dl-data-text" rsf:id="payPeriod"></td>
                    <td headers="leaveStatementLink" class="dl-data-text" rsf:id="leaveStatementLink"></td>
                    <td headers="leaveFurloughReportLinks" class="dl-data-text" rsf:id="leaveFurloughReportLinks"></td>
                  </tr>
              </tbody>
            </table>
          </div>
          <hrs:pagerNavBar position="bottom" />
        </div>
      </div>

      <div id="${n}dl-sabbatical-reports">
        <div class="fl-pager">
          <hrs:pagerNavBar position="top" showSummary="${true}" />
          <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
            <table class="dl-table table">
              <thead>
                <tr rsf:id="header:">
                  <th class="flc-pager-sort-header" rsf:id="year"><a href="javascript:;">Year</a></th>
                  <th class="flc-pager-sort-header" rsf:id="fullTitle"><a href="javascript:;">Statement</a></th>
                </tr>
              </thead>
              <tbody>
                  <tr class="dl-clickable" rsf:id="row:">
                    <td class="dl-data-text"><a href="#" target="_blank" rsf:id="year"></a></td>
                    <td class="dl-data-text"><a href="#" target="_blank" rsf:id="fullTitle"></a></td>
                  </tr>
              </tbody>
            </table>
          </div>
          <hrs:pagerNavBar position="bottom" />
        </div>
      </div>

      <div id="${n}no-statements">
        <spring:message code="noLeaveOrSabbaticalStatements" />
      </div>

    </div>
  </div>
  <div>
    <%--
    <div class="dl-link">
      <a href="${prefs['UnclassifiedFurloughTimeReport_NonInstructionalStaffUrl'][0]}" target="_blank">Unclassified Furlough Time Report - Non-Instructional Staff</a><br/>
    </div>
    <div class="dl-link">
      <a href="${prefs['UnclassifiedFurloughTimeReport_InstructionalStaffUrl'][0]}" target="_blank">Unclassified Furlough Time Report - Instructional Staff</a>
    </div>
     --%>
    <div class="dl-link">
      <a href="${prefs['UnclassifiedLeaveReportUrl'][0]}" target="_blank" class='btn btn-default'>Unclassified Leave Report</a><c:if test="${not empty prefs['UnclassifiedLeaveReportForSummerUrl'][0]}">
      <span class="hidden-xs visible-xs">|</span>
      <a href="${prefs['UnclassifiedLeaveReportForSummerUrl'][0]}" target="_blank"  class='btn btn-default'>Unclassified Summer Session/Service Leave Report</a></c:if>
    </div>
  </div>

  <%@ include file="/WEB-INF/jsp/footer.jsp"%>

</div>

<portlet:resourceURL var="absenceHistoriesUrl" id="absenceHistories" escapeXml="false"/>

<portlet:resourceURL var="absenceBalancesUrl" id="absenceBalances" escapeXml="false" />

<portlet:resourceURL var="timeSheetsUrl" id="timeSheets" escapeXml="false" />

<portlet:resourceURL var="leaveStatementsUrl" id="leaveStatements" escapeXml="false" />

<portlet:resourceURL var="missingReportUrl" id="missingReport" escapeXml="false" />

<portlet:resourceURL var="leaveStatementPdfUrl" id="leave_statement.pdf" escapeXml="false">
    <portlet:param name="docId" value="leaveStatementDocId"/>
</portlet:resourceURL>
<portlet:resourceURL var="leaveFurloughReportPdfUrl" id="leave_furlough_report.pdf" escapeXml="false">
    <portlet:param name="docId" value="leaveFurloughReportDocId"/>
</portlet:resourceURL>
<portlet:resourceURL var="missingLeaveReportPdfUrl" id="missing_leave_report.pdf" escapeXml="false">
    <portlet:param name="docId" value="missingLeaveReportDocId"/>
</portlet:resourceURL>

<portlet:resourceURL var="sabbaticalReportsUrl" id="sabbaticalReports" escapeXml="false" />
<portlet:resourceURL var="sabbaticalReportPdfUrl" id="sabbatical_report.pdf" escapeXml="false">
    <portlet:param name="docId" value="TMPLT_*.docId_TMPLT"/>
</portlet:resourceURL>

<script type="text/javascript" language="javascript">
<rs:compressJs>
(function($, fluid, dl) {
   $(function() {

     // missing report ajax call
     // resolving eagerly to determine soonest whether to show the {n}leaveReportingNotice div.

     $.ajax({
       type: "POST",
       url: "${missingReportUrl}",
       data: "",
       success: function(response){
         var missingReport = response.report;
         if(missingReport != null || missingReport != undefined) {
           var reportLink =
             "${missingLeaveReportPdfUrl}".replace("missingLeaveReportDocId", missingReport.docId);
           $("#${n}oustandingMissingLeaveReports").attr("href",reportLink);
           $("#${n}oustandingMissingLeaveReports").show();
           $("#${n}leaveReportingNotice").show();
         }
       },
       error: function(e){
         var response = jQuery.parseJSON(e.responseText);
       }
     });

        var formatDataArray = function(dataArray) {
            var startYear = dataArray[0];
            var startMonth = dataArray[1];
            var startDate = dataArray[2];
            if (parseInt(startMonth) < 10)
            {
                startMonth = "0" + startMonth;
            }
            if (parseInt(startDate) < 10)
            {
                startDate = "0" + startDate;
            }
            return startMonth + "/" + startDate + "/" + startYear;
        }

        <sec:authorize ifAllGranted="ROLE_VIEW_ABSENCE_HISTORIES">
          dl.pager.init("#${n}dl-absence", {
            model: {
                sortKey: "start",
                sortDir: -1
            },
            summary: {
                type: "fluid.pager.summary",
                options: {
                  message: "%first-%last of %total absences"
                }
            },
            columnDefs: [
              dl.pager.colDef("name", {sortable: true}),
              dl.pager.colDef("status", {sortable: true}),
              dl.pager.colDef("start", {sortable: true, sortValueExtractor : dl.pager.dateExtractor}),
              dl.pager.colDef("end", {sortable: true, sortValueExtractor : dl.pager.dateExtractor}),
              dl.pager.colDef("total", {sortable: true, sortValueExtractor : dl.pager.numberExtractor}),
              dl.pager.colDef("title", {sortable: true})
            ],
            dataList: {
              url: "${absenceHistoriesUrl}",
              dataLoadErrorMsg: "${genericErrorMessage}",
              dataExtractor: function (dataKey, data) {
                  data = data.report;
                  $.each(data, function(index, absence) {
                      if (absence.job) {
                          absence.title = absence.job.title;
                          delete absence.job;
                      }
                      else {
                          absence.title = "";
                      }
                      absence.start = formatDataArray(absence.start);
                      absence.end = formatDataArray(absence.end);
                  });
                  return data;
              }
            }
          });
        </sec:authorize>

      <c:if test="${empty hrsUrls['Classic ESS Abs Bal']
      || empty employeeRoles['ROLE_LINK_TO_CLASSIC_ESS_ABS_BAL']}">
        dl.pager.init("#${n}dl-leave-balance", {
            model: {
                sortKey: "entitlement",
                sortDir: 1
            },
            columnDefs: [
               dl.pager.colDef("entitlement", {sortable: true}),
               dl.pager.colDef("balance", {sortable: true, sortValueExtractor : dl.pager.numberExtractor}),
               dl.pager.colDef("title", {sortable: true})
            ],
            dataList: {
              url: "${absenceBalancesUrl}",
              dataLoadErrorMsg: "${genericErrorMessage}",
              dataExtractor: function (dataKey, data) {
                  data = data.report;
                  $.each(data, function(index, absence) {
                      if (absence.job) {
                          absence.title = absence.job.title;
                          delete absence.job;
                      }
                      else {
                          absence.title = "";
                      }
                      absence.balance = new Number(absence.balance).toFixed(2);
                  });
                  return data;
              }
            }
          });
      </c:if>

        <sec:authorize ifAnyGranted="ROLE_VIEW_TIME_ENTRY_HISTORY">
          dl.pager.init("#${n}dl-time-entry", {
            model: {
                sortKey: "date",
                sortDir: -1
            },
            summary: {
                type: "fluid.pager.summary",
                options: {
                  message: "%first-%last of %total entries"
                }
            },
            columnDefs: [
               dl.pager.colDef("date", {sortable: true, sortValueExtractor : dl.pager.dateExtractor}),
               dl.pager.colDef("status", {sortable: true}),
               dl.pager.colDef("total", {sortable: true, sortValueExtractor : dl.pager.numberExtractor}),
               dl.pager.colDef("type", {sortable: true}),
               dl.pager.colDef("title", {sortable: true})
            ],
            dataList: {
                url: "${timeSheetsUrl}",
                dataLoadErrorMsg: "${genericErrorMessage}",
                dataExtractor: function (dataKey, data) {
                    data = data.report;
                    $.each(data, function(index, timesheet) {
                        if (timesheet.job) {
                            timesheet.title = timesheet.job.title;
                            delete timesheet.job;
                        }
                        else {
                            timesheet.title = "";
                        }
                        timesheet.date = formatDataArray(timesheet.date);
                        timesheet.total = new Number(timesheet.total).toFixed(2);
                    });
                    return data;
                }
              }
          });
        </sec:authorize>

        var noStatementsDiv = $("#${n}no-statements");
        noStatementsDiv.data("showStatus", { sabbatical: true, statements: true });
        noStatementsDiv.hide();

        dl.pager.init("#${n}dl-leave-statements", {
          model: {
              sortKey: "payPeriod",
              sortDir: -1
          },
          columnDefs: [
             dl.pager.colDef("payPeriod", {sortable: true, sortValueExtractor: dl.pager.mmyyyyDateExtractor}),
             {
                 key: "",
                 valuebinding: "*.leaveFurloughReportLinks",
                 components: {
                     markup: dl.util.templateUrl("TMPLT_*.leaveFurloughReportLinks_TMPLT")
                 }
             }
          ],
          dataList: {
            url: "${leaveStatementsUrl}",
            dataKey: "report",
            dataLoadCallback: function (data) {
                if (data == undefined || data.length == 0) {
                    //Hide the leave reports block
                    $("#${n}dl-leave-statements").hide();

                    //Increment the show count on the no statements block, if result is 2 show the no statements block
                    var showStatus = noStatementsDiv.data("showStatus");
                    showStatus.statements = false;
                    if (!showStatus.sabbatical && !showStatus.statements) {
                        noStatementsDiv.show();
                    }
                }
                else {
                    //Hide no statements block and decrement the show count
                    noStatementsDiv.hide();
                    noStatementsDiv.data("showStatus").statements = true;

                    $("#${n}dl-leave-statements").show();
                }
            },
            dataLoadErrorMsg: "${genericErrorMessage}",
            dataExtractor: function (dataKey, data) {
                data = data.report;
                $.each(data, function(index, leaveStatement) {
                    //leave / furlough Reports

                	if (leaveStatement.leaveReports && leaveStatement.leaveReports.length > 0) {
                      leaveStatement.leaveFurloughReportLinks = "";
                      $.each(leaveStatement.leaveReports, function (index, leaveFurloughReport) {
                          if (index > 0) {
                              leaveStatement.leaveFurloughReportLinks += ", ";
                          }
                          var reportLink = "${leaveFurloughReportPdfUrl}".replace("leaveFurloughReportDocId", leaveFurloughReport.docId);
                          leaveStatement.leaveFurloughReportLinks += '<a href="' + reportLink + '" target="_blank">' + leaveFurloughReport.title + '</a>';
                      });
                    }
                    else {
                        leaveStatement.leaveFurloughReportLinks = "&nbsp;";
                    }


                });

                return data;
            }
          }
        });

        dl.pager.init("#${n}dl-sabbatical-reports", {
          model: {
              sortKey: "payPeriod",
              sortDir: -1
          },
          columnDefs: [
             dl.pager.linkColDef("year", dl.util.templateUrl("${sabbaticalReportPdfUrl}"), {sortable: true}),
             dl.pager.linkColDef("fullTitle", dl.util.templateUrl("${sabbaticalReportPdfUrl}"), {sortable: true})
          ],
          dataList: {
            url: "${sabbaticalReportsUrl}",
            dataKey: "report",
            dataLoadCallback: function (data) {
            	if (data == undefined || data.length == 0) {
            		//Hide the sabbatical reports block
            		$("#${n}dl-sabbatical-reports").hide();

            		//Increment the show count on the no statements block, if result is 2 show the no statements block
            		var showStatus = noStatementsDiv.data("showStatus");
            		showStatus.sabbatical = false;
            		if (!showStatus.sabbatical && !showStatus.statements) {
            			noStatementsDiv.show();
            		}
            	}
            	else {
            		//Hide no statements block and decrement the show count
            		noStatementsDiv.hide();
            		noStatementsDiv.data("showStatus").sabbatical = true;

            		$("#${n}dl-sabbatical-reports").show();
            	}
            },
            dataLoadErrorMsg: "${genericErrorMessage}"
          }
        });

        dl.tabs("#${n}dl-tabs");

        dl.util.clickableContainer("#${n}dl-time-absence");
    });
})(dl_v1.jQuery, dl_v1.fluid, dl_v1);
</rs:compressJs>
</script>

</sec:authorize>
