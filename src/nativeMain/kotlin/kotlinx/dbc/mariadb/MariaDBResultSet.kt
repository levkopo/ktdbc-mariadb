package kotlinx.dbc.mariadb

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import kotlinx.dbc.ResultSet
import kotlinx.dbc.exceptions.SQLException
import mariadb.*

class MariaDBResultSet(
    private val result: CPointer<MYSQL_RES>
): ResultSet {
    private var currentRow: MYSQL_ROW? = null
    private val fields = lazy {
        ArrayList<String>().apply {
            while (true) {
                val field = mysql_fetch_field(result) ?: break
                add(field.pointed.name!!.toKString())
            }
        }
    }

    override fun close() = mysql_free_result(result)
    override fun findColumn(columnIndex: String): Int = fields.value.indexOf(columnIndex)
    override fun getInt(columnIndex: Int): Int = currentRow?.get(columnIndex)?.toKString()?.toInt() ?: throwOnNullRow()
    override fun getLong(columnIndex: Int): Long = currentRow?.get(columnIndex)?.toKString()?.toLong() ?: throwOnNullRow()
    override fun getObject(columnIndex: Int): Any = currentRow?.get(columnIndex)?.toKString() ?: throwOnNullRow()
    override fun getString(columnIndex: Int): String = currentRow?.get(columnIndex)?.toKString() ?: throwOnNullRow()

    private fun <R> throwOnNullRow(): R = throw SQLException("Row not selected")

    override fun next(): Boolean {
        currentRow = mysql_fetch_row(result)
        return currentRow == null
    }
}