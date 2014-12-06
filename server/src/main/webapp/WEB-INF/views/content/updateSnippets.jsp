<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Snippets</h1>
<c:if test="${not empty snippets}">
<div class="form">
<form:form method="POST" modelAttribute="poster" >
	<c:forEach items="${snippets}" var="snippet">
	<dl>
		<dt>
			<span>${snippet.name}</span>
		</dt>
		<dd>
		<c:if test="${snippet.multiline eq false }">
		<input type="text" id="${snippet_id_field_prefix}${snippet.id}" name="${snippet_id_field_prefix}${snippet.id}" value="<c:out value="${snippet.value}" />" class="" >
		</c:if>
		<c:if test="${snippet.multiline eq true }">
		<textarea id="${snippet_id_field_prefix}${snippet.id}" name="${snippet_id_field_prefix}${snippet.id}" class="required">
			<c:out value="${snippet.value}" />
		</textarea>
		</c:if>
		</dd>
	</dl>
	</c:forEach>
	<div style="clear:both; width:100%;">
		<input type="submit" class="btn btn-paperight" value="Update" />
	</div>
</form:form>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("textarea").height( $("textarea")[0].scrollHeight );
	
	$("textarea").css("background", "");
	//$("textarea").css("background", "");
});
</script>
</c:if>