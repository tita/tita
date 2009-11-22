package at.ac.tuwien.ifs.tita.datasource.filter;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;

public class Collections extends HibernateDaoSupport {
    protected EntityManager entityManager;

    public Collection<? extends BaseEntity> filterList(
            Collection<? extends BaseEntity> list, BaseEntity filterEntity) {
        Example example = Example.create(filterEntity).enableLike()
                .ignoreCase();
        List<? extends BaseEntity> results = getHibernateTemplate()
                .findByExample(filterEntity);
        return list;
    }
}
