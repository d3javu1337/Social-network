package org.d3javu.bd.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.d3javu.bd.filter.user.UserFilter;
import org.d3javu.bd.models.user.User;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager em;

    @Override
    public List<User> findAllByFilter(UserFilter filter) {

        var cb = em.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user);

        List<Predicate> predicates = new ArrayList<>();
        if(filter.firstName() != null && !filter.firstName().isBlank()) {
//            predicates.add(cb.like(user.get("firstName"), filter.firstName()));
            predicates.add(cb.like(user.get("firstName"), "%"+ filter.firstName()+"%"));
        }
        if(filter.firstName()!= null && !filter.lastName().isBlank()){
//            predicates.add(cb.like(user.get("lastName"), filter.lastName()));
            predicates.add(cb.like(user.get("lastName"), "%"+filter.lastName()+"%"));
        }
        if(filter.email() != null && !filter.email().isBlank()){
//            predicates.add(cb.like(user.get("email"), filter.email()));
            predicates.add(cb.like(user.get("email"), "%" +filter.email()+"%"));
        }

        criteria.where(predicates.toArray(Predicate[]::new));

        return em.createQuery(criteria).getResultList();
    }

}
