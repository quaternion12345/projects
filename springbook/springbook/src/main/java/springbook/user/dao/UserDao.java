package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

//public abstract class UserDao {
public class UserDao {
    private ConnectionMaker connectionMaker;

//    public UserDao(ConnectionMaker connectionMaker){
//        this.connectionMaker = connectionMaker;
//    }

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2,user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

//    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

//    private Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/springbook", "root", "1234");
//        return c;
//    }

/*
*   private static UserDao INSTANCE;
*   ...
*   private UserDao(ConnectionMaker connectionMaker) {
*       this.connectionMaker = connectionMaker;
*   }
*   public static synchronized UserDao getInstance() {
*       if (INSTANCE == null) INSTANCE = new UserDao();
*       return INSTANCE;
*   }
*/

/*
 *  private ConnectionMaker connectionMaker; // 읽기전용 인스턴스 변수
 *
 *  // 가변적인 인스턴스 변수
 *  private Connection c;
 *  private User user;
 *
 *  public User get(String id) throws ClassNotFoundException, SQLException {
 *      this.c = connectionMaker.makeConnection();
 *      ...
 *      this.user = new User();
 *      this.user.setId(rs.getString("id"));
 *      this.user.setName(rs.getString("name"));
 *      this.user.setPassword(rs.getString("password"));
 *      ...
 *      return this.user;
 *  }
 *
 *
 */

}
