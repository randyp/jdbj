package jdbj.lambda;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionRunnable {

    void run(Connection connection) throws SQLException;

}
