Progetto per Academy java di IT Consulting
la simulazione del database avviene usando un HashMap, la gestione delle persone inserite segue queste caratteristiche:

Persona id:1 nome:a, cognome:b .... key 1
	dai 16 anni compresi in su id:5 nome:a, cognome:b .... key 5
	Integer id;
	String nome
	String cognome
	Data dataDiNascita
	String cf
	-----> l'età va stampata nel toString
	------------
	Dipendente
	double stipendio min 1000 - 1800
	String email
	Data dataDiAssunzione ---> non può essere retroattiva!

	----------
	Manager stipendio ---> 1800- 2500
	String ruolo ----> pm/ team leader / top manager


	public enum tipoRuolo{
	pm,
	team leader,
	top manager

	}

	------------
	Dirigenti stipendio---> 2500-5000
	String area Amministrativa/ Business / IT Operations

	public enum tipoArea{
	Amministrativa,
	Business,
	IT Operations

	}

	tipoArea area=tipoArea.Business;


	hashMap<Integer,Persona> key ----> id PARTE DA 1

	Icrud --->
	Inserire
	Ricercare CF / ID
	Eliminare CF/ ID
	Modificare CF/ID
	Stampatutto

	Promozione ----> CF / ID Persona ---> Dipendente /Manager/Dirigente se e solo se ha la mag. età!
	Dipendente ---> Manager / Dirigente
	Manager ---> Dirigente */
