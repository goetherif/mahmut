package de.hrw.wi.persistence.dto;

import java.util.HashSet;
import java.util.Set;

public class AuftragDto {
  private String auftragsNr = "";
  private String serienNr = "";
  private String details = "";
  private boolean geschlossen = false;
  private Set<String> reparaturLeistung = new HashSet<String>();

  public Set<String> getReparaturLeistungen() {
    return reparaturLeistung;
  }

  public void setReparaturLeistungen(Set<String> reparaturLeistung) {
    this.reparaturLeistung = reparaturLeistung;
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

  public boolean isGeschlossen() {
    return geschlossen;
  }

  public void setGeschlossen(boolean status) {
    this.geschlossen = status;
  }

}
