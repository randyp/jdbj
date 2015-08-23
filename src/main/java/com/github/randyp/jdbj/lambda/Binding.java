package com.github.randyp.jdbj.lambda;

import com.github.randyp.jdbj.PreparedColumn;

import java.sql.SQLException;

/**
 * Perform a binding operation against a prepared column.
 * <p>
 * For the most common bindings prefer using convenience methods on {@link com.github.randyp.jdbj.ExecuteQuery} and related classes, such as {@link com.github.randyp.jdbj.ExecuteQuery#bindInt(String, int)}.
 * <p>
 * For more complex bindings you should sub-class/lambda {@link Binding}.
 * <p>
 * Example using common bindings:
 * <pre>
 * {@code
 * List<Student> students = JDBJ.query("SELECT * FROM student LIMIT :limit")
 *                             .map(Student::from).toList()
 *                             .bindInt(":limit", 10)
 *                             .execute(db);
 * }
 * </pre>
 * <p>
 * Example using lambda:
 * <pre>
 * {@code
 * List<Student> students = JDBJ.query("SELECT * FROM student WHERE :lower <= id AND id < :upper")
 *                             .map(Student::from).toList()
 *                             .bindLong(":lower", range.hasLower() ? range.lower() : 0)
 *                             .bind(":upper", pc->{
 *                                 if(range.hasUpper()){
 *                                   pc.setLong(range.upper());
 *                                 }else{
 *                                   pc.setDouble(Double.MAX_VALUE);
 *                                 }
 *                             })
 *                             .execute(db);
 * }
 * </pre>
 * <p>
 * Example using subclass:
 * <pre>
 * {@code
 * List<Message> messages = JDBJ.query("SELECT * FROM message where created_dtm <= :max_dtm ORDER BY created_dtm DESC")
 *                             .map(Message::from).toList()
 *                             .bind(":max_dtm", new DateTimeBinding.Now())
 *                             .execute(db);
 * }
 * </pre>
 */
public interface Binding {
    
    void bind(PreparedColumn preparedColumn) throws SQLException;

}
