<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>

<h3>Bulk Import Jobs</h3>

<t:insertDefinition name="pagination">
    <t:putAttribute name="currentIndex" value="${currentIndex}" />
    <t:putAttribute name="beginIndex" value="${beginIndex}" />
    <t:putAttribute name="endIndex" value="${endIndex}" />
    <t:putAttribute name="totalPages" value="${totalPages}" />
    <t:putAttribute name="pageSize" value="${pageSize}" />
    <t:putAttribute name="pageItemCount" value="${pageItemCount}" />
    <t:putAttribute name="totalItemCount" value="${totalItemCount}" />
</t:insertDefinition>

<table class="table table-hover">
    <thead>
        <tr>
            <th>Date</th>
            <th>File</th>
            <th>Status</th>
            <th>Health</th>
            <th>Total Items (Processed)</th>
            <th>Items Status</th>
            <th>Items Health</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${importJobDtos}" var="importJobDto">
            <c:set var="importJob" value="${importJobDto.importJob}" />

            <c:set var="healthClass" value="label-success" />
            <c:set var="statusClass" value="label-success" />
            
            <c:if test="${importJob.status eq 'NEW'}">
                <c:set var="statusClass" value="label-warning" />
            </c:if>
            <c:if test="${importJob.health eq 'ERROR'}">
                <c:set var="healthClass" value="label-danger health-error" />
            </c:if>
            
            <c:choose>
                <c:when test="${fn:length(importJobDto.importItems) eq 0}">
                    <c:set var="itemsStatus" value="NA" />
                    <c:set var="itemsStatusClass" value="label-info" />
                    <c:set var="itemsHealth" value="NA" />
                    <c:set var="itemsHealthClass" value="label-info" />
                </c:when>
                <c:otherwise>
                    <c:set var="itemsStatus" value="${importJobDto.itemsStatus}" />
                    <c:set var="itemsHealth" value="${importJobDto.itemsHealth}" />

                    <c:set var="itemsStatusClass" value="label-success" />
                    <c:set var="itemsHealthClass" value="label-success" />
                    <c:if test="${itemsStatus eq 'PROCESSING'}">
                        <c:set var="itemsStatusClass" value="label-warning" />
                    </c:if>

                    <c:if test="${itemsHealth eq 'ERROR'}">
                        <c:set var="itemsHealthClass" value="label-danger" />
                    </c:if>
                </c:otherwise>
            </c:choose>

            <tr>
                <td><joda:format value="${importJob.createdDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                <td><a href="${ctx}/bulk/${importJob.id}">${importJob.baseFilename}</a></td>
                <td><span class="label ${statusClass}">${importJob.status}</span></td>
                <td><span class="health label ${healthClass}" data-toggle="tooltip" title="<c:out value="${importJob.error}" />">${importJob.health}</span></td>
                <td>${importJobDto.totalItemCount} (${importJobDto.totalProcessedItemCount})</td>
                <td><span class="label ${itemsStatusClass}">${itemsStatus}</span></td>
                <td><span class="label ${itemsHealthClass}">${itemsHealth}</span></td>
            </tr>
        </c:forEach>
    </tbody>
</table>