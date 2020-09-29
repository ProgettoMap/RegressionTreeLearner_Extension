# FAQ - Client
Q: Perchè ottengo sempre l'errore "Cannot create a connection with the database"?

A:	Assicurati che il server abbia avviato il Database Server prima di aprire l'applicativo.

# Regole di progetto
Una volta clonato il progetto, assicurarsi che esso abbia la seguente struttura:

# N.B. Non modificare **assolutamente** la seguente struttura
    ```
    RegressionTreeLearner_Extension\
        \.git
        \README.md
        \Map7Client_ext
            \src
            \bin (non tracciata)
        \Map7Server_ext
            \src
            \bin (non tracciata)      
    ```
---
# Import del progetto nell'editor:
## Eclipse
File > Import... > Projects from File System or Archive > Cliccare su directory e selezionare la cartella (Map7Client_ext o Map7Server_ext) > Cliccare su finish