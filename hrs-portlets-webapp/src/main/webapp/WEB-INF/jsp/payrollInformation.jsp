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

<div id="${n}dl-payroll-information" class="fl-widget portlet dl-payroll-information hrs">
  <div style='margin: 0 10px;'>
      <div class="dl-banner-links">
        <div class="dl-help-link">
          <a href="${helpUrl}" target="_blank">Help</a>
        </div>
      </div>
    
      <hrs:notification/>
  </div>

  <div id="${n}dl-tabs" class="dl-tabs ui-tabs ui-widget ui-widget-content ui-corner-all inner-nav-container">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all inner-nav">
      <%--
        set initial tab selection based on optional portlet request param
      --%>
      <c:choose>
        <c:when test="${requestedContent eq 'Tax Statements'}">
          <li class="ui-state-default ui-corner-top">
            <a href="#${n}dl-earning-statements">Earning Statements</a>
          </li>
          <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
            <a href="#${n}dl-tax-statements">Tax Statements</a>
          </li>
        </c:when>
        <c:otherwise>
          <%-- default to Earnings Statements as default tab. --%>
          <li class="ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
            <a href="#${n}dl-earning-statements">Earning Statements</a>
          </li>
          <li class="ui-state-default ui-corner-top">
            <a href="#${n}dl-tax-statements">Tax Statements</a>
          </li>
        </c:otherwise>
    </c:choose>
    </ul>
    <div id="${n}dl-earning-statements" class="dl-earning-statements ui-tabs-panel ui-widget-content ui-corner-bottom">
      <div class="data-table-description-header">
        <div class="data-table-description">
          Your Net Pay Check amount is reflected on each individual Earnings Statement
        </div>
        <div class="data-table-details">
          <form action="#">
            <label for="${n}dl-earnings-amount-toggle">Show earnings dollar amounts </label>
            <input id="${n}dl-earnings-amount-toggle" name="dl-earnings-amount-toggle" type="checkbox"/>
          </form>
        </div>
      </div>
      <div class="fl-pager">
        <hrs:pagerNavBar position="top" showSummary="${true}"/>
        <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
          <table class="dl-table table" tabindex="0" aria-label="Earning Statements detail table">
            <thead>
              <tr rsf:id="header:">
                <th scope="col" class="flc-pager-sort-header dl-col-25p" rsf:id="paid"><a href="javascript:;">Paid</a></th>
                <th scope="col" class="flc-pager-sort-header dl-col-50p" rsf:id="earned"><a href="javascript:;">Earned</a></th>
                <th scope="col" class="flc-pager-sort-header dl-col-25p" rsf:id="amount"><a href="javascript:;">Amount</a></th>
              </tr>
            </thead>
            <tbody>
                <tr rsf:id="row:" class="dl-clickable">
                  <td headers="paid" class="dl-data-text"><a href="#" target="_blank" rsf:id="paid"></a></td>
                  <td headers="earned" class="dl-data-text"><a href="#" target="_blank" rsf:id="earned"></a></td>
                  <td headers="amount" class="dl-data-number"><a class="dl-earning-amount" href="#" target="_blank" rsf:id="amount"></a></td>
                </tr>
            </tbody>
          </table>
        </div>
        <hrs:pagerNavBar position="bottom" />
      </div>
      <c:if test="${not empty understandingEarningUrl}">
          <div class="dl-link">
            <a href="${understandingEarningUrl}" target="_blank">Understanding Your Earnings Statement</a>
          </div>
      </c:if>
    </div>
    <div id="${n}dl-tax-statements" class="dl-tax-statements ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide">

      <div class="dl-payroll-links">
        <c:if test="${not empty hrsUrls['View W-2']}}"
          <a class="btn btn-primary"
            href="${hrsUrls['View W-2']}"
            target="_blank" rel="noopener noreferrer">View W-2</a>
        </c:if>
        <c:if test="${not empty hrsUrls['W-2 Consent']}}"
          <a class="btn btn-default"
            href="${hrsUrls['W-2 Consent']}"
            target="_blank" rel="noopener noreferrer">
            Consent to receive W-2 electronically</a>
        </c:if>
        <c:if test="${not empty hrsUrls['View 1095-C']}}"
          <a class="btn btn-primary"
            href="${hrsUrls['View 1095-C']}"
            target="_blank" rel="noopener noreferrer">View 1095-C</a>
        </c:if>
        <c:if test="${not empty hrsUrls['1095-C Consent']}}"
          <a class="btn btn-default"
            href="${hrsUrls['1095-C Consent']}"
            target="_blank" rel="noopener noreferrer">
            Consent to receive 1095-C electronically</a>
        </c:if>
      </div>

      <div class="fl-pager">
        <div class="fl-container-flex dl-pager-table-data fl-pager-data table-responsive">
          <table class="dl-table table" tabindex="0" aria-label="Tax Statement table">
            <thead>
              <tr rsf:id="header:">
                <th scope="col" class="flc-pager-sort-header dl-col-5p" rsf:id="year"><a href="javascript:;">Year</a></th>
                <th scope="col" class="flc-pager-sort-header" rsf:id="name"><a href="javascript:;">Statement</a></th>
              </tr>
            </thead>
            <tbody>
                <tr rsf:id="row:" class="dl-clickable">
                  <td headers="year" class="hrs-data-text dl-col-5p"><a href="#" target="_blank" rsf:id="year"></a></td>
                  <td headers="name" class="dl-data-text"><a href="#" target="_blank" rsf:id="name"></a></td>
                </tr>
            </tbody>
          </table>
        </div>
        <hrs:pagerNavBar position="bottom" />
      </div>
    </div>
  </div>
  
  <div class="dl-payroll-links">
    <div class="dl-link">
      <a href="https://uwservice.wisc.edu/docs/forms/pay-employee-withholding.pdf" target="_blank" class="btn btn-default">Update your W4</a>
      <span class='hidden-xs visible-xs'>|</span>
      <!-- show the self-service direct deposit link if authorized-->
      <sec:authorize ifAnyGranted="ROLE_VIEW_DIRECT_DEPOSIT">
        <!-- Only show the self-service direct deposit link if configured. -->
        <c:choose>
          <c:when test="${not empty prefs['directDepositSelfServiceUrl']
            && not empty prefs['directDepositSelfServiceUrl'][0]}">
            <a 
              href="${prefs['directDepositSelfServiceUrl'][0]}" 
              target="_blank" rel="noopener noreferrer"
              class="btn btn-default">
              Update your Direct Deposit</a>
          </c:when>
          <c:otherwise>
            <!-- even if authorized for self-service, if that URL is not set
              fall back on the PDF form. -->
            <a 
              href="https://uwservice.wisc.edu/docs/forms/pay-direct-deposit.pdf"
              target="_blank" rel="noopener noreferrer"
              class="btn btn-default">
              Update your Direct Deposit</a>
          </c:otherwise>
        </c:choose>
      </sec:authorize>
      <sec:authorize ifNotGranted="ROLE_VIEW_DIRECT_DEPOSIT">
        <!-- show the link to the PDF form 
        if not authorized for self-service -->
        <a 
          href="https://uwservice.wisc.edu/docs/forms/pay-direct-deposit.pdf" 
          target="_blank" rel="noopener noreferrer"
          class="btn btn-default">
          Update your Direct Deposit</a>
      </sec:authorize>
    </div>
    <div class="dl-link">
      <a href="https://uwservice.wisc.edu/docs/publications/tax-w2-explanation.pdf" target="_blank" class="btn btn-default">W-2 Explanation</a>
      <span class='hidden-xs visible-xs'>|</span>
      <a href="https://uwservice.wisc.edu/docs/publications/itx-1042s-explanation.pdf" target="_blank" class="btn btn-default">1042-S Explanation</a>
      <%-- Affordable Care Act links --%>
      <span class='hidden-xs visible-xs'>|</span>
      <a href="https://uwservice.wisc.edu/docs/publications/tax-1095c-explanation.pdf" target="_blank" class="btn btn-default">1095-C Explanation</a>
      <span class='hidden-xs visible-xs'>|</span>
      <a href="https://www.wisconsin.edu/ohrwd/aca/" target="_blank" class="btn btn-default">ACA Information</a>
    </div>
    <c:if test="${personalDataError or personalData.onVisa}">
      <div class="dl-link">
        <a href="http://www.online-tax.net/" target="_blank" class="btn btn-default">Glacier International Tax</a>
      </div>
      <c:if test="${personalDataError or personalData.madisonEmpl}">
        <div class="dl-link">
          <a href="https://uwservice.wisc.edu/docs/publications/tax-nonresident-alien-resources.pdf" target="_blank" class="btn btn-default">About Nonresident Tax Preparation</a>
        </div>
      </c:if>
    </c:if>
  </div>

  <%@ include file="/WEB-INF/jsp/footer.jsp"%>

</div>

<portlet:resourceURL var="earningStatementsUrl" id="earningStatements" escapeXml="false"/>
<portlet:resourceURL var="earningStatementPdfUrl" id="earning_statement.pdf" escapeXml="false">
    <portlet:param name="docId" value="TMPLT_*.docId_TMPLT"/>
</portlet:resourceURL>

<portlet:resourceURL var="taxStatementsUrl" id="taxStatements" escapeXml="false"/>
<portlet:resourceURL var="irsStatementPdfUrl" id="irs_statement.pdf" escapeXml="false">
    <portlet:param name="docId" value="TMPLT_*.docId_TMPLT"/>
</portlet:resourceURL>


<script type="text/javascript" language="javascript">
<rs:compressJs>
(function($, fluid, dl) {
    dl.jQuery(function() {
        
        <%-- Disable Right Clicks in Safari browsers --%>
        <%-- Safari (not webkit) does not honor context header for naming downloads
             If indexOf('Constructor')>0 then we are in Safari Browser--%>
        if (Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0) {
            $(".dl-clickable").live("contextmenu", function() {
                return false;
            });
        }
        
        $(document).ready(function(){
        
        var updateAmmountVisibility = function(checkbox) {
            var checked = checkbox.is(':checked');
            var ammounts = $("#${n}dl-payroll-information table.dl-table a.dl-earning-amount");
            if (ammounts.length == 0) {
                $.log("No ammount fields found to update, returning true");
                return true;
            }
            
            var ammountData = ammounts.data();
            if (ammountData.visibilityUpdated == checked) {
                $.log("Earnings Toggle toggled " + checked + " matches current state, returning false");
                return false;
            }
            
            ammountData.visibilityUpdated = checked;
            $.log("Earnings Toggle toggled: " + checked + " updating " + ammounts.length + " ammount fields");
            if (checked) {
                ammounts.show();
            }
            else {
                ammounts.hide();
            }
            
            return true;
        };
        
        var earningsToggle = $("#${n}dl-earnings-amount-toggle");
        $.log("Earnings Toggle: " + earningsToggle.length);
        earningsToggle.change(function() {
            updateAmmountVisibility(earningsToggle);
        });
        
        //Setup the pager with no data right away, this helps avoid page flicker when the data comes back from the ajax request
        var earningStatementUrl = dl.util.templateUrl("${earningStatementPdfUrl}");
        dl.pager.init("#${n}dl-earning-statements", {
          model: {
              sortKey: "paid",
              sortDir: -1
          },
          summary: {
              type: "fluid.pager.summary", 
              options: {
                message: "%first-%last of %total statements"
              }
          },
          columnDefs: [ 
             dl.pager.linkColDef("paid", earningStatementUrl, {sortable: true, sortValueExtractor: dl.pager.dateExtractor}),
             dl.pager.linkColDef("earned", earningStatementUrl, {sortable: true}),
             dl.pager.linkColDef("amount", earningStatementUrl, {sortable: true, sortValueExtractor: dl.pager.currencyExtractor})
          ],
          listeners : {
              onModelChange : function() {
                  dl.scheduleToggleEarnings(earningsToggle, updateAmmountVisibility);
                  //updateAmmountVisibility(earningsToggle);
              }
          },
          dataList: {
              url: "${earningStatementsUrl}",
              dataKey: "report",
              dataLoadErrorMsg: "${genericErrorMessage}"
          }
        });
        

        var taxStatementUrl = dl.util.templateUrl("${irsStatementPdfUrl}");
        dl.pager.init("#${n}dl-tax-statements", {
          model: {
              sortKey: "year",
              sortDir: -1
          },
          summary: {
              type: "fluid.pager.summary", 
              options: {
                message: "%first-%last of %total statements"
              }
          },
          columnDefs: [
             dl.pager.linkColDef("year", taxStatementUrl, {sortable: true}),
             dl.pager.linkColDef("name", taxStatementUrl, {sortable: true})
          ],
          dataList: {
              url: "${taxStatementsUrl}",
              dataKey: "report",
              dataLoadErrorMsg: "${genericErrorMessage}"
          }
        });
        
        dl.tabs("#${n}dl-tabs");
        
        dl.util.clickableContainer("#${n}dl-payroll-information");
        });
    });    
})(dl_v1.jQuery, dl_v1.fluid, dl_v1);
</rs:compressJs>
</script>
