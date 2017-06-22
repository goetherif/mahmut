package de.hrw.wi.business;

/**
 * 
 * @author andriesc
 *
 */
public class AuftragGeschlossen implements AuftragsStatus {
    private ReparaturAuftrag auftrag = null;

    public AuftragGeschlossen(ReparaturAuftrag auftrag) {
        this.auftrag = auftrag;
    }

    @Override
    public void addReparaturLeistung(String leistung) {
        throw new RuntimeException("Kann keine Leistung in geschlossenem Auftrag ergänzen");
    }

    @Override
    public void schliessen() {
        return;
    }

    @Override
    public void oeffnen() {
        auftrag.setCurrentStatus(new AuftragOffen(auftrag));
    }

    @Override
    public boolean istOffen() {
        return false;
    }

    @Override
    public boolean istGeschlossen() {
        return true;
    }

}
