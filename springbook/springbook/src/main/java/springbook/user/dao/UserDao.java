package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

//public abstract class UserDao {
public class UserDao {
//    private ConnectionMaker connectionMaker;
    private DataSource dataSource;

//    public UserDao(ConnectionMaker connectionMaker){
//        this.connectionMaker = connectionMaker;
//    }

//    public void setConnectionMaker(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }
    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException{
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection c = connectionMaker.makeConnection();
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2,user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws SQLException{
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection c = connectionMaker.makeConnection();
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;

        if(rs.next()){
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if(user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public void deleteAll() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("delete from users");
        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
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
