package de.hrw.wi.business;

/**
 * 
 * @author andriesc
 *
 */
public interface AuftragsStatus {
    void addReparaturLeistung(String leistung);

    void schliessen();

    void oeffnen();

    boolean istOffen();

    boolean istGeschlossen();
}
