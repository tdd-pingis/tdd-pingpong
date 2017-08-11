
package pingis.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import pingis.entities.TmcUserDto;
import pingis.services.DataImporter.UserType;

public class WithMockCustomUserSecurityContextFactory {
    
    private static final String TEST_EMAIL = "testmail@mail.com";
     
    public SecurityContext createSecurityContext(pingis.entities.User customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        TmcUserDto principal = new TmcUserDto(Long.toString(customUser.getId()), 
                                              customUser.getName(), 
                                              TEST_EMAIL, 
                                              customUser.isAdministrator());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, 
                                                                               "password", 
                                                                                principal.getAuthorities());
        
        context.setAuthentication(authentication);
        
        return context;
    }

}
