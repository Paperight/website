<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h3>Create new product</h3>
<div style="margin-bottom:20px"><a href="${ctx}/pdf/product/${pdfConversion.id}/cancel">Cancel conversion</a></div>
<div class="form">
	<form:form action="${ctx}/pdf/product" method="POST" modelAttribute="toProduct" enctype="multipart/form-data">
	
		<sessionConversation:insertSessionConversationId attributeName="toProduct"/>
		
		<form:hidden path="pdfConversion.originalFilename" />
		
		<div style="width:60%; float:left; margin-right:30px;">
			<c:if test="${not empty pdfConversion.oneUpFilename}">
			<dl>
				<dt>One Up Filename</dt>
				<dd>
					<form:input path="product.oneUpFilename" disabled="true" />
					<form:errors path="product.oneUpFilename" cssClass="error" element="label" />				
					<a href="${pdfConversion.oneUpFilename}" class="btn-preview-pdf">Preview</a><c:if test="${canDeleteLayout eq true}">&nbsp;&nbsp;<a href="${ctx}/pdf/product/${pdfConversion.id}/delete/${pdfConversion.oneUpFilename}/ONE_UP/" class="btn-delete-conversion">Delete</a></c:if>
				</dd>				
			</dl>
			</c:if>
			<c:if test="${not empty pdfConversion.twoUpFilename}">
			<dl>
				<dt>Two Up Filename</dt>
				<dd>
					<form:input path="product.twoUpFilename" disabled="true"/>
					<form:errors path="product.twoUpFilename" cssClass="error" element="label" />
					<a href="${pdfConversion.twoUpFilename}" class="btn-preview-pdf">Preview</a><c:if test="${canDeleteLayout eq true}">&nbsp;&nbsp;<a href="${ctx}/pdf/product/${pdfConversion.id}/delete/${pdfConversion.twoUpFilename}/TWO_UP/" class="btn-delete-conversion">Delete</a></c:if>					
				</dd>
			</dl>
			</c:if>
			<c:if test="${not empty pdfConversion.a5Filename}">
			<dl>
				<dt>A5 Filename</dt>
				<dd>
					<form:input path="product.a5Filename" disabled="true" />
					<form:errors path="product.a5Filename" cssClass="error" element="label" />				
					<a href="${pdfConversion.a5Filename}" class="btn-preview-pdf">Preview</a><c:if test="${canDeleteLayout eq true}">&nbsp;&nbsp;<a href="${ctx}/pdf/product/${pdfConversion.id}/delete/${pdfConversion.a5Filename}/A5/" class="btn-delete-conversion">Delete</a></c:if>
				</dd>				
			</dl>
			</c:if>	
			<dl>
				<dt>Identifier</dt>
				<dd><form:input path="product.identifier" cssClass="required" />
				<form:errors path="product.identifier" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Identifier Type</dt>
				<dd><form:input path="product.identifierType" cssClass="required" />
				<form:errors path="product.identifierType" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Title</dt>
				<dd><form:input path="product.title" cssClass="required" />
				<form:errors path="product.title" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Sub Title</dt>
				<dd><form:input path="product.subTitle" cssClass="" />
				<form:errors path="product.subTitle" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Licence fee in dollars</dt>
				<dd><form:input path="product.licenceFeeInDollars" class="price required" />
				<form:errors path="product.licenceFeeInDollars" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Jacket Image Filename</dt>
				<dd><form:input path="product.jacketImageFilename" />
				<form:errors path="product.jacketImageFilename" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Jacket Image Upload
				<br /><span class="note">Only .jpg and .png files allowed</span>
				</dt>
				<dd><form:input path="jacketImage" type="file" class="imageFile" />
				<form:errors path="jacketImage" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Copyright Status</dt>
				<dd><form:input path="product.copyrightStatus" />
				<form:errors path="product.copyrightStatus" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Publisher</dt>
				<dd><form:input path="product.publisher" />
				<form:errors path="product.publisher" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Alternative Title</dt>
				<dd><form:input path="product.alternativeTitle" />
				<form:errors path="product.alternativeTitle" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Primary Creators (Comma Separated)</dt>
				<dd><form:input path="product.primaryCreators" />
				<form:errors path="product.primaryCreators" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Secondary Creators (Comma Separated)</dt>
				<dd><form:input path="product.secondaryCreators" />
				<form:errors path="product.secondaryCreators" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Edition</dt>
				<dd><form:input path="product.edition" />
				<form:errors path="product.edition" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Primary Languages (Comma Separated)</dt>
				<dd><form:input path="product.primaryLanguages" />
				<form:errors path="product.primaryLanguages" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Secondary Languages (Comma Separated)</dt>
				<dd><form:input path="product.secondaryLanguages" />
				<form:errors path="product.secondaryLanguages" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Subject Area (Comma Separated)</dt>
				<dd><form:input path="product.subjectArea" />
				<form:errors path="product.subjectArea" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Publication Date</dt>
				<dd><form:input path="product.publicationDate" cssClass="date" data-date-format="YYYY-MM-DD"/>
				<form:errors path="product.publicationDate" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Embargo Date</dt>
				<dd><form:input path="product.embargoDate" cssClass="date" data-date-format="YYYY-MM-DD"/>
				<form:errors path="product.embargoDate" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Short Description</dt>
				<dd><form:textarea path="product.shortDescription" />
				<form:errors path="product.shortDescription" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Long Description</dt>
				<dd><form:textarea path="product.longDescription" />
				<form:errors path="product.longDescription" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Parent Isbn</dt>
				<dd><form:input path="product.parentIsbn" />
				<form:errors path="product.parentIsbn" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Alternate Isbn</dt>
				<dd><form:input path="product.alternateIsbn" />
				<form:errors path="product.alternateIsbn" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Audience</dt>
				<dd><form:input path="product.audience" />
				<form:errors path="product.audience" cssClass="error" element="label" /></dd>
			</dl>
		
			<dl>
				<dt>Disallowed Countries (Comma Separated ISO Country Codes)</dt>
				<dd><form:input path="product.disallowedCountries" />
				<form:errors path="product.disallowedCountries" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Tags (Comma Separated ISO Country Codes)</dt>
				<dd><form:input path="product.tags" />
				<form:errors path="product.tags" cssClass="error" element="label" /></dd>
			</dl>
			
			<dl>
				<dt>URL</dt>
				<dd><form:input path="product.url" />
				<form:errors path="product.url" cssClass="error" element="label" /></dd>
			</dl>
			
			<dl>
				<dt>URL Call to Action</dt>
				<dd><form:input path="product.urlCallToAction" />
				<form:errors path="product.urlCallToAction" cssClass="error" element="label" /></dd>
			</dl>
			
			<dl>
				<dt>Licence Statement</dt>
				<dd><form:input path="product.licenceStatement" />
				<form:errors path="product.licenceStatement" cssClass="error" element="label" /></dd>
			</dl>
			
		</div>
		
		<div style="clear:both; width:100%;">
			<input type="submit" class="btn btn-paperight" value="Update" />
		</div>
	</form:form>
</div>