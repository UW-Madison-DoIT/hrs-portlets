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

<div id="${n}dl-time-absence" class="fl-widget portlet dl-time-absence hrs">

  <sec:authorize ifAnyGranted="ROLE_VIEW_MANAGED_ABSENCES,ROLE_VIEW_MANAGED_TIMES">
  <div>
      <div class="dl-banner-links">
        <div class='dl-banner-link'>
            <a href="${hrsUrls['Time Management']}" target="_blank">Manager Self Service - Time Management</a>
        </div>

        <hrs:helpLink appContext="Approvals" />
      </div>

      <hrs:notification/>
  </div>
  <div>
    <span class="dl-link">
      <a href="${hrsUrls['Approve Absence']}" target="_blank" class="btn btn-default">Approve Absence</a>
    </span>
    <span class='hidden-xs visible-xs'>|</span>
    <span class='dl-link'>
      <a href="${hrsUrls['Approve Payable time']}" target="_blank" class="btn btn-default">Approve Payable Time</a>
    </span>
  </div>

  <div id="${n}dl-tabs" class="dl-tabs ui-tabs ui-widget ui-widget-content ui-corner-all inner-nav-container">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all inner-nav">
      <c:set var="activeTabStyle" value="ui-tabs-selected ui-state-active"/>
      <sec:authorize ifAnyGranted="ROLE_VIEW_MANAGED_ABSENCES">
        <li class="ui-state-default ui-corner-top ${activeTabStyle}"><a href="#${n}dl-absence">Absence</a></li>
        <c:set var="activeTabStyle" value=""/>
      </sec:authorize>
      <sec:authorize ifAnyGranted="ROLE_VIEW_MANAGED_TIMES">
        <li class="ui-state-default ui-corner-top ${activeTabStyle}"><a href="#${n}dl-time">Time</a></li>
      </sec:authorize>
    </ul>
    <c:set var="hiddenTabStyle" value=""/>
    <sec:authorize ifAnyGranted="ROLE_VIEW_MANAGED_ABSENCES">
      <c:set var="hiddenTabStyle" value="ui-tabs-hide"/>
      <div id="${n}dl-absence" class="dl-absence ui-tabs-panel ui-widget-content ui-corner-bottom">
        <div class="fl-pager">
          <hrs:pagerNavBar position="top" showSummary="${true}" />
          <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
            <table class="dl-table table">
              <thead>
                <tr rsf:id="header:">
                  <th class="flc-pager-sort-header" rsf:id="name"><a href="javascript:;">Name</a></th>
                  <th class="flc-pager-sort-header" rsf:id="status"><a href="javascript:;">Status</a></th>
                </tr>
              </thead>
              <tbody>
                  <tr rsf:id="row:">
                    <td class="dl-data-text"><span rsf:id="name"></span></td>
                    <td class="dl-data-text"><span rsf:id="status"></span></td>
                  </tr>
              </tbody>
            </table>
          </div>
          <hrs:pagerNavBar position="bottom" />
        </div>
        <div>
          <a class="dl-refresh" href="#">Refresh</a><br/>
        </div>
      </div>
    </sec:authorize>
    <sec:authorize ifAnyGranted="ROLE_VIEW_MANAGED_TIMES">
      <div id="${n}dl-time" class="dl-time ui-tabs-panel ui-widget-content ui-corner-bottom ${hiddenTabStyle}">
        <div class="fl-pager">
          <hrs:pagerNavBar position="top" showSummary="${true}" />
          <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
            <table class="dl-table table">
              <thead>
                <tr rsf:id="header:">
                  <th class="flc-pager-sort-header" rsf:id="name"><a href="javascript:;">Name</a></th>
                  <th class="flc-pager-sort-header" rsf:id="status"><a href="javascript:;">Status</a></th>
                </tr>
              </thead>
              <tbody>
                  <tr rsf:id="row:">
                    <td class="dl-data-text"><span rsf:id="name"></span></td>
                    <td class="dl-data-text"><span rsf:id="status"></span></td>
                  </tr>
              </tbody>
            </table>
          </div>
          <hrs:pagerNavBar position="bottom" />
        </div>
        <div>
          <a class="dl-refresh" href="#">Refresh</a><br/>
        </div>
      </div>
    </sec:authorize>
  </div>
  </sec:authorize>
  <sec:authorize ifNotGranted="ROLE_VIEW_MANAGED_ABSENCES,ROLE_VIEW_MANAGED_TIMES">
    <div class="center">This module is for managers. If you believe you are a manager and should see content, please contact HR.</div>
  </sec:authorize>

  <%@ include file="/WEB-INF/jsp/footer.jsp"%>

</div>

<portlet:resourceURL var="managedAbsencesUrl" id="managedAbsences" escapeXml="false"/>
<portlet:resourceURL var="managedTimesUrl" id="managedTimes" escapeXml="false"/>

<script type="text/javascript" language="javascript">
<rs:compressJs>
(function($, dl) {
    $(function() {
        <sec:authorize ifAnyGranted="ROLE_VIEW_MANAGED_ABSENCES">
          dl.pager.init("#${n}dl-absence", {
            columnDefs: [
              dl.pager.colDef("name", {sortable: true}),
              dl.pager.colDef("status", {sortable: true})
            ],
            dataList: {
                url: "${managedAbsencesUrl}",
                dataKey: "report",
                dataLoadErrorMsg: "${genericErrorMessage}"
            }
          });
        </sec:authorize>

        <sec:authorize ifAnyGranted="ROLE_VIEW_MANAGED_TIMES">
          dl.pager.init("#${n}dl-time", {
            columnDefs: [
              dl.pager.colDef("name", {sortable: true}),
              dl.pager.colDef("status", {sortable: true})
            ],
            dataList: {
                url: "${managedTimesUrl}",
                dataKey: "report",
                dataLoadErrorMsg: "${genericErrorMessage}"
            }
          });
        </sec:authorize>

        dl.tabs("#${n}dl-tabs");

        dl.util.clickableContainer("#${n}dl-time-absence");
    });
})(dl_v1.jQuery, dl_v1);
</rs:compressJs>
</script>
