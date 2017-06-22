package de.hrw.wi.service;

import de.hrw.wi.business.ReparaturAuftrag;

import java.sql.SQLException;
import java.util.Set;

/**
 * Schnittstelle für den ReparaturAuftragsService.
 * 
 * <p>
 * Der Service vergibt für Aufträge die Auftragsnummer.
 * </p>
 * 
 * @author andriesc
 *
 */
public interface ReparaturAuftragsServiceInterface {

    /**
     * 
     * @return Eine Menge mit allen Aufträgen in der Datenbank.
     */
    public Set<ReparaturAuftrag> getAllAuftraege();

    /**
     * Hole einen persistent in der Datenbank gespeicherten Auftrag über seine Nummer.
     * 
     * 
     *            Die Nummer eines Auftrags
     * @return Der Auftrag mit der angegebenen Nummer oder null falls kein solcher Auftrag existiert
     */
    ReparaturAuftrag getAuftragByNr(String nr);

    /**
     * Erzeuge einen neuen, leeren Auftrag und vergebe dabei die Auftragsnummer.
     * 
     * @return Ein neuer, leerer Auftrag mit einer eindeutigen Auftragsnummer.
     */
    ReparaturAuftrag createNewAuftrag();

    /**
     * Rechnet einen Auftrag ab und schließt den Auftrag dabei.
     * 
     * @param auftrag
     *            Der abzurechnende Auftrag.
     * @throws SQLException
     *             Bei Datenbankfehlern
     * 
     */
    void abrechnen(ReparaturAuftrag auftrag) throws SQLException;

    /**
     * Storniert die Abrechnung eines abgerechneten Auftrags und öffnet diesen dazu wieder.
     * 
     * @param auftrag
     *            Der Auftrag, dessen Abrechnung zu stornieren ist.
     * @throws SQLException
     *             Bei Datenbankfehlern
     * 
     */
    void abrechnungStornieren(ReparaturAuftrag auftrag) throws SQLException;

    /**
     * Speichert den Auftrag persistent in der Datenbank.
     * 
     * @param auftrag
     *            Der zu speichernde Auftrag.
     * @throws SQLException
     *             Bei Datenbankfehlern
     */
    void storePersistent(ReparaturAuftrag auftrag) throws SQLException;

    /**
     * Gibt alle möglichen Reparaturleistungen zurück.
     * 
     * @return Alle möglichen Reparaturleistungen, die in der Datenbank gespeichert sind.
     */
    Set<String> getAllReparaturLeistungen();
}
