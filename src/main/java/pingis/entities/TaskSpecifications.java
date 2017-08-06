/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.entities;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author lauri
 */
public class TaskSpecifications {
    private TaskSpecifications() {}
    
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
}
