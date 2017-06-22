package de.hrw.wi.business;

import java.util.Set;

/**
 * 
 * @author andriesc
 *
 */
public class AuftragOffen implements AuftragsStatus {
    private ReparaturAuftrag auftrag = null;

    public AuftragOffen(ReparaturAuftrag auftrag) {
        this.auftrag = auftrag;
    }

    @Override
    public void addReparaturLeistung(String leistung) {
        Set<String> reparaturLeistungen = auftrag.getReparaturLeistungen();
        reparaturLeistungen.add(leistung);
        auftrag.setReparaturLeistungen(reparaturLeistungen);
    }

    @Override
    public void schliessen() {
        auftrag.setCurrentStatus(new AuftragGeschlossen(auftrag));
    }

    @Override
    public void oeffnen() {
        return;
    }

    @Override
    public boolean istOffen() {
        return true;
    }

    @Override
    public boolean istGeschlossen() {
        return false;
    }

}
