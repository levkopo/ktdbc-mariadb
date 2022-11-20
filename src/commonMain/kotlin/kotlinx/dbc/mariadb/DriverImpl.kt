package kotlinx.dbc.mariadb

import kotlinx.dbc.Connection

expect object DriverImpl {
    fun connect(params: HashMap<String, Any>): Connection
}