
package pingis.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import pingis.entities.TmcUserDto;
import pingis.services.DataImporter.UserType;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
     
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        TmcUserDto principal = new TmcUserDto(customUser.id(), 
                                              customUser.username(), 
                                              customUser.email(), 
                                              customUser.administrator());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, 
                                                                               "password", 
                                                                                principal.getAuthorities());
        
        context.setAuthentication(authentication);
        
        return context;
    }

}
