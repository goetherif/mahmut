package de.hrw.wi.persistence;

import de.hrw.wi.persistence.dto.AuftragDto;

import java.util.Set;

/**
 * Lesendes Interface für Datenbank.
 * 
 * @author andriesc
 *
 */
public interface DatabaseReadInterface {

  /**
   * Gibt alle Aufträge aus der Datenbank zurück.
   * 
   * @return Menge aller Aufträge in der Datenbank
   */
  Set<AuftragDto> getAllAuftraege();

  /**
   * Gibt die zuletzt vergebene Auftragsnummer zurück, damit eine eindeutige nächste Auftragsnummer
   * erzeugt werden kann.
   * 
   * @return Die zuletzt vergebene Auftragsnummer
   */
  String getLastAuftragsNummer();

  /**
   * Sucht einen Auftrag über Nummer aus der Datenbank.
   * 
   * @return Der gefundene Auftrag.
   */
  AuftragDto getAuftragByNummer(String auftragsNr);

  /**
   * Prüft anhand der Auftragsnummer, ob der Auftrag in der Datenbank gespeichert ist.
   * 
   * @param auftragsNr
   *          Die Nummer des Auftrags, den man prüfen will
   * @return true, falls der Auftrag vorhanden ist, sonst false
   */
  boolean isAuftragVorhanden(String auftragsNr);

  /**
   * Gibt eine Menge aller möglichen Reparaturleistungen zurück.
   * 
   * @return Alle in der Datenbank gespeicherten Reparaturleistungen.
   */
  Set<String> getAllReparaturLeistungen();
}
