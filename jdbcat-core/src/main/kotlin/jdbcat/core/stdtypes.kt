package jdbcat.core

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class NullableVarCharColumn constructor(
    name: String,
    val size: Int,
    specifier: String? = null,
    table: Table
) : Column<String?>(name = name, type = "varchar($size)", specifier = specifier, table = table) {

    override fun getData(rs: ResultSet, paramIndex: Int): String? = rs.getString(paramIndex)

    override fun setData(stmt: PreparedStatement, paramIndex: Int, value: Any?) {
        if (value == null) {
            stmt.setNull(paramIndex, Types.VARCHAR)
        } else {
            stmt.setString(paramIndex, value as String)
        }
    }

    fun nonnull() = table.unregisterColumn(this).registerColumn(
        VarCharColumn(name = name, size = size, specifier = specifier, table = table)
    )
}

class VarCharColumn constructor(
    name: String,
    val size: Int,
    specifier: String? = null,
    table: Table
) : Column<String>(name = name, type = "varchar($size) NOT NULL", specifier = specifier, table = table) {

    override fun setData(stmt: PreparedStatement, paramIndex: Int, value: Any?) {
        stmt.setString(paramIndex, value as String)
    }
    override fun getData(rs: ResultSet, paramIndex: Int) = rs.getString(paramIndex)!!
}

class NullableIntegerColumn constructor(
    name: String,
    specifier: String? = null,
    table: Table
) : Column<Int?>(name = name, type = "integer", specifier = specifier, table = table) {

    override fun getData(rs: ResultSet, paramIndex: Int): Int? {
        val value = rs.getInt(paramIndex)
        return if (rs.wasNull()) null else value
    }

    override fun setData(stmt: PreparedStatement, paramIndex: Int, value: Any?) {
        if (value == null) {
            stmt.setNull(paramIndex, Types.INTEGER)
        } else {
            stmt.setInt(paramIndex, (value as Number).toInt())
        }
    }

    fun nonnull() = table.unregisterColumn(this).registerColumn(
        IntegerColumn(name = name, specifier = specifier, table = table)
    )
}

class IntegerColumn constructor(
    name: String,
    specifier: String? = null,
    table: Table
) : Column<Int>(name = name, type = "integer NOT NULL", specifier = specifier, table = table) {

    override fun getData(rs: ResultSet, paramIndex: Int) = rs.getInt(paramIndex)
    override fun setData(stmt: PreparedStatement, paramIndex: Int, value: Any?) {
        stmt.setInt(paramIndex, (value as Number).toInt())
    }
}
