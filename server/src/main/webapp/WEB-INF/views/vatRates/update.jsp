<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Update country VAT rates</h1>
<div class="form">
	<form:form action="${ctx}/vat-rate/update" method="POST" modelAttribute="vatRate" >
	
		<sessionConversation:insertSessionConversationId attributeName="vatRate"/>
	
		<div style="width:40%; float:left; margin-right:30px;">
			<dl>
				<c:set var="countryDisabled" value="false" />
				<c:if test="${not empty vatRate.countryCode}" >
				    <c:set var="countryDisabled" value="true" />
				</c:if>
				<dt>Country</dt>		
				<dd><form:select path="countryCode" cssClass="required" disabled="${countryDisabled}" >
						<form:option value="" label="Select a country..." />
		    			<form:options items="${countries}" itemLabel="name" /> 
					</form:select>
					<form:errors path="countryCode" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>
					<span>Rate 0</span>
					<br /><span class="note">e.g. enter "14" for 14% </span>
				</dt>
				<dd><form:input path="rate0" cssClass="number required" />
				<form:errors path="rate0" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span>Rate 1</span>
					<br /><span class="note">e.g. enter "14" for 14% </span>
				</dt>
				<dd><form:input path="rate1" cssClass="number required" />
				<form:errors path="rate1" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span>Rate 2</span>
					<br /><span class="note">e.g. enter "14" for 14% </span>
				</dt>
				<dd><form:input path="rate2" cssClass="number required" />
				<form:errors path="rate2" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Type</dt>
				<dd>
					<form:select path="type">
						<form:options />
					</form:select>
				<form:errors path="type" cssClass="error" element="label" /></dd>
			</dl>
			
		</div>
		
		<div style="clear:both; width:100%;">
			<input type="submit" class="btn btn-paperight" value="Update" />
		</div>
	</form:form>
</div>