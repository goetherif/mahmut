package de.hrw.wi.business;

import de.hrw.wi.persistence.dto.AuftragDto;

import java.util.HashSet;
import java.util.Set;

/**
 * Die Klasse repräsentiert einen einzelnen Reparaturauftrag.
 * 
 * @author andriesc
 *
 */
public class ReparaturAuftrag {
    private String auftragsNr = "";
    private String serienNr = "";
    private String details = "";
    private AuftragsStatus status = null;
    private Set<String> reparaturLeistungen = null;

    /**
     * Erzeugt einen ReparaturAuftrag aus einem DTO.
     * 
     * @param dto
     *            Das AuftragDto aus der Datenbank
     */
    public ReparaturAuftrag(AuftragDto dto) {
        auftragsNr = dto.getAuftragsNr();
        serienNr = dto.getSerienNr();
        details = dto.getDetails();
        reparaturLeistungen = dto.getReparaturLeistungen();
        if (dto.isGeschlossen()) {
            setCurrentStatus(new AuftragGeschlossen(this));
        } else {
            setCurrentStatus(new AuftragOffen(this));
        }
    }

    /**
     * Erzeugt einen neuen, leeren ReparaturAuftrag mit einer Auftragsnummer.
     * 
     * @param auftragsNr
     *            Die Auftragsnummer für den neuen Auftrag.
     */
    public ReparaturAuftrag(String auftragsNr) {
        this.auftragsNr = auftragsNr;
        reparaturLeistungen = new HashSet<String>();
        setCurrentStatus(new AuftragOffen(this));
    }

    public void addReparaturLeistung(String leistung) {
        status.addReparaturLeistung(leistung);
    }

    public Set<String> getReparaturLeistungen() {
        return reparaturLeistungen;
    }

    public void setReparaturLeistungen(Set<String> reparaturLeistungen) {
        this.reparaturLeistungen = reparaturLeistungen;
    }

    public void schliessen() {
        status.schliessen();
    }

    public void oeffnen() {
        status.oeffnen();
    }

    public String getAuftragsNr() {
        return auftragsNr;
    }

    public void setAuftragsNr(String auftragsNr) {
        this.auftragsNr = auftragsNr;
    }

    public String getSerienNr() {
        return serienNr;
    }

    public void setSerienNr(String serienNr) {
        this.serienNr = serienNr;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCurrentStatus(AuftragsStatus newStatus) {
        this.status = newStatus;
    }

    public boolean istOffen() {
        return this.status.istOffen();
    }

    public boolean istGeschlossen() {
        return this.status.istGeschlossen();
    }
}
