package Model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Persona {

	private String nome;
	private String cognome;
	private String codiceFiscale;
	//private int eta;
	private LocalDate dataNascita;
	private int id;
	
	
	public void setNome(String nome)
	{
		this.nome=nome;
		
	}
	
	
	public String getNome()
	{
		return nome;
	}


	public String getCognome() {
		return cognome;
	}
    

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }
    
	public String getCodiceFiscale() {
		
		return codiceFiscale;
	}
	
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	
	
	
	
	@Override
	public String toString() {
		int eta = Period.between(dataNascita, LocalDate.now()).getYears();
		return  
				"nome=" + nome +
				", cognome=" + cognome +
				", data di nascita=" + dataNascita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))  +
	            ", codice fiscale=" + codiceFiscale +
		        ", eta utente=" + eta +
		        ", id=" + id;
	}
     

	public Persona()
	{
		
	}
	
	
	
	public Persona(String nome, String cognome, String codiceFiscale, LocalDate dataNascita, int eta)
	{
		this.nome=nome;
		this.cognome=cognome;
		this.dataNascita = dataNascita;
		this.codiceFiscale=codiceFiscale;
		
		
	}
	
}
/*
@Override
public int hashCode() {
	return Objects.hash(codiceFiscale, cognome, dataNascita, eta, nome);
}


@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Persona other = (Persona) obj;
	return Objects.equals(codiceFiscale, other.codiceFiscale) && Objects.equals(cognome, other.cognome)
			&& Objects.equals(dataNascita, other.dataNascita) && eta == other.eta
			&& Objects.equals(nome, other.nome);
}

*/
