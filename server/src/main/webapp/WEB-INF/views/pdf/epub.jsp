<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1>ePub to PDF</h1>
<div class="form">
	<form:form method="post" modelAttribute="epubFileUpload">
		<div style="width: 40%; float: left; margin-right: 30px;">
			<dl>
				<dt>File</dt>
				<dd>
					<form:input path="file" type="file" cssClass="epub"/>
					<form:errors path="*" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
				<dt>Layout(s)</dt>
				<dd>
					<form:select path="layout" cssClass="">
						<form:option value="">ALL</form:option>
						<form:options />
					</form:select>
					<form:errors path="*" cssClass="error" element="label" />
				</dd>
			</dl>
			
			<br />

		</div>

		<div style="clear: both; width: 100%;">
			<input type="submit" class="btn btn-paperight" value="Upload &amp; Convert" />
		</div>
	</form:form>
</div>