package jdbcat.core

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import java.util.Date

/*
 * Create BIGINT column
 * Might not be supported by all database, please refer to your db documentation if "BIGINT" is supported
 * and if it is compatible with JDBC's getLong/setLong.
 */

class NullableBigIntColumn constructor(
    name: String,
    specifier: String? = null,
    table: Table
) : Column<Long?>(name = name, type = "BIGINT", specifier = specifier, table = table) {

    override fun getData(rs: ResultSet, paramIndex: Int): Long? {
        val value = rs.getLong(paramIndex)
        return if (rs.wasNull()) null else value
    }

    override fun setData(stmt: PreparedStatement, paramIndex: Int, value: Any?) {
        if (value == null) {
            stmt.setNull(paramIndex, Types.BIGINT)
        } else {
            stmt.setLong(paramIndex, value as Long)
        }
    }

    fun nonnull() = table.unregisterColumn(this).registerColumn(
        BigIntColumn(name = name, specifier = specifier, table = table)
    )
}

class BigIntColumn constructor(
    name: String,
    specifier: String? = null,
    table: Table
) : Column<Long>(name = name, type = "BIGINT NOT NULL", specifier = specifier, table = table) {

    override fun getData(rs: ResultSet, paramIndex: Int) = rs.getLong(paramIndex)

    override fun setData(stmt: PreparedStatement, paramIndex: Int, value: Any?) {
        stmt.setLong(paramIndex, value as Long)
    }
}

fun Table.bigInt(name: String, specifier: String? = null) = registerColumn(
    NullableBigIntColumn(name = name, specifier = specifier, table = this)
)
