package de.hrw.wi.service;

import de.hrw.wi.business.ReparaturAuftrag;
import de.hrw.wi.persistence.DatabaseReadInterface;
import de.hrw.wi.persistence.DatabaseWriteInterface;
import de.hrw.wi.persistence.dto.AuftragDto;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author andriesc
 *
 */
public class ReparaturAuftragsServiceImpl implements ReparaturAuftragsServiceInterface {

    DatabaseReadInterface dbRead = null;
    DatabaseWriteInterface dbWrite = null;

    /**
     * Konstruktor
     * 
     * @param dbRead
     *            Lesende Datenbankschnittstelle
     * @param dbWrite
     *            Schreibende Datenbankschnittstelle
     */
    public ReparaturAuftragsServiceImpl(DatabaseReadInterface dbRead,
            DatabaseWriteInterface dbWrite) {
        this.dbRead = dbRead;
        this.dbWrite = dbWrite;
    }

    @Override
    public Set<ReparaturAuftrag> getAllAuftraege() {
        Set<AuftragDto> auftragDtos = dbRead.getAllAuftraege();
        Set<ReparaturAuftrag> alleAuftraege = new HashSet<ReparaturAuftrag>();

        for (AuftragDto dto : auftragDtos) {
            alleAuftraege.add(new ReparaturAuftrag(dto));
        }

        return alleAuftraege;
    }

    @Override
    public ReparaturAuftrag getAuftragByNr(String nr) {
        AuftragDto dto = dbRead.getAuftragByNummer(nr);
        if (dto != null) {
            return new ReparaturAuftrag(dto);
        } else {
            return null;
        }
    }

    @Override
    public ReparaturAuftrag createNewAuftrag() {
        // Letzte Auftragsnummer holen
        String lastNumberStr = dbRead.getLastAuftragsNummer();

        // Umwandeln und erhöhen
        String nr = lastNumberStr.substring(lastNumberStr.length() - 3, lastNumberStr.length());
        lastNumberStr = lastNumberStr.substring(0, lastNumberStr.length() - 3);
        int lastNumber = Integer.valueOf(nr);
        lastNumber++;
        String newNumberStr = lastNumberStr + String.format("%03d", lastNumber);

        // Auftrag mit neuer Auftragsnummer erzeugen
        ReparaturAuftrag auftrag = new ReparaturAuftrag(newNumberStr);
        return auftrag;
    }

    private void rechnungErstellen(ReparaturAuftrag auftrag) {
        // Bei zukünftigem Ausbau des Systems noch zu implementieren
    }

    private void rechnungStornieren(ReparaturAuftrag auftrag) {
        // Bei zukünftigem Ausbau des Systems noch zu implementieren
    }

    @Override
    public void abrechnen(ReparaturAuftrag auftrag) throws SQLException {
        auftrag.schliessen();
        rechnungErstellen(auftrag);
    }

    @Override
    public void abrechnungStornieren(ReparaturAuftrag auftrag) throws SQLException {
        rechnungStornieren(auftrag);
        auftrag.oeffnen();
    }

    @Override
    public void storePersistent(ReparaturAuftrag auftrag) throws SQLException {
        AuftragDto dto = new AuftragDto();
        dto.setAuftragsNr(auftrag.getAuftragsNr());
        dto.setDetails(auftrag.getDetails());
        dto.setGeschlossen(auftrag.istGeschlossen());
        dto.setSerienNr(auftrag.getSerienNr());
        dto.setReparaturLeistungen(auftrag.getReparaturLeistungen());

        dbWrite.upsertAuftrag(dto);
    }

    @Override
    public Set<String> getAllReparaturLeistungen() {
        return dbRead.getAllReparaturLeistungen();
    }

}
