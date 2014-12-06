<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<c:set var="nameDisabled" value="true" />
<c:if test="${empty article.name}">
	<c:set var="nameDisabled" value="false" />
</c:if>
<h1>Update Article</h1>
<div class="form">
	<form:form action="${ctx}/article/update" method="POST" modelAttribute="article">

		<sessionConversation:insertSessionConversationId attributeName="article" />
		
		<dl>
			<dt>
				<span>Name</span>
				<br /><span class="note">Used to identify articles in website URL i.e. http://www.paperight.com/article/my-article-name
				<br />Once this is created, all subsequent updates/revisions will be readonly.</span>
			</dt>
			<dd>
				<form:input path="name" disabled="${nameDisabled}" cssClass="required" />
				<form:errors path="name" cssClass="error" element="label" />
			</dd>
		</dl>

		<dl>
			<dt>
				<span>Title</span>
				<br /><span class="note">Used in browser window/tab title, and above the page content as an H1 header</span>
			</dt>
			<dd>
				<form:input path="title" cssClass="required" />
				<form:errors path="title" cssClass="error" element="label" />
			</dd>
		</dl>

		<dl>
			<dt>Content</dt>
			<dd>
				<form:textarea path="content" cssClass="required editor editor-full"/>
				<form:errors path="content" cssClass="error" element="label" />
			</dd>
		</dl>

		<div style="clear:both; width:100%;">
			<input type="submit" class="btn btn-paperight" value="Update" />
		</div>
	</form:form>
</div>

<script type="text/javascript">
	CKEDITOR.replace('editor4', {
		toolbar : [ {
			name : 'basicstyles',
			items : [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript',
					'Superscript', '-', 'RemoveFormat' ]
		} ],
		uiColor : '#94CA27',
		removePlugins : 'elementspath',
		resize_enabled : false
	});
</script>