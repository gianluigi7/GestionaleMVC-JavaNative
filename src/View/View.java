package View;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.Scanner;

import Exceptions.ArrayVuoto;
import Exceptions.DataAssunzione;
import Exceptions.LunghezzaNome;
import Exceptions.MaggioreEta;
import Exceptions.RuoloNonValido;
import Exceptions.StipendioNonValido;
import Model.Dipendente;
import Model.Dirigente;
import Model.Manager;
import Model.Persona;
import Model.PersonaCrud;

public class View {

	private Scanner input = new Scanner(System.in);
	private static final String[] ruoli = { "pm", "team leader", "top manager" };
	private static final String[] aree = { "amministrativa", "business", "it operation" };
//	private static	PersonaCrud crud=new PersonaCrud();
	
	public void formInserimento(Persona p)
			throws LunghezzaNome, MaggioreEta, DataAssunzione, StipendioNonValido, RuoloNonValido {

		String nome = leggiString("Nome: ");
		validaCampo(nome, "Nome");
		p.setNome(nome);

		String cognome = leggiString("Cognome: ");
		validaCampo(cognome, "Cognome");
		p.setCognome(cognome);

		LocalDate dataNascita = parseData(leggiDataNascita("Data di nascita (formato: dd/MM/yyyy): "));
		int eta = calcolaEta(dataNascita);

		if (p instanceof Dipendente && eta < 18) {
			throw new MaggioreEta("Un dipendente deve avere almeno 18 anni.");
		} else if (!(p instanceof Dipendente) && eta < 16) {
			throw new MaggioreEta("Una persona deve avere almeno 16 anni.");
		}

		p.setDataNascita(dataNascita);
	//	p.setEta(calcolaEta(dataNascita));

		String codiceFiscale = leggiString("Codice Fiscale: ");
		validaCampo(codiceFiscale, "Codice Fiscale");
		p.setCodiceFiscale(codiceFiscale);

		if (p instanceof Dirigente) {
			// try {
			Dirigente d = (Dirigente) p;
			campiDirigente(d); // Leggi area di competenza per Dirigente
			campiManager(d); // Leggi ruolo per Dirigente
			campiDpendente(d);
			// } catch (RuoloNonValido e) {
			// stampa(e.getMessage()); // Mostra errore se area o ruolo non sono validi
			// }
		} else if (p instanceof Manager) {
			// try {
			Manager m = (Manager) p;
			// m.setRuolo(leggiRuoloManager());
			campiManager(m);// Leggi ruolo per Manager
			campiDpendente(m);
			// } catch (RuoloNonValido e) {
			// stampa(e.getMessage()); // Mostra errore se ruolo non valido
			// }
		} else if (p instanceof Dipendente) {
			Dipendente d = (Dipendente) p;
			campiDpendente(d); // Solo per Dipendente, non chiedere ruolo o area
		}

	}

	private void campiDpendente(Dipendente p) throws DataAssunzione, StipendioNonValido, LunghezzaNome {
		String email = leggiString("Email: ");
		validaCampo(email, "Email");
		p.setEmail(email);
		p.setStipendio(stipendio(p));
		//stipendio(p);
		

		String dataStr = leggiString("Data di assunzione (formato: dd/MM/yyyy): ");
	    LocalDate data;
	    try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        data = LocalDate.parse(dataStr, formatter);
	    } catch (DateTimeParseException e) {
	        throw new DataAssunzione("Formato data non valido. Usa il formato dd/MM/yyyy.");
	    }

	    LocalDate oggi = LocalDate.now();
	    if (data.isBefore(oggi)) {
	        throw new DataAssunzione("La data di assunzione non può essere passata.");
	    }

	    p.setDataAssunzione(data);
	}

	private void campiManager(Manager m) throws RuoloNonValido {

		m.setRuolo(leggiRuoloManager());

	}
	private double stipendio(Dipendente d) throws StipendioNonValido {
		
		double stipendio = Double.parseDouble(leggiString("Stipendio: "));
		double min = 1000, max = 1800;

		if (d instanceof Manager && !(d instanceof Dirigente)) {
			min = 1800;
			max = 2500;
		} else if (d instanceof Dirigente) {
			min = 2500;
			max = 5000;
		}

		if (stipendio < min || stipendio > max) {
			throw new StipendioNonValido("Lo stipendio deve essere tra " + min + " e " + max + " euro.");
		}
		
		return stipendio;
		
		
		
	}
	
	

	private void campiDirigente(Dirigente d) throws RuoloNonValido {

		d.setArea(leggiAreaDirigente());
	}

	public String leggiRuoloManager() throws RuoloNonValido {
		System.out.println("Ruoli disponibili:");
		for (String ruolo : ruoli) {
			System.out.println("- " + ruolo);
		}

		String ruoloInserito = leggiString("Inserisci il ruolo:");

		for (String ruolo : ruoli) {
			if (ruolo.equalsIgnoreCase(ruoloInserito)) {
				return ruolo.toLowerCase();
			}
		}

		throw new RuoloNonValido(
				"Ruolo non valido: " + ruoloInserito + ". Valori validi: pm, team leader, top manager.");
	}

	public String leggiAreaDirigente() throws RuoloNonValido {
		System.out.println("Aree disponibili:");
		for (String area : aree) {
			System.out.println("- " + area);
		}

		String areaInserita = leggiString("Inserisci l'area di competenza:");

		for (String area : aree) {
			if (area.equalsIgnoreCase(areaInserita)) {
				return area.toLowerCase();
			}
		}

		throw new RuoloNonValido(
				"Area non valida: " + areaInserita + ". Valori validi: amministrativa, business, IT operation.");
	}

	public void formModifica(Persona p) throws LunghezzaNome, MaggioreEta, DataAssunzione, StipendioNonValido {
		String nome = leggiString("Nome [" + p.getNome() + "]: ");
		if (!nome.isEmpty()) {
			validaCampo(nome, "Nome");
			p.setNome(nome);
		}

		String cognome = leggiString("Cognome [" + p.getCognome() + "]: ");
		if (!cognome.isEmpty()) {
			validaCampo(cognome, "Cognome");
			p.setCognome(cognome);
		}

		String dataNascitaStr = leggiString("Data di nascita (dd/MM/yyyy) ["
				+ p.getDataNascita().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "]: ");
		if (!dataNascitaStr.isEmpty()) {
			LocalDate dataNascita = parseData(dataNascitaStr);
			int eta = calcolaEta(dataNascita);
			if (p instanceof Dipendente && eta < 18) {
				throw new MaggioreEta("Un dipendente deve avere almeno 18 anni.");
			} else if (!(p instanceof Dipendente) && eta < 16) {
				throw new MaggioreEta("Una persona deve avere almeno 16 anni.");
			}
			p.setDataNascita(dataNascita);
		//	p.setEta(eta);
		}

		String codiceFiscale = leggiString("Codice Fiscale [" + p.getCodiceFiscale() + "]: ");
		if (!codiceFiscale.isEmpty()) {
			validaCampo(codiceFiscale, "Codice Fiscale");
			p.setCodiceFiscale(codiceFiscale);
		}

		if (p instanceof Dirigente) {
			try {
				Dirigente d = (Dirigente) p;
				String nuovaArea = leggiString("Area di competenza [" + d.getArea() + "]: ");
				if (!nuovaArea.isEmpty()) {
					d.setArea(leggiAreaDirigente()); // Leggi area di competenza per Dirigente
				}
				String nuovoRuolo = leggiString("Ruolo [" + d.getRuolo() + "]: ");
				if (!nuovoRuolo.isEmpty()) {
					d.setRuolo(leggiRuoloManager()); // Leggi ruolo per Dirigente
				}
				modificaCampiDipendente(d);
			} catch (RuoloNonValido e) {
				stampa(e.getMessage()); // Mostra errore se area o ruolo non sono validi
			}
		} else if (p instanceof Manager) {
			try {
				Manager m = (Manager) p;
				String nuovoRuolo = leggiString("Ruolo [" + m.getRuolo() + "]: ");
				if (!nuovoRuolo.isEmpty()) {
					m.setRuolo(leggiRuoloManager()); // Leggi ruolo per Manager
				}
				modificaCampiDipendente(m);
			} catch (RuoloNonValido e) {
				stampa(e.getMessage());
				return;// Mostra errore se ruolo non valido
			}
		} else if (p instanceof Dipendente) {
			modificaCampiDipendente((Dipendente) p); // Solo per Dipendente, non chiedere ruolo o area
		}

	}

	private void modificaCampiDipendente(Dipendente p) throws LunghezzaNome, StipendioNonValido, DataAssunzione {
		String email = leggiString("Email [" + p.getEmail() + "]: ");
		if (!email.isEmpty()) {
			validaCampo(email, "Email");
			p.setEmail(email);
		}

		String stipendioStr = leggiString("Stipendio [" + p.getStipendio() + "]: ");
		if (!stipendioStr.isEmpty()) {
			double stipendio = Double.parseDouble(stipendioStr);
			double min = 1000, max = 1800;
			if (p instanceof Manager && !(p instanceof Dirigente)) {
				min = 1800;
				max = 2500;
			} else if (p instanceof Dirigente) {
				min = 2500;
				max = 5000;
			}
			if (stipendio < min || stipendio > max) {
				throw new StipendioNonValido("Lo stipendio deve essere tra " + min + " e " + max + " euro.");
			}
			p.setStipendio(stipendio);
		}

		String dataAssunzioneStr = leggiString("Data di assunzione (dd/MM/yyyy) ["
				+ p.getDataAssunzione().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "]: ");
		if (!dataAssunzioneStr.isEmpty()) {
			LocalDate data = parseData(dataAssunzioneStr);
			LocalDate oggi = LocalDate.now();
	//		LocalDate domani = oggi.plusDays(1);
			if (data.isBefore(oggi)) {
			    throw new DataAssunzione("La data di assunzione non può essere nel passato.");
			}
			p.setDataAssunzione(data);
		}
	}

	public boolean confermaSi(String s) {
		String conferma = leggiString(s);

		if (conferma.equalsIgnoreCase("si"))
			return true;
		else
			return false;

	}

	private String validaCampo(String valore, String nomeCampo) throws LunghezzaNome {
		if (valore.length() < 3) {
			throw new LunghezzaNome(nomeCampo);
		}

		// Nome e Cognome non possono contenere numeri
		if (!nomeCampo.equalsIgnoreCase("Codice Fiscale") && !nomeCampo.equalsIgnoreCase("Email")
				&& valore.matches(".*\\d.*")) {
			throw new LunghezzaNome(nomeCampo, " non può contenere numeri");
		}

		// Email: deve contenere esattamente una "@"
		if (nomeCampo.equalsIgnoreCase("Email")) {
			long countAt = valore.chars().filter(ch -> ch == '@').count();
			if (countAt != 1) {
				throw new LunghezzaNome(nomeCampo, " deve contenere una @");
			}
		}

		return valore;
	}

	public void stampaPersona(Persona p) {
		System.out.println(p);
	}

	public void stampaPersona(HashMap<Integer, Persona> personaMap) throws ArrayVuoto {
		
		for (Persona p : personaMap.values()) {
			String tipo =
					 p instanceof Dirigente ? "Dirigente"
				       : p instanceof Manager ? "Manager"
					    : p instanceof Dipendente ? "Dipendente" : "Persona";
			System.out.println("[" + tipo + "] " + p);
		}
		
	
		
		
		
		
		
	}

	public String leggiString(String s) {
		System.out.println(s);
		return input.nextLine();
	}

	public int leggiInt(String s) {
		System.out.println(s);
		return Integer.parseInt(input.nextLine());
	}
	public int leggiIntRegex(String s, int max) {
	//	Scanner scanner = new Scanner(System.in);
	    while (true) {
	        System.out.print(s);
	        String inputnumero = input.nextLine();

	        if (inputnumero.matches("^[0-9]$")) {
	            return Integer.parseInt(inputnumero);
	        } else {
	            System.out.println("Input non valido. Inserisci un numero intero da 0 a " + max);
	        }
	    }
	}

	public void stampa(String stringa) {

		System.out.println(stringa);
	}

	public int menu() {
		System.out.println("Menu gestione persona");
		System.out.println("1) Inserimento");
		System.out.println("2) Ricerca codice fiscale");
		System.out.println("3) Elimina");
		System.out.println("4) Modifica");
		System.out.println("5) Stampa tutto");
		System.out.println("6) Promozione");
		System.out.println("0) Esci");
		return leggiIntRegex("Fai una scelta: ", 6);

	}

	public int menuTipoPersona() {
		System.out.println("Che tipo di persona vuoi inserire?");
		System.out.println("1) Persona");
		System.out.println("2) Dipendente");
		System.out.println("3) Manager");
		System.out.println("4) Dirigente");
		System.out.println("0) Torna indietro");

		int scelta = leggiIntRegex("Scelta: ", 4);

		while (scelta < 0 || scelta > 4) {
			System.out.println("Scelta non valida. Riprova.");
			scelta = leggiIntRegex("Scelta: ", 4);
		}

		
		return scelta;
	}

	public String leggiDataNascita(String s) {
		System.out.println(s);
		String inputDate = input.nextLine();

		if (isDataValida(inputDate)) {
			return inputDate;

		}

		return null;

	}

	public static boolean isDataValida(String dataStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");// .withResolverStyle(ResolverStyle.STRICT);

		LocalDate data = LocalDate.parse(dataStr, formatter);

		if (data.isAfter(LocalDate.now()) || (data.getYear() < 1900)) {

			return false;
		}

		return true;

	}

	public static LocalDate parseData(String dataStr) // throws NullPointerException

	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT); // non
																															// permette
																															// la
																															// modifica
																															// automatica
																															// nella
																															// prima
																															// data
																															// valida
		return LocalDate.parse(dataStr, formatter);
	}

	public int calcolaEta(LocalDate dataNascita) {

		return Period.between(dataNascita, LocalDate.now()).getYears();

	}

	public void promuoviPersona(Persona persona, String ruolo)
			throws DataAssunzione, StipendioNonValido, LunghezzaNome, ArrayVuoto, RuoloNonValido {
		// Verifica il ruolo attuale della persona
	
		 if (persona instanceof Manager) {
	        if (ruolo.equalsIgnoreCase("dirigente") || ruolo.equalsIgnoreCase("si")) {
	            promuoviAManagerInDirigente((Manager) persona);
	        } else {
	            System.out.println("Promozione non valida. Un Manager può diventare solo Dirigente.");
	        }

	    } else if (persona instanceof Dipendente) {
	        if (ruolo.equalsIgnoreCase("manager")) {
	            promuoviADipendenteInManager((Dipendente) persona);
	        } else if (ruolo.equalsIgnoreCase("dirigente")) {
	            promuoviADipendenteInDirigente((Dipendente) persona);
	        } else {
	            System.out.println("Promozione non valida. Un Dipendente può diventare Manager o Dirigente.");
	        }

	    } else if (persona instanceof Persona) {
	        if (ruolo.equalsIgnoreCase("dipendente")) {
	            promuoviAPersonaInDipendente(persona);
	        } else if (ruolo.equalsIgnoreCase("manager")) {
	            promuoviAPersonaInManager(persona);
	        } else if (ruolo.equalsIgnoreCase("dirigente")) {
	            promuoviAPersonaInDirigente(persona);
	        } else {
	            System.out.println("Ruolo non valido.");
	        }
	}}

	// Metodo per promuovere una Persona a Dipendente

	private void promuoviAPersonaInDipendente(Persona persona)
			throws DataAssunzione, StipendioNonValido, LunghezzaNome, ArrayVuoto {


		    // Cast della persona esistente in Dipendente
		 Dipendente dipendente = new Dipendente();
		    
		    // Copiamo i dati dalla persona alla dipendente
		    dipendente.setId(persona.getId());
		    dipendente.setNome(persona.getNome());
		    dipendente.setCognome(persona.getCognome());
		    dipendente.setDataNascita(persona.getDataNascita());
		    dipendente.setCodiceFiscale(persona.getCodiceFiscale());

		    // Impostiamo i campi specifici per il Dipendente
		    campiDpendente(dipendente);

		    // Aggiorniamo la mappa con il nuovo oggetto Dipendente
		    PersonaCrud.getInstance().update(persona.getId(), dipendente);
        //    crud.getInstance().update(persona.getId(), dipendente);
		    // Messaggio di successo
		    stampa("Promozione a Dipendente avvenuta con successo.");
		    stampaPersona(dipendente);

	}

	// Metodo per promuovere una Persona a Manager
	private void promuoviAPersonaInManager(Persona persona) throws DataAssunzione, StipendioNonValido, LunghezzaNome, RuoloNonValido {
		Manager manager = new Manager();
		manager.setId(persona.getId());
		manager.setNome(persona.getNome());
		manager.setCognome(persona.getCognome());
		manager.setDataNascita(persona.getDataNascita());
		manager.setCodiceFiscale(persona.getCodiceFiscale());
		campiDpendente(manager);
		campiManager(manager);
		manager.setRuolo("Manager");
		PersonaCrud.getInstance().update(persona.getId(), manager);
		System.out.println("Promozione a Manager avvenuta con successo.");
	}

	
	// Metodo per promuovere una Persona a Dirigente
	private void promuoviAPersonaInDirigente(Persona persona) throws RuoloNonValido, DataAssunzione, StipendioNonValido, LunghezzaNome {
		Dirigente dirigente = new Dirigente();
		dirigente.setId(persona.getId());
		dirigente.setNome(persona.getNome());
		dirigente.setCognome(persona.getCognome());
		dirigente.setDataNascita(persona.getDataNascita());
		dirigente.setCodiceFiscale(persona.getCodiceFiscale());
		campiDpendente(dirigente);
		campiManager(dirigente);
		campiDirigente(dirigente);
		PersonaCrud.getInstance().update(persona.getId(), dirigente);
		System.out.println("Promozione a Dirigente avvenuta con successo.");
	}

	// Metodo per promuovere un Dipendente a Manager
	private void promuoviADipendenteInManager(Dipendente dipendente) throws RuoloNonValido, StipendioNonValido {
		Manager manager = new Manager();
		manager.setId(dipendente.getId());
		manager.setNome(dipendente.getNome());
		manager.setCognome(dipendente.getCognome());
		manager.setDataNascita(dipendente.getDataNascita());
		manager.setCodiceFiscale(dipendente.getCodiceFiscale());
		manager.setDataAssunzione(dipendente.getDataAssunzione());
		manager.setEmail(dipendente.getEmail());
		manager.setStipendio(stipendio(manager));
		campiManager(manager);
		PersonaCrud.getInstance().update(dipendente.getId(), manager);
		System.out.println("Promozione a Manager avvenuta con successo.");
	}

	// Metodo per promuovere un Dipendente a Dirigente
	private void promuoviADipendenteInDirigente(Dipendente dipendente) throws RuoloNonValido, StipendioNonValido {
		Dirigente dirigente = new Dirigente();
		dirigente.setId(dipendente.getId());
		dirigente.setNome(dipendente.getNome());
		dirigente.setCognome(dipendente.getCognome());
		dirigente.setDataNascita(dipendente.getDataNascita());
		dirigente.setCodiceFiscale(dipendente.getCodiceFiscale());
		dirigente.setDataAssunzione(dipendente.getDataAssunzione());
		dirigente.setEmail(dipendente.getEmail());
		dirigente.setStipendio(stipendio(dirigente));
		campiManager(dirigente);
		campiDirigente(dirigente);
		PersonaCrud.getInstance().update(dipendente.getId(), dirigente);
		System.out.println("Promozione a Dirigente avvenuta con successo.");
	}

	// Metodo per promuovere un Manager a Dirigente
	private void promuoviAManagerInDirigente(Manager manager) throws StipendioNonValido {
		Dirigente dirigente = new Dirigente();
		dirigente.setId(manager.getId());
		dirigente.setNome(manager.getNome());
		dirigente.setCognome(manager.getCognome());
		dirigente.setDataNascita(manager.getDataNascita());
		dirigente.setCodiceFiscale(manager.getCodiceFiscale());
		dirigente.setRuolo(manager.getRuolo()); // Ruolo del manager
		dirigente.setDataAssunzione(manager.getDataAssunzione());
		dirigente.setEmail(manager.getEmail());
		dirigente.setStipendio(stipendio(dirigente));
		PersonaCrud.getInstance().update(manager.getId(), dirigente);
		System.out.println("Promozione a Dirigente avvenuta con successo.");
	}
}
