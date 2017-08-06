/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.entities;

import java.util.function.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import pingis.entities.ChallengeImplementation_;
/**
 *
 * @author lauri
 */
public class ChallengeImplementationSpecifications {
    private ChallengeImplementationSpecifications() {}
    
    public static Specification<ChallengeImplementation> isOpen() {
        return new Specification<ChallengeImplementation>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<ChallengeImplementation> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                return cb.equal(root.get(ChallengeImplementation_.status), CodeStatus.IN_PROGRESS);
            }
        };
    }
    
    public static Specification<ChallengeImplementation> hasTestUser(User user) {
        return new Specification<ChallengeImplementation>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<ChallengeImplementation> root,
                    CriteriaQuery<?> quert,
                    CriteriaBuilder cb) {
                return cb.equal(root.get(ChallengeImplementation_.testUser), user);
            }
        };        
    }
    public static Specification<ChallengeImplementation> hasImplUser(User user) {
        return new Specification<ChallengeImplementation>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<ChallengeImplementation> root,
                    CriteriaQuery<?> quert,
                    CriteriaBuilder cb) {
                return cb.equal(root.get(ChallengeImplementation_.implementationUser), user);
            }
        };        
    }
        
    
}
