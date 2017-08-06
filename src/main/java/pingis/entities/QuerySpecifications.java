/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.entities;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class QuerySpecifications {
    private QuerySpecifications() {}
    
    public static Specification<ChallengeImplementation> isInProgress() {
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

    public static Specification<Task> isTest() {
        return new Specification<Task>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<Task> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                return cb.equal(root.get(Task_.type), ImplementationType.TEST);
            }
        };
    }

    public static Specification<TaskImplementation> hasUser(User user) {
        return new Specification<TaskImplementation>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<TaskImplementation> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                return cb.equal(root.get(TaskImplementation_.user), user);
            }
        };
    }
    
    public static Specification<TaskImplementation> hasTask(Task task) {
        return new Specification<TaskImplementation>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<TaskImplementation> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                return cb.equal(root.get(TaskImplementation_.task), task);
            }
        };
    }
    
    public static Specification<Task> hasIndex(long index) {
        return new Specification<Task>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<Task> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                return cb.equal(root.get(Task_.index), index);
            }
        };
    }
    public static Specification<Task> hasChallenge(Challenge challenge) {
        return new Specification<Task>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<Task> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                return cb.equal(root.get(Task_.challenge), challenge);
            }
        };
    }
        
  
}
