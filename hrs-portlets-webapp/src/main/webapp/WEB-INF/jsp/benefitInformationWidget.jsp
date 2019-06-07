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

<portlet:defineObjects/>
<spring:htmlEscape defaultHtmlEscape="true" />

<div class="widget-body" layout="column" layout-align="center center">
  <sec:authorize
    ifAnyGranted="ROLE_VIEW_NEW_HIRE_BENEFITS, ROLE_VIEW_OPEN_ENROLL_BENEFITS">
    <span class="tsc__title">You have
      a<sec:authorize ifAnyGranted="ROLE_VIEW_OPEN_ENROLL_BENEFITS">n annual</sec:authorize>
      benefit enrollment opportunity.</span>

    <div class="tsc__status">
      <p>
        <md-button class="md-raised md-accent"
          href="${hrsUrls['Open Enrollment/Hire Event']}">Enroll now</md-button>
      </p>
    </div>
  </sec:authorize>
  <div class="tsc__extra-buttons" layout="row" layout-align="center center">
    <c:choose>
      <c:when test="${isMadisonUser}">
        <a
          target="_blank" rel="noopener noreferrer"
          href="https://hr.wisc.edu/benefits/annual-benefits-enrollment/">
          Learn more
        </a>
      </c:when>
      <c:otherwise>
        <a
          target="_blank" rel="noopener noreferrer"
          href="https://www.wisconsin.edu/ohrwd/benefits/">
          Learn more
        </a>
      </c:otherwise>
    </c:choose>
  </div>
</div>

<c:choose>
  <c:when test="${isMadisonUser}">
    <%-- Bug: will launch the wrong Benefit Information for Madison users
         in context of my.wisconsin --%>
    <launch-button
      data-href="/web/exclusive/university-staff-benefits-statement"
      data-target="_self"
      data-button-text="Launch full app"
      data-aria-label="Launch Benefit Information">
    </launch-button>
  </c:when>
  <c:otherwise>
    <launch-button
      data-href="/web/exclusive/uw-system-university-staff-benefits-statement"
      data-target="_self"
      data-button-text="Launch full app"
      data-aria-label="Launch Benefit Information">
    </launch-button>
  </c:otherwise>
</c:choose>
