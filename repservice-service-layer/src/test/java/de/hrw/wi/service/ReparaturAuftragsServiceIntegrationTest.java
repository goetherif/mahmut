package de.hrw.wi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hrw.wi.business.ReparaturAuftrag;
import de.hrw.wi.persistence.DatabaseImpl;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReparaturAuftragsServiceIntegrationTest {
    private static final String DB_URL = "jdbc:hsqldb:file:../repservice-db-layer/"
            + "database/repservice_db;shutdown=true;hsqldb.write_delay=false;";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    IDatabaseTester databaseTester;
    DatabaseImpl db;
    ReparaturAuftragsServiceInterface repAuftragsService;

    public void setUp() throws Exception {

        databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", DB_URL, USER, PASSWORD);
        databaseTester
                .setDataSet(new FlatXmlDataSetBuilder().build(new File("db_full_export.xml")));
        databaseTester.onSetup();
        db = new DatabaseImpl();
        repAuftragsService = new ReparaturAuftragsServiceImpl(db, db);

    }

    @Test
    public void testGetAllAuftraege() throws Exception {


        Set<ReparaturAuftrag> alleAuftraege = repAuftragsService.getAllAuftraege();
        assertEquals(5, alleAuftraege.size());

        Set<String> auftragsNummern = new HashSet<String>(Arrays.asList("20160112001",
                "20160112002", "20160112003", "20160113001", "20160113001", "20160113002"));

        for (ReparaturAuftrag auftrag : alleAuftraege) {
            if (auftragsNummern.contains(auftrag.getAuftragsNr())) {
                auftragsNummern.remove(auftrag.getAuftragsNr());
            } else {
                fail();
            }

            if (auftrag.getAuftragsNr().equals("20160112001")
                    || auftrag.getAuftragsNr().equals("20160112002")) {
                assertFalse(auftrag.istOffen());
                assertTrue(auftrag.istGeschlossen());
            } else {
                assertTrue(auftrag.istOffen());
                assertFalse(auftrag.istGeschlossen());
            }
        }

        assertEquals(0, auftragsNummern.size());
    }

    @Test
    public void testAbrechnen() throws Exception {
//        // Initialisierung von DBUnit Anfang
//        databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", DB_URL, USER, PASSWORD);
//        databaseTester
//                .setDataSet(new FlatXmlDataSetBuilder().build(new File("db_full_export.xml")));
//        databaseTester.onSetup();
//        db = new DatabaseImpl();
//        repAuftragsService = new ReparaturAuftragsServiceImpl(db, db);
//        // Initialisierung von DBUnit Ende

        ReparaturAuftrag auftrag = repAuftragsService.getAuftragByNr("20160112003");
        assertFalse(auftrag.istGeschlossen());
        assertTrue(auftrag.istOffen());
        try {
            repAuftragsService.abrechnen(auftrag);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        assertTrue(auftrag.istGeschlossen());
        assertFalse(auftrag.istOffen());
    }

    @Test
    public void testAbrechnungStornieren() throws Exception {
//        // Initialisierung von DBUnit Anfang
//        databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", DB_URL, USER, PASSWORD);
//        databaseTester
//                .setDataSet(new FlatXmlDataSetBuilder().build(new File("db_full_export.xml")));
//        databaseTester.onSetup();
//        db = new DatabaseImpl();
//        repAuftragsService = new ReparaturAuftragsServiceImpl(db, db);
//        // Initialisierung von DBUnit Ende

        ReparaturAuftrag auftrag = repAuftragsService.getAuftragByNr("20160112001");
        assertTrue(auftrag.istGeschlossen());
        assertFalse(auftrag.istOffen());
        try {
            repAuftragsService.abrechnungStornieren(auftrag);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
        assertFalse(auftrag.istGeschlossen());
        assertTrue(auftrag.istOffen());
    }

    @Test
    public void testStorePersistent() throws Exception {
//        // Initialisierung von DBUnit Anfang
//        databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", DB_URL, USER, PASSWORD);
//        databaseTester
//                .setDataSet(new FlatXmlDataSetBuilder().build(new File("db_full_export.xml")));
//        databaseTester.onSetup();
//        db = new DatabaseImpl();
//        repAuftragsService = new ReparaturAuftragsServiceImpl(db, db);
//        // Initialisierung von DBUnit Ende

        ReparaturAuftrag auftrag = repAuftragsService.getAuftragByNr("20160113001");
        auftrag.setDetails("Geänderte Details");
        assertEquals(1, auftrag.getReparaturLeistungen().size());
        auftrag.addReparaturLeistung(
                repAuftragsService.getAllReparaturLeistungen().iterator().next());

        try {
            repAuftragsService.storePersistent(auftrag);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        auftrag = repAuftragsService.getAuftragByNr("20160113001");
        assertTrue(auftrag.getDetails().equals("Geänderte Details"));
        assertTrue(auftrag.istOffen());
        assertFalse(auftrag.istGeschlossen());
        assertEquals(2, auftrag.getReparaturLeistungen().size());
    }

}
