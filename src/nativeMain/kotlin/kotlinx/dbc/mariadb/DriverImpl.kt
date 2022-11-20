package kotlinx.dbc.mariadb

import kotlinx.cinterop.allocArray
import kotlinx.cinterop.toKString
import kotlinx.dbc.Connection
import kotlinx.dbc.exceptions.SQLException
import mariadb.mysql_error
import mariadb.mysql_init
import mariadb.mysql_real_connect

actual object DriverImpl {
    actual fun connect(params: HashMap<String, Any>): Connection {
        val mysql = mysql_init(null) ?: throw SQLException("Can't init mysql")
        if(mysql_real_connect(
                mysql,
                params["host"].toString(),
                params["user"].toString(),
                params["password"].toString(),
                params["sb"].toString(),
                params["post"]?.toString()?.toUInt() ?: 3306.toUInt(),
                params["unix_socket"]?.toString(),
                params["flags"].toString().toULong()
            ) ==null) {
            throw Error("Can't connect: "+ mysql_error(mysql)!!.toKString())
        }

        TODO("Not yet implemented")
    }
}