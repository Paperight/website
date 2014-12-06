<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<div id="wrapper">
    <div id="sidebar-wrapper">
        <ul class="sidebar-nav">
            <t:useAttribute id="headline" name="headline" ignore="true" />
            <c:if test="${not empty headline}">
                <h1>${headline}</h1>
            </c:if>
            <div class="">
                <t:useAttribute id="list" name="columnOne"
                    classname="java.util.List" />
                <c:forEach var="item" items="${list}">
                    <t:insertAttribute value="${item}" flush="true" />
                </c:forEach>
            </div>
        </ul>
    </div>
    <div id="page-content-wrapper">
        <div class="page-content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="">
                <t:useAttribute id="list" name="columnTwo"
                    classname="java.util.List" />
                <c:forEach var="item" items="${list}">
                    <t:insertAttribute value="${item}" flush="true" />
                </c:forEach>
            </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%-- 

<div class="container-fluid">
	<div class="row">
		<div class="col-xs-2">
			<t:useAttribute id="headline" name="headline" ignore="true" />
			<c:if test="${not empty headline}">
				<h1>${headline}</h1>
			</c:if>
			<div class="">
				<t:useAttribute id="list" name="columnOne"
					classname="java.util.List" />
				<c:forEach var="item" items="${list}">
					<t:insertAttribute value="${item}" flush="true" />
				</c:forEach>
			</div>
		</div>
		<div class="col-xs-10">
			<div class="">
				<t:useAttribute id="list" name="columnTwo"
					classname="java.util.List" />
				<c:forEach var="item" items="${list}">
					<t:insertAttribute value="${item}" flush="true" />
				</c:forEach>
			</div>
		</div>
	</div>
</div> --%>