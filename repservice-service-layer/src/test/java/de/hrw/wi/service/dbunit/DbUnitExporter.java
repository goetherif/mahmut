package de.hrw.wi.service.dbunit;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

public class DbUnitExporter {
    private static final String dbURL = "jdbc:hsqldb:file:"
            + "../repservice-db-layer/database/repservice_db;shutdown=true;hsqldb.write_delay=false;";
    private static final String USER = "sa";
    private static final String password = "";

    /**
     * Export database into XML file for DBUnit.
     * 
     * @param args
     *            Does not process args
     * @throws Exception
     *             Does not handle exceptions
     */
    public static void main(String[] args) throws Exception {
        // database connection
        Connection jdbcConnection = DriverManager.getConnection(dbURL, USER, password);
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        // DBUnit's DatabaseSequenceFilter automatically determines table order
        // using foreign keys information. Without, IDatabaseTester.onSetup()
        // would produce constraint violation exceptions on foreign keys.
        ITableFilter filter = new DatabaseSequenceFilter(connection);

        // full database export
        IDataSet fullDataSet = new FilteredDataSet(filter, connection.createDataSet());

        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("db_full_export.xml"));
        connection.close();
        jdbcConnection.close();
    }
}
