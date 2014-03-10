<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="branches">
	<div class="branch">
		<div class="map">
		
		<c:url value="http://maps.googleapis.com/maps/api/staticmap" var="mapImage">
			<c:param name="center" value="-33.978492,18.463808" />
			<c:param name="zoom" value="15" />
			<c:param name="size" value="192x192" />
			<c:param name="sensor" value="false" />
			<c:param name="markers" value="color:green|label:Paperight|-33.978492,18.463808" />
		</c:url>
		<c:url value="http://maps.google.co.za/maps" var="mapLink">
			<c:param name="q" value="-33.978492,18.463808" />
			<c:param name="ll" value="-33.978492,18.463808" />
			<c:param name="spn" value="0.002331,0.005284" />
			<c:param name="num" value="1" />
			<c:param name="ie" value="UTF8" />
			<c:param name="zoom" value="14" />
			<c:param name="t" value="m" />
			<c:param name="source" value="embed" />
		</c:url>
		
		<img width="192" height="192" src="${mapImage}" /><br />
		<small><a target="_blank" href="${mapLink}" style="color:#0000FF;text-align:left"><pr:snippet name="view-map-button" group="contactUsForm" defaultValue="View larger map"/></a></small>
		</div>
		<div class="details">
			<pr:snippet name="address" group="contactUsForm" multiline="true" defaultValue="&lt;h2&gt;Paperight head office&lt;/h2&gt;
&lt;div class=&quot;description&quot;&gt;
	3rd floor, Sunclare building&lt;br /&gt;
	21 Dreyer St&lt;br /&gt;
	Claremont&lt;br /&gt;
	7708&lt;br /&gt;
&lt;/div&gt;
&lt;dl&gt;
	&lt;dt&gt;Phone&lt;/dt&gt;
	&lt;dd&gt;+27 21 671 1278&lt;/dd&gt;
&lt;/dl&gt;
&lt;dl&gt;
	&lt;dt&gt;Fax&lt;/dt&gt;
	&lt;dd&gt;+27 88 021 671 1278&lt;/dd&gt;
&lt;/dl&gt;
&lt;dl&gt;
	&lt;dt&gt;Email&lt;/dt&gt;
	&lt;dd&gt;&lt;a href=&quot;mailto:team@paperight.com&quot;&gt;team@paperight.com&lt;/a&gt;&lt;/dd&gt;
&lt;/dl&gt;"/>
		</div>
	</div>
</div>