package de.hrw.wi.persistence;

import de.hrw.wi.persistence.dto.AuftragDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Datenbankimplementierung.
 * 
 * @author andriesc
 *
 */
public class DatabaseImpl implements DatabaseReadInterface, DatabaseWriteInterface {
  private static final String dbURL = "jdbc:hsqldb:file:"
      + "../repservice-db-layer/database/repservice_db;shutdown=true;hsqldb.write_delay=false;";
  private static final String user = "sa";
  private static final String password = "";

  private ResultSet executeQuery(String sql) throws SQLException {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(dbURL, user, password);
      ResultSet rs = conn.createStatement().executeQuery(sql);
      conn.commit();
      return rs;
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private int executeUpdate(String sql) throws SQLException {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(dbURL, user, password);
      int result = conn.createStatement().executeUpdate(sql);
      conn.commit();
      return result;
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private List<String> getResultAsString(String sql) {
    List<String> list = new ArrayList<String>();
    try {
      ResultSet result = executeQuery(sql);
      while (result.next()) {
        list.add(result.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  private int getInt(String sql) {
    try {
      ResultSet result = executeQuery(sql);
      if (result.next()) {
        return result.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  private String getString(String sql) {
    try {
      ResultSet result = executeQuery(sql);
      if (result.next()) {
        return result.getString(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Hole alle Reparaturleistungen für eine Auftragsnummer.
   * 
   * @param auftragsNr
   *          Die betreffende Auftragsnummer
   * @return Alle Reparaturleistungen
   */
  public Set<String> getReparaturLeistungenForAuftrag(String auftragsNr) {
    List<String> leistungsNrs = getResultAsString(
        "SELECT number FROM ERBRACHTE_LEISTUNGEN " + "WHERE auftrag='" + auftragsNr + "'");
    Set<String> result = new HashSet<String>();

    for (String nr : leistungsNrs) {
      String leistung = getString("SELECT name FROM LEISTUNGEN WHERE number='" + nr + "'");
      result.add(leistung);
    }

    return result;
  }

  @Override
  public Set<AuftragDto> getAllAuftraege() {
    Set<AuftragDto> result = new HashSet<AuftragDto>();
    ResultSet rs = null;
    try {
      rs = executeQuery("SELECT auftragsNr, details, serienNr, status FROM AUFTRAEGE");
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    try {
      while (rs.next()) {
        AuftragDto dto = new AuftragDto();
        dto.setAuftragsNr(rs.getString(1));
        dto.setDetails(rs.getString(2));
        dto.setSerienNr(rs.getString(3));
        int status = rs.getInt(4);
        if (status == 0) {
          dto.setGeschlossen(false);
        } else {
          dto.setGeschlossen(true);
        }
        dto.setReparaturLeistungen(getReparaturLeistungenForAuftrag(dto.getAuftragsNr()));

        result.add(dto);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return result;
  }

  @Override
  public String getLastAuftragsNummer() {
    String lastNumber = getString("SELECT MAX(auftragsNr) FROM AUFTRAEGE");
    return lastNumber;
  }

  @Override
  public AuftragDto getAuftragByNummer(String auftragsNr) {
    if (isAuftragVorhanden(auftragsNr)) {
      AuftragDto dto = new AuftragDto();
      dto.setAuftragsNr(auftragsNr);
      dto.setDetails(
          getString("SELECT details FROM AUFTRAEGE WHERE auftragsNr='" + auftragsNr + "'"));
      dto.setSerienNr(
          getString("SELECT serienNr FROM AUFTRAEGE WHERE auftragsNr='" + auftragsNr + "'"));
      int status = getInt("SELECT status FROM AUFTRAEGE WHERE auftragsNr='" + auftragsNr + "'");
      if (status == 0) {
        dto.setGeschlossen(false);
      } else {
        dto.setGeschlossen(true);
      }

      dto.setReparaturLeistungen(getReparaturLeistungenForAuftrag(auftragsNr));

      return dto;
    } else {
      return null;
    }
  }

  @Override
  public void upsertAuftrag(AuftragDto auftrag) throws SQLException {
    if (isAuftragVorhanden(auftrag.getAuftragsNr())) {
      // Schon vorhanden, aktualisieren (UPdate)
      // Auftrag selbst
      if (auftrag.isGeschlossen()) {
        executeUpdate("UPDATE AUFTRAEGE SET serienNr='" + auftrag.getSerienNr() + "', details='"
            + auftrag.getDetails() + "', status=1 WHERE auftragsNr='" + auftrag.getAuftragsNr()
            + "';");
      } else {
        executeUpdate("UPDATE AUFTRAEGE SET serienNr='" + auftrag.getSerienNr() + "', details='"
            + auftrag.getDetails() + "', status=0 WHERE auftragsNr='" + auftrag.getAuftragsNr()
            + "';");
      }
      // Nun alle Reparaturleistungen löschen
      executeUpdate(
          "DELETE FROM ERBRACHTE_LEISTUNGEN WHERE auftrag='" + auftrag.getAuftragsNr() + "';");

    } else {
      // Neu anlegen (inSERT)
      if (auftrag.isGeschlossen()) {
        executeUpdate("INSERT INTO AUFTRAEGE (auftragsNr, serienNr, details, status) VALUES ('"
            + auftrag.getAuftragsNr() + "', '" + auftrag.getSerienNr() + "', '"
            + auftrag.getDetails() + "', 1);");
      } else {
        executeUpdate("INSERT INTO AUFTRAEGE (auftragsNr, serienNr, details, status) VALUES ('"
            + auftrag.getAuftragsNr() + "', '" + auftrag.getSerienNr() + "', '"
            + auftrag.getDetails() + "', 1);");
      }

    }
    // Für beide Fälle Reparaturleistungen speichern
    for (String leistung : auftrag.getReparaturLeistungen()) {
      int number = getInt("SELECT number FROM LEISTUNGEN where name ='" + leistung + "';");
      executeUpdate("INSERT INTO ERBRACHTE_LEISTUNGEN (auftrag, number) VALUES ('"
          + auftrag.getAuftragsNr() + "', " + number + ");");
    }
  }

  @Override
  public boolean isAuftragVorhanden(String auftragsNr) {
    String sql = "SELECT auftragsNr FROM AUFTRAEGE WHERE auftragsNr='" + auftragsNr + "'";
    return (getString(sql) != null);
  }

  @Override
  public Set<String> getAllReparaturLeistungen() {
    Set<String> result = new HashSet<String>(getResultAsString("SELECT name FROM LEISTUNGEN;"));
    return result;
  }
}
