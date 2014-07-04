<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="deck">
	<div class="wrapper error-deck">
		<div class="column column-one">
			<h2>How it works</h2>
			<div class="call-to-action">
				<div class="logo-panel">
					<img src="${ctx}/img/howitworks.png" width="240" height="220" alt="Do you publish books, magazines or other documents?" />
				</div>
				<div class="description">
					<em>Do you publish books, magazines or other documents?</em>
					Save costs and reach new markets through Paperight's network of print-on-demand outlets.
					<div class="links">
						<a href="${ctx}/terms/publisher">Learn more</a>  |  <sec:authorize access="isAnonymous()"><a href="${ctx}/register?publisher=true">Register now</a></sec:authorize><sec:authorize access="isAuthenticated()"><a href="${ctx}/account/update">Register now</a></sec:authorize>
					</div>
				</div>
			</div>
		</div>
		<div class="column column-two">
			<div class="call-to-action">
				<div class="logo-panel">
					<img src="${ctx}/img/businessservices.png" width="240" height="220" alt="Does your business offer printing services?" />
				</div>
				<div class="description">						
					<em>Does your business offer printing services?</em>
					Offer your customers instant, legally printed books from our online library.<br /><br />
					<div class="links">
						<a href="${ctx}/terms/outlet">Learn more</a>  |  <sec:authorize access="isAnonymous()"><a href="${ctx}/register?outlet=true">Register as an outlet for free</a></sec:authorize><sec:authorize access="isAuthenticated()"><a href="${ctx}/account/update">Register as an outlet for free</a></sec:authorize>
					</div>
				</div>
			</div>
		</div>
		<div class="column column-three">
			<div class="call-to-action">
				<div class="logo-panel">
					<img src="${ctx}/img/needabook.png" width="240" height="220" alt="Need one of our books?" />
				</div>
				<div class="description">
					<em>Need one of our books?</em>
					Go to your nearest copy shop. If they register with Paperight, they can print out the books you need right there. Faster and easier than ordering from a book shop. 
					<div class="links">
						<a href="${ctx}/howto">Learn more</a>  |  <a href="/outlets" target="_blank">Find outlets</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>