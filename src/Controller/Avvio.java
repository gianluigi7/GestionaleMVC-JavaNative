package Controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

import Exceptions.ArrayVuoto;
import Exceptions.DataAssunzione;
import Exceptions.InputNonValido;
import Exceptions.LunghezzaNome;
import Exceptions.MaggioreEta;
import Exceptions.PersonaGiàDirigente;
import Exceptions.RuoloNonValido;
import Exceptions.StipendioNonValido;
import Exceptions.StringaNonValida;
import Model.Dipendente;
import Model.Dirigente;
import Model.Manager;
import Model.Persona;
import Model.PersonaCrud;
import View.View;




public class Avvio {

	private static	View view=new View();  //lo static serve per far leggere ad entrambe le funzioni 
	private static	PersonaCrud crud=PersonaCrud.getInstance();
	
	public static void main(String[] args) {
		boolean flag=true;
		Persona persona; 
    //View view=new View();
	//PersonaCrud crud=new PersonaCrud();	
		
		
		do
		{
		
		switch (view.menu()) 
		{
		case 1:
		    
			
		//	Persona persona;

			

			int tipo = view.menuTipoPersona();
			if (tipo == 0) break;

			switch (tipo) {
			    case 2:
			        persona = new Dipendente();
			        break;
			    case 3:
			        persona = new Manager();
			        break;
			    case 4:
			        persona = new Dirigente();
			        break;
			    case 1:
			    default:
			        persona = new Persona();
			        break;
			}
			try {
				view.formInserimento(persona);
				
				
				if (crud.ricercaCF(persona.getCodiceFiscale()) != null) {
                    view.stampa("Codice fiscale già presente.");
                } else {
                    // Se il codice fiscale non esiste, inserisci la persona
                    crud.insert(persona);
                    view.stampaPersona(persona);
                    }
                }
	
			
						
			
			
			catch(DateTimeParseException e) {	
				view.stampa("Formato data non valido");
			}
			
		    catch (LunghezzaNome | MaggioreEta | DataAssunzione | StipendioNonValido | RuoloNonValido  e) {
		  	
		    view.stampa(e.getMessage());
		    
	       	}
			
			break;
	
		case 2:
            // Cerca persona
            try {
            	crud.getHashMap();
                Persona personaTrovata = cercaPersona();
                
                    view.stampaPersona(personaTrovata);
                 if (personaTrovata == null ){
                	 throw new StringaNonValida("input non valido");
                }
            } catch (ArrayVuoto | StringaNonValida e) {
                view.stampa(e.getMessage());
            }
            break;	
            
		case 3:
            // Elimina persona
            try {
            	crud.getHashMap();
                Persona personaTrovata = cercaPersona();
                view.stampaPersona(personaTrovata);
                if (personaTrovata == null) 
                	throw new StringaNonValida("input non valido");
                
                if (view.confermaSi("Sei sicuro di voler eliminare questa persona? digitare si")) {
                        crud.rimuoviPersona(personaTrovata.getId());
                        view.stampa("Persona eliminata con successo.");
                    }
                else {
                        view.stampa("Eliminazione annullata.");
                    }
                } 
             catch (ArrayVuoto | StringaNonValida e) {
                view.stampa(e.getMessage());
            }
            break;
		case 4:
		    try {
		    	crud.getHashMap();
		        Persona personaDaModificare = cercaPersona();
		 //       if (personaDaModificare != null) {
		            view.stampa("Dati attuali:");
		            view.stampaPersona(personaDaModificare);
		            if (personaDaModificare == null) 
	                	throw new StringaNonValida("input non valido");
		            if (view.confermaSi("Vuoi modificare questa persona? digitare si")) {
		                view.formModifica(personaDaModificare);
		                view.stampa("Modifica completata con successo.");
		                view.stampaPersona(personaDaModificare);
		            } else {
		                view.stampa("Modifica annullata.");
		            }
		  //      } else {
		  //          view.stampa("Persona non trovata.");
		  //      }
		    } catch (ArrayVuoto | LunghezzaNome | DateTimeParseException | MaggioreEta | DataAssunzione | StipendioNonValido | StringaNonValida e) {
		        view.stampa("Errore: " + e.getMessage());
		    }
		    break;
		case 5:
			
			try {
		//		System.out.println("Contenuto della mappa: " + crud.getHashMap());
				view.stampaPersona(PersonaCrud.getInstance().getHashMap());
			} catch (ArrayVuoto e) {
				view.stampa(e.getMessage());
			}
			
			break;
		case 6:
			
				
			
		    try {
		    	crud.getHashMap();
		     	Persona personaTrovata = cercaPersona();
		     	if (personaTrovata == null) 
		     		throw new StringaNonValida("input non valido");
		     	LocalDate oggi = LocalDate.now();
		        int eta = Period.between(personaTrovata.getDataNascita(), oggi).getYears();
		        if (eta < 18) {
		            throw new MaggioreEta("La persona ha meno di 18 anni e non può essere promossa.");
		        }
		        
		        String ruoloPersona=null;
	            switch (personaTrovata.getClass().getSimpleName()) {
	                case "Persona":
	                    ruoloPersona = view.leggiString("Può essere promossa a Dipendente, Manager o Dirigente. A quale ruolo vuoi promuoverla?");
	                    
	                    break;
	                case "Dipendente":
	                	 ruoloPersona = view.leggiString("Può essere promossa a Manager o Dirigente. A quale ruolo vuoi promuoverla?");
	                    break;
	                case "Manager":
	                	 ruoloPersona = view.leggiString("Vuoi promuovere a Dirigente? digitare si ");
	                    break;
	                case "Dirigente":
	                    throw new PersonaGiàDirigente("La persona è già un Dirigente e non può essere promossa ulteriormente.");
	                default:
	                    System.out.println("Tipo di persona non valido.");
	                    break;
	            }
	            
	            
	            
	            
	            if (!ruoloPersona.equalsIgnoreCase("manager") && !ruoloPersona.equalsIgnoreCase("si") && !ruoloPersona.equalsIgnoreCase("dirigente") && !ruoloPersona.equalsIgnoreCase("dipendente")) {
	            	throw new InputNonValido("input non valido");
	            	
	            }
	            
	            		view.promuoviPersona(personaTrovata, ruoloPersona);
	            		view.stampaPersona(crud.getHashMap());
		    }
	         catch (PersonaGiàDirigente | ArrayVuoto | DataAssunzione | StipendioNonValido | DateTimeParseException | LunghezzaNome | RuoloNonValido | MaggioreEta | StringaNonValida | InputNonValido e) {
	            System.out.println(e.getMessage());
	        }
		    break;
		case 0:
			
			flag=false;			
			break;
			
		default:
			view.stampa("scelta non valida");
			break;
		}
		
		}while(flag);
		
		
		
	}	
	private static Persona cercaPersona() {
	//    try {
	        String ricercaTipo = view.leggiString("Vuoi cercare per CF (Codice Fiscale) o per ID?").toUpperCase();
	 //       crud.getHashMap();
	        switch (ricercaTipo) {
	            case "CF":
	                String cfRicerca = view.leggiString("Inserisci il Codice Fiscale da cercare:");
	                return crud.ricercaCF(cfRicerca);
	            case "ID":
	                int idRicerca = view.leggiInt("Inserisci l'ID da cercare:");
	                return crud.ricercaId(idRicerca);
	            default:
	                view.stampa("Opzione non valida. Inserisci 'CF' per codice fiscale o 'ID' per ID.");
	                return null;
	        }
	//    } catch (ArrayVuoto e) {
	 //       view.stampa(e.getMessage());
	//        return null;
	//    }
	}
}

		   
