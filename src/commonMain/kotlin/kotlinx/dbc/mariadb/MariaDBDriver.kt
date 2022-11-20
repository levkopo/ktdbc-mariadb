package kotlinx.dbc.mariadb

import kotlinx.dbc.Connection
import kotlinx.dbc.Driver
import kotlinx.dbc.DriverManager

object MariaDBDriver: Driver {
    override val name: String = "mariadb"
    override val version: IntArray = intArrayOf(1, 0, 0)

    override fun connect(params: HashMap<String, Any>): Connection = DriverImpl.connect(params)
    
    init {
        DriverManager.register(MariaDBDriver)
    }
}