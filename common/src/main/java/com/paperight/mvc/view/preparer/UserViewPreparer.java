package com.paperight.mvc.view.preparer;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.paperight.authentication.AuthenticationService;
import com.paperight.user.User;

@Component
@PreAuthorize("isAuthenticated()")
class UserViewPreparer extends ViewPreparerSupport {

	public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) throws PreparerException {
		if (AuthenticationService.currentActingUser() != null) {
			User user = AuthenticationService.currentAuthenticatedUser();
			User actingUser = AuthenticationService.currentActingUser();
			attributeContext.putAttribute("actingUser", new Attribute(actingUser));
			attributeContext.putAttribute("user", new Attribute(user));
		}
	}
}