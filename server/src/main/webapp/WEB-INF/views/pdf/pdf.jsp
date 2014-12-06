<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1>Existing PDF to PDF</h1>
<div class="form">
	<form:form method="post" modelAttribute="existingPdfConversion" enctype="multipart/form-data" >
		<div style="float: left; margin-right: 30px;">
			<dl>
				<dt>
					<form:checkbox path="amendExistingProduct" id="amendExistingProduct"  />
					<label for="amendExistingProduct">Amend existing product</label>
				</dt>
				<dd>
					<div id="existingProduct" style="display:none">
						<div class="selectfilters">
							<input id="productSearchQuery" type="text" placeholder="Search products..." />
							<button class="btn btn-paperight" id="searchProducts">&raquo;</button>
						</div>
						<div class="selectlists">
							<form:select path="existingProductId" size="15" style="width: 100%;">
							</form:select>
						</div>
					</div>
				</dd>
			</dl>
			<dl>
				<dt>Existing PDF
				<br /><span class="note">Only .pdf files allowed</span>
				</dt>
				<dd><form:input path="existingPdf" type="file" class="pdfFile required" />
				<form:errors path="existingPdf" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Layout(s)</dt>
				<dd>
					<div>
						<form:checkboxes path="layouts" items="${pageLayouts}" itemLabel="displayName" delimiter="<br />" cssClass="required" />
					</div>
					<div>
						<form:errors path="layouts" cssClass="error" element="label" />
					</div>
				</dd>
			</dl>						
			<br />

		</div>

		<div style="clear: both; width: 100%;">
			<input type="submit" class="btn btn-paperight" value="Convert" />
		</div>
	</form:form>
</div>