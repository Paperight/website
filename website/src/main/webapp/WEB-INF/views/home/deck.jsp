<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="deck">
	<div class="wrapper">
		<div style="float:left; margin-top:55px; width:55%;">
			<div id="intro-video" class="video-panel" style="float:right; padding-right:30px;">
				<div class="video"><iframe width="460" height="280" src="http://www.youtube.com/embed/vW4lwI0C1I0?wmode=transparent" frameborder="0" allowfullscreen="true"></iframe></div>
				<div class="video-mask"></div>
			</div>
		</div>
		<div style="float:left; margin-top:55px; width:45%;">
			<div style="margin-right:60px;">
				<h2 style="margin-left:0;"><pr:snippet name="videoLine1" group="homePage" defaultValue="Turn your printer into a book shop"/></h2>
				<p><pr:snippet name="videoLine2" group="homePage" defaultValue="A local copy shop is now a whole book shop!"/></p>
				<p><pr:snippet name="videoLine3" group="homePage" defaultValue="Printing businesses register free with Paperight, then sell legal, low-cost print-outs of our books to their customers."/></p>
				<sec:authorize access="isAnonymous()"><a id="btn-register" href="${ctx}/register" class="button button-big">register</a></sec:authorize>
				<a id="btn-browse" href="${ctx}/browse" class="button button-big"><pr:snippet name="videoButton" group="homePage" defaultValue="browse"/></a>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$('#intro-video').bind('click.intro', function(){
	$(this).unbind('click.intro');
	var frame = $(this).closest('.video-panel').find('iframe');
	frame.attr('src', frame.attr('src')+'&autoplay=1');
	$(this).find('.video-mask').fadeOut('slow');
});
</script>