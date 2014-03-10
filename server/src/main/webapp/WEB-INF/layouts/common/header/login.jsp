<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<sec:authorize access="isAnonymous()">
<c:if test="${not empty param.error and param.error eq 'login_failed'}">
<div class="notification error">
	<p>Your log in attempt was not successful, try again. Caused: <strong>${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</strong></p>
</div>
</c:if>
<div id="login-panel">
	<div id="welcome-message">Welcome back! Please log in. </div>
	<div id="login-form-panel">
		<form id="login-form" method="post" action="${ctx}/login">
			<div>
				<div class="input-panel">
					<input name="username" id="email" class="required noplaceholder clearfocus" type="text" value="Email Address" />
				</div>
				<div class="input-panel">
					<input name="password" id="password" class="required password noplaceholder clearfocus" type="text" value="Password" />
				</div>
				<div class="btn-panel">
					<button type="submit">Log in</button>
				</div>
			</div>
			<div class="row-two">
				<input type="checkbox" id="rememberme" name="_spring_security_remember_me" value="true" />
				<label for="rememberme">This is a staff only computer (Remember me)</label>
			</div>
			<div>
				<span><a href="${ctx}/forgotpassword">Forgot your password?</a> | <a href="${ctx}/register">Register</a> with us if you don't have an account.</span>
			</div>
		</form>
	</div>
</div>

</sec:authorize>