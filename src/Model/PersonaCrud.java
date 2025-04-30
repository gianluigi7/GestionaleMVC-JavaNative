package Model;

import java.util.HashMap;

import Exceptions.ArrayVuoto;


public class PersonaCrud {
	
	private HashMap<Integer, Persona> personaMap = new HashMap<Integer, Persona>();
	private static int idCounter = 1;
	private static PersonaCrud instance = null;
	
	
    public void insert(Persona persona) {
	persona.setId(idCounter);
    idCounter++;
       personaMap.put(persona.getId(), persona);
    }
	
    public PersonaCrud()
	{
		
	}
    	
    
    public static PersonaCrud getInstance() {
        if (instance == null) {
            instance = new PersonaCrud();
        }
        return instance;
    }
	
    public void update(int id, Persona persona) {
        personaMap.put(id, persona);
    }
 
	
    public HashMap<Integer, Persona> getHashMap() throws ArrayVuoto {
        if (personaMap.isEmpty()) {
            throw new ArrayVuoto("Nessuna persona è stata registrata.");
        }
        return personaMap;
    }
	
    public Persona ricercaCF(String codiceFiscale) {
        // Itera sulla mappa e cerca il codice fiscale nella lista di persone
        for (Persona persona : personaMap.values()) {
            if (persona.getCodiceFiscale().equalsIgnoreCase(codiceFiscale)) {
                return persona;  // Restituisce la persona trovata
            }
        }
        return null;  // Se non trovata, restituisce null
    }
    
    public Persona ricercaId(int id) {
        return personaMap.get(id);
    }
    
    public void rimuoviPersona(Integer id) throws ArrayVuoto {
        if (!personaMap.containsKey(id)) {
            throw new ArrayVuoto("Persona con ID " + id + " non esiste.");
        }
        personaMap.remove(id);
    }
    
    
}
	
