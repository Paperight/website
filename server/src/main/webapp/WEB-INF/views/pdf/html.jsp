<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1>HTML to PDF</h1>
<div class="form">
	<form:form method="post" modelAttribute="htmlConversion" >
		<div style="float: left; margin-right: 30px;">
			<dl>
				<dt>PDF Conversion</dt>
					<br />
					<div class="note-conversion" id="pdfConversionNote">
						<span class="note">
							Docraptor limitations:
							<ul>
								<li>You cannot add external css to html. The css should be included in the html code of the page to be converted.</li>
								<li>When converting a file, all urls to resources (images, css, etc) should be absolute paths, publicly accessible by Docraptor.</li>
								<li>If you select One Up, Two Up and A5, DocRaptor will be called twice. This affects the monthly quotas.</li>
							</ul>
						</span>
					</div>
				<dd>
					<form:select path="pdfConversion" id="pdfConverter">
						<c:forEach var="converter" items="${pdfConversions}" >
						    <form:option value="${converter}" label="${converter.displayName}"/>
						</c:forEach>					
					</form:select>
					<form:errors path="pdfConversion" cssClass="error" element="label" />
				</dd>
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
				<dt>HTML Location
					<br /><span class="note">
					Available options:
					<div>
						<div><form:radiobutton path="" value="Http" name="htmlLocation" id="htmlLocationHttp" checked="checked" /> Web URL e.g. http://www.gutenberg.org/files/40909/40909-h/40909-h.htm</div>
						<div><form:radiobutton path="" value="File" name="htmlLocation" id="htmlLocationFile" /> File relative to FTP html upload folder e.g. mybook/mybook.html</div>
					</div>
					</span>
				</dt>
				<dd>
					<form:input path="html" type="text" cssClass="required"/>
					<form:errors path="*" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl id="cssSelectorContainer">
				<dt>CSS</dt>
				<dd>
					<form:select id="cssSelector" path="css" cssClass="required">
						<form:option value="" label="Select a css..." />
		    			<form:options items="${cssFiles}" /> 
					</form:select>
					<form:errors path="css" cssClass="error" element="label" />
				</dd>
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
