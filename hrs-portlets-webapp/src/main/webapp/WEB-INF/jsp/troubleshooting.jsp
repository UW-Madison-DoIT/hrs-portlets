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

  <portlet:renderURL var="personLookupUrl"></portlet:renderURL>
  <form class="user-email-search" action="${personLookupUrl}" method="post">
    <fieldset>
      <div>
        <label for="queriedEmplId">HR EmplID: </label><input type="text" name="queriedEmplId" />
      </div>
      <input type="submit" value="Lookup roles and earnings statements for employee by emplId" />
    </fieldset>
  </form>

  <h2>Roles</h2>
  <c:if test="${not empty queriedEmplId}">
    <p>emlpId ${queriedEmplId} has these HRS Portlets (MyUW app) roles:</p>
      <ul>

        <c:forEach var="role" items="${roles}">
          <li>${role}</li>
        </c:forEach>

        <c:if test="${empty roles}">
          <li>(none)</li>
        </c:if>
      </ul>

    <p>HRS Portlets are aware of these raw HRS roles for emplId ${queriedEmplId}:</p>

    <ul>

      <c:forEach var="rawRole" items="${rawRoles}">
        <li>${rawRole}</li>
      </c:forEach>

      <c:if test="${empty rawRoles}">
        <li>(none)</li>
      </c:if>

    </ul>

  </c:if>

  <p>In general, the HRS Portlets (MyUW app) map from raw HRS roles to MyUW HRS portlet app roles
     according to these rules:</p>

  <ul>
    <c:forEach var="mappingRule" items="${rules}">
      <c:choose>
        <c:when test="${mappingRule.multiplePortletRoles}">
          <li>
            HRS role <em>${mappingRule.hrsRole}</em> grants
            Portlet roles ${mappingRule.portletRolePhrase}.
          </li>
        </c:when>
        <c:otherwise>
          <li>
            HRS role <em>${mappingRule.hrsRole}</em> grants
            Portlet role ${mappingRule.portletRolePhrase}.
          </li>
        </c:otherwise>
      </c:choose>
    </c:forEach>
  </ul>

  <p>See also
    <a href="https://github.com/UW-Madison-DoIT/hrs-portlets/blob/uw-master/hrs-portlets-ps-impl/src/main/resources/app-context/psAppContext.xml">
      Documentation about the effects of HRS Portlet roles.
    </a>
  </p>

  <c:if test="${not empty queriedEmplId}">
  <h2>Earnings statements</h2>

  <c:choose>
  <c:when test="${empty earningsStatementsError}">
  <table>
    <tr>
      <th>Check date</th>
      <th>Earned</th>
      <th>Amount</th>
    </tr>

    <c:forEach var="earningsStatement" items="${earningsStatements}">
    <tr>
      <td><a href="${earningsStatement.url}"
        target="_blank" rel="noopener noreferrer">${earningsStatement.isoDateOfCheck}</a></td>
      <td>${earningsStatement.earnedPeriodLabel}</td>
      <td>${earningsStatement.amountNetPay}</td>
    </tr>
    </c:forEach>

  </table>
  <c:when>
  <c:otherwise>
  <p>Error querying earnings statements: ${earningsStatementsError}.</p>
  <c:otherwis>
  </c:choose>
  </c:if>

  <%@ include file="/WEB-INF/jsp/footer.jsp"%>

</div>
