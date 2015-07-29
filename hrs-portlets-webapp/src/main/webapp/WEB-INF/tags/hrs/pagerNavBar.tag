<%@ include file="/WEB-INF/jsp/include.jsp"%>

<%@ tag dynamic-attributes="attributes" isELIgnored="false" %>

<%@ attribute name="position" description="Either 'top' or 'bottom'"  required="true" %>
<%@ attribute name="showSummary" description="If the summary text should be shown" %>
<%@ attribute name="hideNav" description="If the entire nav bar should be hidden" %>

<div class="flc-pager-${position} dl-pager-navbar" style="display:none">
  <ul class="dl-pager-bar fl-pager-ui uw-pager-bar">
    <li class="flc-pager-previous dl-pager-previous">
      <a href="#" title="Previous Page" class="btn btn-default">Previous</a>
    </li>
    <li>
      <ul class="flc-pager-links dl-pager-links">
        <li class="flc-pager-pageLink dl-pager-pageLink-default"><a href="#" class="btn btn-default">1</a></li>
        <li class="flc-pager-pageLink"><a href="#" class="btn btn-default">2</a></li>
      </ul>
    </li>
    <li class="flc-pager-next dl-pager-next"><a href="#" title="Next Page" class="btn btn-default">Next</a></li>
    <%-- <c:if test="${showSummary == true}">
      <li class="flc-pager-summary hrs-pager-summary"></li>
    </c:if> --%>
  </ul>
</div>
