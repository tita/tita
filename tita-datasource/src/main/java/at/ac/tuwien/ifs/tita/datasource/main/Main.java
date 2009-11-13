package at.ac.tuwien.ifs.tita.datasource.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.ac.tuwien.ifs.tita.datasource.dao.IBaseDAO;
import at.ac.tuwien.ifs.tita.datasource.domain.Role;
import at.ac.tuwien.ifs.tita.datasource.domain.User;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        IBaseDAO<User> userDAO = (IBaseDAO<User>) ctx.getBean("userDAO");
        IBaseDAO<Role> roleDAO = (IBaseDAO<Role>) ctx.getBean("roleDAO");

        Role role1 = new Role();
        role1.setDescription("Das ist die TestRolle1");
        Role role2 = new Role();
        role2.setDescription("Das ist die TestRolle2");

        roleDAO.save(role1);
        roleDAO.save(role2);

        System.err.println(roleDAO.getById(Role.class, 1L).getDescription());
        System.err.println(roleDAO.getById(Role.class, 2L).getDescription());

        User u1 = new User();
        u1.setDeleted(false);
        u1.setEmail("email@user1.at");
        u1.setFirstName("User1");
        u1.setLastName("LastnameUser1");
        u1.setPassword("User1");
        u1.setRole(role1);
        u1.setUserName("username1");

        User u2 = new User();
        u2.setDeleted(false);
        u2.setEmail("email@user2.at");
        u2.setFirstName("User2");
        u2.setLastName("LastnameUser2");
        u2.setPassword("User2");
        u2.setRole(role2);
        u2.setUserName("username2");

        userDAO.save(u1);
        userDAO.save(u2);

        System.err.println(userDAO.getById(User.class, 1L).getUserName());
        System.err.println(userDAO.getById(User.class, 2L).getUserName());

        userDAO.delete(u1);
        userDAO.delete(u2);

        roleDAO.delete(role1);
        roleDAO.delete(role2);
    }
}
