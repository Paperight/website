<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Application Configuration</h1>
<div class="form">
	<form:form modelAttribute="application" method="post">
	
		<sessionConversation:insertSessionConversationId attributeName="application"/>
	
		<div style="width:40%; float:left; margin-right:30px;">
<!-- 			<dl> -->
<!-- 				<dt>Paperight Credit Base Currency</dt> -->
<%-- 				<dd><form:select path="paperightCreditBaseCurrencyCode" cssClass="required"> --%>
<%-- 						<form:option value="" label="Select a currency..." /> --%>
<%-- 		    			<form:options items="${currencies}" itemLabel="name" />  --%>
<%-- 					</form:select> --%>
<%-- 					<form:errors path="paperightCreditBaseCurrencyCode" cssClass="error" element="label" /></dd> --%>
<!-- 			</dl> -->
<!-- 			<dl> -->
<!-- 				<dt>Paperight Credit to Base Currency Rate</dt> -->
<%-- 				<dd><form:input path="paperightCreditToBaseCurrencyRate" cssClass="required" /> --%>
<%-- 				<form:errors path="paperightCreditToBaseCurrencyRate" cssClass="error" element="label" /></dd> --%>
<!-- 			</dl> -->
			<dl>
				<dt>Default currency</dt>
				<dd><form:select path="defaultCurrencyCode" cssClass="required">
						<form:option value="" label="Select a currency..." />
		    			<form:options items="${currencies}" itemLabel="name" /> 
					</form:select>
					<form:errors path="defaultCurrencyCode" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span>Default publisher earning percentage</span>
					<br /><span class="note">e.g. enter "80" for 80% </span>
				</dt>
				<dd><form:input path="publisherEarningPercent" cssClass="number required" />
				<form:errors path="publisherEarningPercent" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span>Default PDF sample range</span>
					<br /><span class="note">1. Print dialog format. e.g. "1-4, 6, 9-12"<br />2. Percentage. e.g. "20%"</span>
				</dt>
				<dd><form:input path="pdfSampleRange" cssClass="pagerange required" />
				<form:errors path="pdfSampleRange" cssClass="error" element="label" /></dd>
			</dl>
			
			<sec:authorize access="hasPermission(#user, 'PDF_GENERATION_CONFIGURATION')">
			<dl>
				<dt>Default HTML conversion PDF generator</dt>
				<dd>
					<form:select path="defaultPdfConversion">
						<c:forEach var="converter" items="${pdfConversions}" >
						    <form:option value="${converter}" label="${converter.displayName}"/>
						</c:forEach>					
					</form:select>
					<form:errors path="defaultPdfConversion" cssClass="error" element="label" />
				</dd>
			</dl>
			</sec:authorize>
			<sec:authorize access="hasPermission(#user, 'INVOICE_PDF_GENERATION')">
			<dl>
				<dt>Invoice PDF generator</dt>
				<dd>
					<form:select path="invoicePdfConversion">
						<c:forEach var="converter" items="${pdfConversions}" >
						    <form:option value="${converter}" label="${converter.displayName}"/>
						</c:forEach>					
					</form:select>
					<form:errors path="invoicePdfConversion" cssClass="error" element="label" />
				</dd>
			</dl>
			</sec:authorize>
		</div>
		
		<div style="clear:both; width:100%;">
			<button class="btn btn-paperight" type="submit">Save details</button>
		</div>
	</form:form>
</div>