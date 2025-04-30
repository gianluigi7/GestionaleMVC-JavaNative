package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Dipendente extends Persona{

	private double stipendio;
	private String email;
	private LocalDate dataAssunzione;
	
	public void setStipendio(double stipendio) {
		
		this.stipendio= stipendio;
	}
	
	public double getStipendio() {
		
		return stipendio;
		
	}
	
    public void setEmail(String email) {
		
		this.email= email;
	}
    
    public String getEmail() {
    	
    	return email;
    }
    
    public void setDataAssunzione(LocalDate dataAssunzione) {
    	
    	this.dataAssunzione=dataAssunzione;
    }
    
    public LocalDate getDataAssunzione() {
    	return dataAssunzione;
    }
    
   

    @Override
    public String toString() {
        return  super.toString() +
        		", Stipendio: " + stipendio +
               ", Email: " + (email != null ? email : "N/A") +
               ", Data assunzione: " + (dataAssunzione != null ? dataAssunzione.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A");
    }
}
