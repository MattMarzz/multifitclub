# MultiFitClub - ISPW 23/24

## Descrizione
Questo progetto è stato realizzato in quanto requisito vincolante per lo
svolgimento dell'esame _"Ingegneria del Software e Progettazione Web 2023/24"_ presso
_Università degli Studi di Roma "Tor Vergata"_.  
  
Il progetto, in particolare, riguarda la gestione dei corsi all'interno di una
struttura sportiva, con la possibilità di richiedere prenotazioni per sale e 
inviare/leggere comunicazioni.  
  
  
## Struttura
```cmd
/MultiFitClub
    /logs                                       # File di log
    /src                                        # Codice sorgente del progetto
        /main
             /java
                it/.../ispw
                    /bean                       # Classi bean
                    /controller                 # Controller applicativi
                    /enums                      # Enumerazioni
                    /excepiton                  # Eccezioni personalizzate
                    /model                      # Model divisi per tipo + DAO
                    /notification               # Client/Server per le notifiche in tempo reale (main)
                    /utils                      # Classi utilitarie
                    /view
                        /cli                    # Interfaccia CLI applicazione
                        /graphicalcontroller    # Controller grafici JavaFx
                    App.java                    # Main class (main)
                module-info.java
             /resources
                /csv                            # File csv per la persistenza
                    /test                       # File csv a scopo di test
                /images                         # Immagini
                /it/.../views
                    /segreteria                 # Interfacce grafiche segreteria
                    /utente                     # Interfacce grafiche utente
                /sql                            # Dump db structure
                application.properties          # Configurazioni persistenza e UIs
        /test
            /java/.../ispw
                /model
                    /lezione                    # Test DAO e model "Lezione"
                /utils                          # Test utils
    pom.xml                                     # Dipendenze progetto
    README.md                                   # Documentazione del progetto
```
## Avvio
Per l'applicativo sono previsti due livelli di persistenza e due diverse interfacce grafiche.
Si possono specificare entrambe le scelte direttamente dal file _application.properties_.  
Per garantire il corretto funzionamento delle notifiche sarà necessario avviare prima il _ServerNotification.java_.

### Livelli di persistenza
* **DBMS**: sarà necessario creare lo schema (cartella _.../sql_)
* **File System**: i dati saranno salvati in file _.csv_ all'interno dell'apposita cartella (_.../csv_)  
  
### Interfacce grafiche
* **JavaFx**
* **CLI**