<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<h1>Paperight posters</h1>

<form:form action="${ctx}/posters" method="GET" modelAttribute="theme">
	<div style="width:40%; float:left; margin-right:30px;">
		<dl>
			<dt>Theme</dt>
				<dd>
					<form:select path="id" onchange="$(this).closest('form').submit();">
						<form:options path="theme" items="${themes}" itemValue="id" itemLabel="name" />
 					</form:select>
 				</dd>
			</dl>
		</div>

</form:form>

<div class="form" style="clear: both;">
	<span class="note">* Drag and drop to order.</span>
</div>
<c:if test="${not empty posters}">
<table id="posters-grid" class="grid" style="width:100%">
	<thead>
		<tr class="header">
			<td>Title</td>
			<td class="right-column"></td>
			<td class="right-column"></td>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${posters}" var="poster">
	<tr id="${poster.id}">
		<td>${poster.title}</td>
		<td class="right-column"><a href="${ctx}/poster/update/${poster.id}">Edit</a></td>
		<td class="right-column"><a class="delete" href="${ctx}/poster/delete/${poster.id}">Delete</a></td>
	</tr>
	</c:forEach>
	</tbody>
</table>
</c:if>

<script type="text/javascript">

var fixHelperModified = function(e, tr) {
    var $originals = tr.children();
    var $helper = tr.clone();
    $helper.children().each(function(index)
    {
      $(this).width($originals.eq(index).width());
    });
    return $helper;
};

$("#posters-grid tbody").sortable({
    helper: fixHelperModified,
	update: function(event, ui) {
		var order = 1, map = {}, id;
		$(this).find('tr').each(function(){
			id = $(this).attr('id');
			map[id] = order;
			order++;
		});
        $.ajax({
            url: "${ctx}/posters/reorder",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(map),
            dataType: "json",
            error: function(){
                alert("error updating rows");
            }
        });
	}
    
}).disableSelection();
</script>