<%@ include file="/WEB-INF/jsp/include.jsp"%>

<%@ tag bodyContent="empty" isELIgnored="false" %>

<%@ attribute name="appContext" required="false" %>

<c:if test="${empty appContext}">
  <c:set var="appContext" value="App"/>
</c:if>

<!-- Depends upon helpUrl **Request** attribute. -->

<c:if test="${not empty helpUrl}">
<div class="dl-help-link">
  <a
    id="help-link"
    href="${helpUrl}"
    target="_blank" rel="noopener noreferrer">
    ${appContext} help and resources
    <!-- material.io launch icon-->
    <svg id="launch-icon" xmlns="http://www.w3.org/2000/svg"
      width="24" height="24" viewBox="0 0 24 24">
      <path d="M0 0h24v24H0z" fill="none"/>
      <path d="M19 19H5V5h7V3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2v-7h-2v7zM14 3v2h3.59l-9.83 9.83 1.41 1.41L19 6.41V10h2V3h-7z"/>
    </svg>
  </a>
</div>
</c:if>
