package kotlinx.dbc.mariadb

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import kotlinx.dbc.Connection
import kotlinx.dbc.Statement
import mariadb.MYSQL
import mariadb.mysql_close
import mariadb.mysql_error

class MariaDBConnection(
    val mysql: CPointer<MYSQL>
): Connection {
    override fun close() = mysql_close(mysql)

    override fun createStatement(sql: String): Statement {
        return MariaDBStatement(this, sql)
    }

    fun getError() = mysql_error(mysql)!!.toKString()
}