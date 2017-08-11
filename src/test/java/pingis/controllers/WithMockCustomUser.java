package pingis.controllers;

import org.springframework.security.test.context.support.*;

/**
 * Used for testing of Spring Security user authentication. Defines a custom
 * authentication principal that is used to populate the SecurityContextHolder.
 * @author villburn
 */
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
	String id() default "1";
	String username() default "user";
	String email() default "testmail@com";
        boolean administrator() default false;
        String[] roles() default { "USER" }; 
}
