package at.ac.tuwien.ifs.tita.datasource.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.ac.tuwien.ifs.tita.datasource.domain.Role;
import at.ac.tuwien.ifs.tita.datasource.domain.User;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.datasource.service.IUserService;

public class Main {
    public static void main(String[] args) throws TitaDAOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "persistence-context.xml");

        IUserService service = (IUserService) ctx.getBean("userService");

        Role role1 = new Role();
        role1.setDescription("Das ist die TestRolle1");
        Role role2 = new Role();
        role2.setDescription("Das ist die TestRolle2");

        role1 = service.saveRole(role1);
        role2 = service.saveRole(role2);

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
        u2.setRole(role1);
        u2.setUserName("username2");

        u1 = service.saveUser(u1);
        u2 = service.saveUser(u2);

        System.err.println("-------------------------------------------------");
        System.err.println(service.getUserById(u1.getId()).getId());
        System.err.println(service.getUserById(u1.getId()).getRole());
        System.err.println(service.getUserById(u1.getId()).getRole().getId());
        System.err.println(service.getUserById(u2.getId()).getRole().getId());
        System.err.println("-------------------------------------------------");

        service.deleteRole(role1);
        service.deleteRole(role2);

        service.deleteUser(u1);
        service.deleteUser(u2);
    }
}
