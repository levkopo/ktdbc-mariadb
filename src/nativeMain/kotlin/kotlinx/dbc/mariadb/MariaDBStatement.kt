package kotlinx.dbc.mariadb

import kotlinx.cinterop.toKString
import kotlinx.dbc.ResultSet
import kotlinx.dbc.Statement
import kotlinx.dbc.exceptions.SQLException
import mariadb.mysql_error
import mariadb.mysql_query
import mariadb.mysql_use_result

class MariaDBStatement(
    val mariaDBConnection: MariaDBConnection,
    val sql: String
) : Statement {

    override fun close() {
        //TODO
    }

    override fun execute(): ResultSet {
        if(mysql_query(mariaDBConnection.mysql, sql)==1) {
            throw SQLException("Can't run query: "+mariaDBConnection.getError())
        }

        return MariaDBResultSet(
            mysql_use_result(mariaDBConnection.mysql) ?:
            throw SQLException("Can't use query result: "+mariaDBConnection.getError())
        )
    }

    override fun executeUpdate() {
        //TODO
    }
}