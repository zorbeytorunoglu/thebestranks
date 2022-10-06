package com.zorbeytorunoglu.thebestranks.databases

import com.zorbeytorunoglu.thebestranks.TBR
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class Sql(private val plugin: TBR) {

    private var connection: Connection? = null

    fun getConnection(): Connection? {
        return connection
    }

    fun connect() {

        try {
            this.connection=DriverManager.getConnection(
                plugin.getSettingsHandler().getSqlHost()+plugin.getSettingsHandler().getSqlDatabaseName(),
                plugin.getSettingsHandler().getSqlUsername(),
                plugin.getSettingsHandler().getSqlPassword())
        } catch (exception: java.lang.Exception) {
            connection=null
        }

    }

    fun tableExists(tableName: String, connect: Boolean): Boolean {

        if (connect) connect()

        val resultSet: ResultSet = getConnection()!!.metaData.getTables(null, null, tableName, null)

        if (connect) getConnection()!!.close()

        return resultSet.next()

    }

    fun createTable(tableName: String, uuidColumn: String, rankColumn: String, connect: Boolean): Boolean {

        val sql: String = "CREATE TABLE IF NOT EXISTS $tableName ($uuidColumn TEXT, $rankColumn TEXT)"

        return try {
            if (connect) connect()
            getConnection()!!.createStatement().executeUpdate(sql)
            if (connect) getConnection()!!.close()
            true
        } catch (exception: SQLException) {
            exception.printStackTrace()
            false
        }

    }

    fun savePlayerRank(tableName: String, uuidColumn: String, rankColumn: String, uuid: String, rankId: String, connect: Boolean): Boolean {

        val select = "SELECT $uuidColumn FROM $tableName WHERE $uuidColumn = ?"
        val insert = "insert into ranks values(?,?)"
        val update = "UPDATE $tableName SET $rankColumn = $rankId WHERE `$uuidColumn` = ?"

        return try {

            if (connect) connect()

            val preparedStatement = getConnection()!!.prepareStatement(select)
            preparedStatement.setString(1, uuid)
            if (preparedStatement.executeQuery().next()) {
                val preparedStatementU = getConnection()!!.prepareStatement(update)
                preparedStatementU.setString(1, uuid)
                preparedStatementU.executeUpdate()
                if (connect) getConnection()!!.close()

            } else {

                val preparedStatementS = getConnection()!!.prepareStatement(insert)
                preparedStatementS.setString(1,uuid)
                preparedStatementS.setString(2,rankId)
                preparedStatementS.executeUpdate()
                if (connect) getConnection()!!.close()

            }

            true

        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }

    }

    fun isConnected(): Boolean {
        if (getConnection()==null) return false
        return (!getConnection()!!.isClosed)
    }

}