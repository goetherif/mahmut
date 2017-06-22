package de.hrw.wi.databasesetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Initiales Aufsetzen der Datenbank mit Demodaten.
 * 
 * @author andriesc
 *
 */

public class InitialDatabaseSetup {

  /**
   * Main-Methode setzt Datenbank auf.
   * 
   * @param args
   *          Kommandozeilenparameter
   */
  public static void main(String[] args) throws SQLException {
    // Optional Parameter ";shutdown=true;hsqldb.write_delay=false;"
    Connection conn = DriverManager.getConnection(
        "jdbc:hsqldb:file:../repservice-db-layer/database/repservice_db"
            + ";shutdown=true;hsqldb.write_delay=false;", "sa", "");
    conn.setAutoCommit(false);
    System.out.println("Autocommit " + (conn.getAutoCommit() ? "on" : "off"));

    conn.createStatement().executeQuery("DROP TABLE ERBRACHTE_LEISTUNGEN IF EXISTS");
    conn.createStatement().executeQuery("DROP TABLE AUFTRAEGE IF EXISTS");
    conn.createStatement().executeQuery("DROP TABLE LEISTUNGEN IF EXISTS");

    conn.createStatement().executeQuery(
        "CREATE TABLE AUFTRAEGE (auftragsNr varchar(11) PRIMARY KEY, serienNr varchar(10),"
            + " details varchar(255), status INTEGER)");

    conn.createStatement().executeQuery(
        "CREATE TABLE LEISTUNGEN (number INTEGER PRIMARY KEY, name varchar(255))");

    conn.createStatement().executeQuery(
        "CREATE TABLE ERBRACHTE_LEISTUNGEN (auftrag varchar(11), number INTEGER, "
            + " constraint PK_STOCK PRIMARY KEY (auftrag, number),"
            + " constraint FK_LEISTUNGEN FOREIGN KEY (number) REFERENCES LEISTUNGEN(number),"
            + " constraint FK_AUFTRAEGE FOREIGN KEY (auftrag) REFERENCES AUFTRAEGE(auftragsNr))");

    conn.createStatement().executeQuery("INSERT INTO LEISTUNGEN VALUES (1,'Diagnose')");
    conn.createStatement().executeQuery(
        "INSERT INTO LEISTUNGEN VALUES (2,'Oberflächliche Prüfung')");
    conn.createStatement().executeQuery("INSERT INTO LEISTUNGEN VALUES (3,'Gründliche Prüfung')");
    conn.createStatement().executeQuery("INSERT INTO LEISTUNGEN VALUES (4,'Austausch Hauptmodul')");
    conn.createStatement().executeQuery(
        "INSERT INTO LEISTUNGEN VALUES (5,'Austausch Nebenmodul 1')");
    conn.createStatement().executeQuery(
        "INSERT INTO LEISTUNGEN VALUES (6,'Austausch Nebenmodul 2')");
    conn.createStatement().executeQuery(
        "INSERT INTO LEISTUNGEN VALUES (7,'Austausch Nebenmodul 3')");
    conn.createStatement().executeQuery("INSERT INTO LEISTUNGEN VALUES (8,'Austausch Gehäuse')");
    conn.createStatement()
        .executeQuery("INSERT INTO LEISTUNGEN VALUES (9,'Austausch Verkabelung')");

    conn.createStatement().executeQuery(
        "INSERT INTO AUFTRAEGE VALUES ('20160112001','0000000001','Gerät ohne Funktion', 1)");
    conn.createStatement().executeQuery(
        "INSERT INTO AUFTRAEGE VALUES ('20160112002','0000000002','Gerät produziert Dämpfe', 1)");
    conn.createStatement().executeQuery(
        "INSERT INTO AUFTRAEGE VALUES ('20160112003','0000000103',"
            + "'Gerät stürzt nach 15min Benutzung ab', 0)");
    conn.createStatement().executeQuery(
        "INSERT INTO AUFTRAEGE VALUES ('20160113001','1000000022',"
            + "'Nebenfunktion führt zu Störungen', 0)");
    conn.createStatement().executeQuery(
        "INSERT INTO AUFTRAEGE VALUES ('20160113002','2000003589','Ein-/Ausschalter defekt', 0)");

    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160112001, 1)");
    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160112001, 3)");
    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160112001, 4)");
    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160112001, 6)");
    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160112002, 1)");
    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160112002, 9)");
    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160112002, 2)");
    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160112003, 1)");
    conn.createStatement()
        .executeQuery("INSERT INTO ERBRACHTE_LEISTUNGEN VALUES (20160113001, 1)");

    // c.createStatement().executeQuery("SHUTDOWN");

    conn.commit();

    conn.close();

    System.out.println("ready");
  }

}
