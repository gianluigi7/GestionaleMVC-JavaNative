package Model;

public class Manager extends Dipendente{

	String ruolo;
	
	
	public void setRuolo(String ruolo) {
		
		this.ruolo=ruolo;
		
	}
	
	public String getRuolo() {
		return ruolo;
	}
	
	
	@Override
	public String toString() {
	    return super.toString() +
	    		", Ruolo: " + (ruolo != null ? ruolo : "N/A");
	}
}
