/**
   Copyright 2009 TiTA Project, Vienna University of Technology
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE\-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package at.ac.tuwien.ifs.tita.dao.test.util;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;

/**
 * Class for executing sql files to fill and clean db.
 * @author herbert
 *
 * @param <T> persistenceClass
 * @param <ID> Serializeable ID
 */
public class NativeSqlExecutorDao<T,ID extends Serializable> 
    extends GenericHibernateDao<T,ID> implements INativeSqlExecutorDao<T,ID>,
    INativeSqlExecutor {
    
    private SQLFileParser parser        = new SQLFileParser();

    public NativeSqlExecutorDao(Class<T> persistenceClass) {
        super(persistenceClass);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public void executeSql(String query) {
        Session session = getSession();
        SQLQuery q;
        q = session.createSQLQuery(query);
        q.executeUpdate();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public void executeSqlFile(Object class1) throws Exception {
        List<String> statements = null;
        statements = parser.readSqlFile(class1);

        for (String stm : statements) {
            executeSql(stm);
        }
        
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(propagation=Propagation.REQUIRED)
    public List<Object> getQueryList(String str){
        
        Session session = getSession();
        SQLQuery q;
        q = session.createSQLQuery(str);
        return q.list();
    }
}
