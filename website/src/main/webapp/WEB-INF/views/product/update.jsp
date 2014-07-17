<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h3>Update Product</h3>
<div class="form">
	<form:form action="${ctx}/product/update" method="POST" modelAttribute="product" >
	
		<sessionConversation:insertSessionConversationId attributeName="product" />
	
		<div style="width:40%; float:left; margin-right:30px;">
			<dl>
				<dt>Availability</dt>
				<dd>
					<form:select path="availabilityStatus" cssClass="required">
						<form:options itemLabel="displayName" />
					</form:select>
					<form:errors path="availabilityStatus" cssClass="error" element="label" />
				</dd>
			</dl>
			<dl>
                <dt><form:checkbox id="premium" path="premium" cssClass="" /><label for="premium"> Premium</label></dt>
                <dd><form:errors path="premium" cssClass="error" element="label" /></dd>
            </dl>
			<dl>
				<dt><form:checkbox id="disabled" path="disabled" cssClass="" /><label for="disabled"> Disabled</dt>
				<dd><form:errors path="disabled" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
                <dt><form:checkbox id="publisherInactive" path="publisherInactive" cssClass="" /><label for="publisherInactive"> Publisher Inactive</dt>
                <dd><form:errors path="publisherInactive" cssClass="error" element="label" /></dd>
            </dl>
            <dl>
                <dt><form:checkbox id="canPhotocopy" path="canPhotocopy" cssClass="" /><label for="canPhotocopy"> Photocopy Licence</label></dt>
                <dd><form:errors path="canPhotocopy" cssClass="error" element="label" /></dd>
            </dl>
			<dl>
				<dt>Identifier</dt>
				<dd><form:input path="identifier" cssClass="required" />
				<form:errors path="identifier" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Identifier Type</dt>
				<dd><form:input path="identifierType" cssClass="required" />
				<form:errors path="identifierType" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Title</dt>
				<dd><form:input path="title" cssClass="required" />
				<form:errors path="title" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Sub Title</dt>
				<dd><form:input path="subTitle" cssClass="" />
				<form:errors path="subTitle" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Copyright Status</dt>
				<dd><form:input path="copyrightStatus" />
				<form:errors path="copyrightStatus" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Publisher</dt>
				<dd><form:input path="publisher" />
				<form:errors path="publisher" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Alternative Title</dt>
				<dd><form:input path="alternativeTitle" />
				<form:errors path="alternativeTitle" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Primary Creators (Comma Separated)</dt>
				<dd><form:input path="primaryCreators" />
				<form:errors path="primaryCreators" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Secondary Creators (Comma Separated)</dt>
				<dd><form:input path="secondaryCreators" />
				<form:errors path="secondaryCreators" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Edition</dt>
				<dd><form:input path="edition" />
				<form:errors path="edition" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Primary Languages (Comma Separated)</dt>
				<dd><form:input path="primaryLanguages" />
				<form:errors path="primaryLanguages" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Secondary Languages (Comma Separated)</dt>
				<dd><form:input path="secondaryLanguages" />
				<form:errors path="secondaryLanguages" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Subject Area (Comma Separated)</dt>
				<dd><form:input path="subjectArea" />
				<form:errors path="subjectArea" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Publication Date</dt>
				<dd><form:input path="publicationDate" cssClass="date" />
				<form:errors path="publicationDate" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Embargo Date</dt>
				<dd><form:input path="embargoDate" cssClass="date" />
				<form:errors path="embargoDate" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Short Description</dt>
				<dd><form:textarea path="shortDescription" />
				<form:errors path="shortDescription" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Long Description</dt>
				<dd><form:textarea path="longDescription" />
				<form:errors path="longDescription" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Parent Isbn</dt>
				<dd><form:input path="parentIsbn" />
				<form:errors path="parentIsbn" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Alternate Isbn</dt>
				<dd><form:input path="alternateIsbn" />
				<form:errors path="alternateIsbn" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Audience</dt>
				<dd><form:input path="audience" />
				<form:errors path="audience" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Licence Fee in Dollars</dt>
				<dd><form:input path="licenceFeeInDollars" class="price required" />
				<form:errors path="licenceFeeInDollars" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>One Up Filename</dt>
				<dd><form:input path="oneUpFilename" />
				<form:errors path="oneUpFilename" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Two Up Filename</dt>
				<dd><form:input path="twoUpFilename" />
				<form:errors path="twoUpFilename" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>A5 Filename</dt>
				<dd><form:input path="a5Filename" />
				<form:errors path="a5Filename" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Jacket Image Filename</dt>
				<dd><form:input path="jacketImageFilename" />
				<form:errors path="jacketImageFilename" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Disallowed Countries (Comma Separated ISO Country Codes)</dt>
				<dd><form:input path="disallowedCountries" />
				<form:errors path="disallowedCountries" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>Tags (comma separated)</dt>
				<dd><form:input path="tags" />
				<form:errors path="tags" cssClass="error" element="label" /></dd>
			</dl>
			
			<dl>
				<dt>URL</dt>
				<dd><form:input path="url" />
				<form:errors path="url" cssClass="error" element="label" /></dd>
			</dl>
			
			<dl>
				<dt>URL Call to Action</dt>
				<dd><form:input path="urlCallToAction" />
				<form:errors path="urlCallToAction" cssClass="error" element="label" /></dd>
			</dl>
			
			<dl>
				<dt>
					<span>PDF sample range</span>
					<br /><span class="note">1. Print dialog format. e.g. "1-4, 6, 9-12"<br />2. Percentage. e.g. "20%"</span>
				</dt>
				<dd><form:input path="samplePageRange" cssClass="pagerange" />
				<form:errors path="samplePageRange" cssClass="error" element="label" /></dd>
			</dl>

			<dl>
				<dt>Licence Statement</dt>
				<dd><form:input path="licenceStatement" />
				<form:errors path="licenceStatement" cssClass="error" element="label" /></dd>
			</dl>
			<dl>
				<dt>
					<span>Publisher earning percentage</span>
					<br /><span class="note">e.g. enter "80" for 80% </span>
				</dt>
				<dd><form:input path="publisherEarningPercent" cssClass="number" />
				<form:errors path="publisherEarningPercent" cssClass="error" element="label" /></dd>
			</dl>
			
		</div>
		
		<div style="clear:both; width:100%;">
			<input type="submit" value="Update" />
		</div>
	</form:form>
</div>