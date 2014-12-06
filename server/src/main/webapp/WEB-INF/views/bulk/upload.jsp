<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Bulk Import Upload</h1>

<div class="form">
	<form:form method="post" modelAttribute="fileUpload" enctype="multipart/form-data">
	
		<div style="width:40%; float:left; margin-right:30px;">
			<dl>
				<dt>File</dt>
				<dd><input type="file" name="file">
				<form:errors path="*" />
				</dd>
			</dl>
			<br />
			
		</div>
		
		<div style="clear:both; width:100%;">
			<input class="btn btn-paperight" type="submit" value="Upload" />
		</div>
	</form:form>
</div>