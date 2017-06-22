package de.hrw.wi.persistence;

import de.hrw.wi.persistence.dto.AuftragDto;

import java.sql.SQLException;

/**
 * Schreibendes Interface für die Datenbank.
 * 
 * @author andriesc
 *
 */
public interface DatabaseWriteInterface {

  /**
   * Speichert einen Auftrag in der Datenbank oder aktualisiert ihn, falls er schon vorhanden ist.
   * (Upsert == UPdate + inSERT)
   * 
   * 
   * @param auftrag
   *          DTO des zu speichernden Auftrags.
   */
  public void upsertAuftrag(AuftragDto auftrag) throws SQLException;
}
