<div id="intro-video" class="video-panel">
	<div class="video"><iframe width="460" height="280" src="http://www.youtube.com/embed/vW4lwI0C1I0?wmode=transparent" frameborder="0" allowfullscreen="true"></iframe></div>
	<div class="video-mask"></div>
</div>
<script type="text/javascript">
$('#intro-video').bind('click.intro', function(){
	$(this).unbind('click.intro');
	var frame = $(this).closest('.video-panel').find('iframe');
	frame.attr('src', frame.attr('src')+'&autoplay=1');
	$(this).find('.video-mask').fadeOut('slow');
});
</script>