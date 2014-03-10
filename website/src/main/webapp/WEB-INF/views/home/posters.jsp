<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="posters">
	<div id="available-outlets" style="float:left; width:50%;">
		<div style="margin-left:20px;">
			<h2><pr:snippet name="postersLine1" group="homePage" defaultValue="Now available at Paperight outlets"/></h2>
			<p class="mute"><pr:snippet name="postersLine2" group="homePage" defaultValue="Copy shops that use Paperight can print out these books for you."/></p>
			<p><a target="_blank" href="/outlets"><pr:snippet name="postersLine3" group="homePage" defaultValue="Find your nearest outlet »"/></a></p>
		</div>
	</div>
	<div style="float:left; width:50%;">
		<div id="poster-grid">
			<c:forEach var="poster" items="${posters}" begin="0" end="7">
				<c:url var="url" value="${poster.linkUrl}" />
				<div class="product">
					<a href="${url}" title="${poster.title}" ><img src="${poster.imageUrl}" alt="${poster.title}" width="92" height="130" /></a>
				</div>
			</c:forEach>
		</div>
	</div>
</div>