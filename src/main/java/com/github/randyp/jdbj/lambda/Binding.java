package com.github.randyp.jdbj.lambda;

import com.github.randyp.jdbj.PreparedColumn;

import java.sql.SQLException;

/**
 * Encapsulates how to bind a specific named parameter to a prepared column.
 * <p>
 * For the most common bindings prefer using convenience methods on {@link com.github.randyp.jdbj.PositionalBindingsBuilder} and related classes such as {@link com.github.randyp.jdbj.PositionalBindingsBuilder#bindInt(String, int)}.
 * <p>
 * For more complex bindings you can sub-class/lambda {@link Binding} and call {@link com.github.randyp.jdbj.PositionalBindingsBuilder#bind(String, Binding)}.
 * <p>
 * Example using common bindings:
 * <pre>
 * {@code
 * List<Student> students = JDBJ.string("SELECT * FROM student LIMIT :limit").query()
 *                             .map(Student::from).toList()
 *                             .bindInt(":limit", 10)
 *                             .execute(db);
 * }
 * </pre>
 * <p>
 * Example using lambda:
 * <pre>
 * {@code
 * List<Student> students = JDBJ.string("SELECT * FROM student WHERE :lower <= id AND id < :upper").query()
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
 * List<Message> messages = JDBJ.string("SELECT * FROM message where created_dtm <= :max_dtm ORDER BY created_dtm DESC")
 *                             .query()
 *                             .map(Message::from).toList()
 *                             .bind(":max_dtm", new DateTimeBinding.Now())
 *                             .execute(db);
 * }
 * </pre>
 * 
 * @see com.github.randyp.jdbj.ValueBindingsBuilder
 * @see com.github.randyp.jdbj.PositionalBindingsBuilder
 * @see com.github.randyp.jdbj.CollectionBindingsBuilder
 */
public interface Binding {
    
    void bind(PreparedColumn preparedColumn) throws SQLException;

}
