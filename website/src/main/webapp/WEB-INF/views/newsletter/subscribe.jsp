<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<h1><pr:snippet name="heading" group="subscribe-newsletter-form" defaultValue="Subscribe to our newsletter"/></h1>
<div>
	<!-- Begin MailChimp Signup Form -->
	<div class="form">
		<form
			action="http://paperight.us2.list-manage1.com/subscribe/post?u=d34d88428a57556e80d98735f&amp;id=106e056f48"
			method="post" id="mc-embedded-subscribe-form"
			name="mc-embedded-subscribe-form" target="_blank">
			<div>
				<dl>
					<dt>
						<span><pr:snippet name="email" group="subscribe-newsletter-form" defaultValue="Email address" /></span> <br />
						<span class="note"><pr:snippet name="email-note" group="subscribe-newsletter-form" defaultValue="" /></span>
					</dt>
					<dd><input type="text" value="" name="EMAIL" class="required email" id="mce-EMAIL"></dd>
				</dl>
				<dl>
					<dt>
						<span><pr:snippet name="first-name" group="subscribe-newsletter-form" defaultValue="First name" /></span> <br />
						<span class="note"><pr:snippet name="first-name-note" group="subscribe-newsletter-form" defaultValue="" /></span>
					</dt>
					<dd><input type="text" value="" name="FNAME" class="" id="mce-FNAME"></dd>
				</dl>
				<dl>
					<dt>
						<span><pr:snippet name="last-name" group="subscribe-newsletter-form" defaultValue="Last name" /></span> <br />
						<span class="note"><pr:snippet name="last-name-note" group="subscribe-newsletter-form" defaultValue="" /></span>
					</dt>
					<dd><input type="text" value="" name="LNAME" class="" id="mce-LNAME"></dd>
				</dl>
			</div>

			<div style="clear:both; width:100%;">
				<input type="submit" value="<pr:snippet name="button" group="subscribe-newsletter-form" defaultValue="Subscribe" />" name="subscribe"	id="mc-embedded-subscribe" class="button">
			</div>
		</form>
	</div>
	<!--End mc_embed_signup-->
</div>