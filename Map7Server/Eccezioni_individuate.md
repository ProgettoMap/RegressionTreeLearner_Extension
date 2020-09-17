# Classe Data

<table>
    <thead>
        <tr>
            <th>Gruppo</th>
            <th>Eccezione</th>
            <th>Messaggio errore</th>
            <th>Implementata</th>
            <th>Testata</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td rowspan=2>Attributo @schema</td>
            <td>La prima stringa del file non ha due parole separate da uno spazio (Lunghezza vettore diversa da 2)</td>
            <td>Errore nel training set a riga 1: il numero di parole trovate nella riga è diverso da quello atteso.
Ricontrolla la sintassi.
Esempio: "@schema &ltnumero intero positivo maggiore di 0&gt"</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <tr>
            <td>
              <ul>
                  <li>Mancanza dell'attributo nel training set alla prima riga (valore prima parola diverso da "@schema")</li>
                  <li> Mancanza del valore del parametro (valore seconda parola vuota)</li><li>parametro non numerico o negativo
              </ul>
            </td>
            <td >Errore nel training set a riga 1: Attributo @schema non trovato.</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <!-- -- -- -- -- -- -- -- FINE RIGA -- -- -- -- -- -- -- -- -- -- -->
        <!-- Aggiungere eccezione se il numero di attributi specificati con desc è uguale a quelli di schema -->
        <tr>
            <td rowspan=3>Attributo @desc</td>
            <td>La stringa del file non ha tre parole separate da uno spazio (Lunghezza vettore diversa da 3)</td>
            <td >Errore nel training set a riga 2: il numero di parole trovate nella riga è diverso da quello atteso.
Ricontrolla la sintassi.
Esempio: '@desc &ltnome attributo&gt &ltvalore1,valore2...&gt'</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <tr>
            <td>Mancanza dell'attributo @data in tutto il training set (valore prima parola diverso da "@desc")</td>
            <td >Errore nel training set: Tag @desc non trovato</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <tr>
            <td>
              <ul>
                <li> Mancanza del valore dell'attributo nel training set (ci si aspetta una stringa separata da virgole) (valore terza parola  == "") </li>
                <li> Mancanza del nome dell'attributo nel training set (valore seconda parola == "") </li>
            </ul>
          </td>
            <td >Errore nel training set a riga 2: Non è stato specificato correttamente il nome dell'attributo. Ricontrolla.</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <!-- -- -- -- -- -- -- -- FINE RIGA -- -- -- -- -- -- -- -- -- -- -->
        <tr>
            <td >Attributo @target</td>
            <td>Mancanza dell'attributo nel training set (valore della prima parola diverso da "@target")</td>
            <td ></td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <!-- -- -- -- -- -- -- -- FINE RIGA -- -- -- -- -- -- -- -- -- -- -->
        <tr>
            <td rowspan=3>Attributo @data</td>
            <td>La stringa del file non ha due parole separate da uno spazio (Lunghezza vettore diversa da 2)</td>
            <td >Errore nel training set a riga 7: il numero di parole trovate nella riga dell'attributo @data è diverso da quello atteso.
Ricontrolla la sintassi.
Esempio: '@data &ltvalore intero maggiore di 0&gt'
</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <tr>
            <td>Mancanza dell'attributo nel training set (valore della prima parola diverso da "@data")</td>
            <td >Attributo @data non trovato nel training set</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <tr>
            <td>
              <ul>
                  <li> Mancanza del valore del parametro (valore seconda parola vuota)</li>
                  <li> Parametro non numerico o negativo</li>
              </ul>
            </td>
            <td > Errore nel training set a riga 7: il parametro specificato per il tag @data non è un intero maggiore di 0</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <!-- -- -- -- -- -- -- -- FINE RIGA -- -- -- -- -- -- -- -- -- -- -->
        <tr>
            <td rowspan=5>Eccezioni esempi training set</td>
            <td>Numero di attributi letti nella singola riga diversi da @schema</td>
            <td >data.TrainingDataException: java.lang.ArrayIndexOutOfBoundsException: I valori letti in riga 8 sono diversi dal numero di attributi attesi nel file servo.dat</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <tr>
            <td>Numero di righe explanatory set diverso da valore @data</td>
            <td>I valori letti sono diversi dal parametro @data</td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <tr>
            <td>Valori attributi nella riga letta dell'explanatory set non compresi negli attributi specificati per l'i-esimo attributo nell'intestazione del file (EXPLANATORYSET)</td>
            <td ></td>
            <td align="center">✓</td>
            <td align="center">✓</td>
        </tr>
        <tr>
            <td>
              Attributo di classe presente con valore non float
            </td>
            <td >Il valore target specificato nella riga 1, colonna 5 non è di tipo double</td>
            <td align="center">✓</td>
            <td align="center"></td>
        </tr>
        <tr>
            <td>Righe lette diverse da @data</td>
            <td ></td>
            <td align="center"></td>
            <td align="center"></td>
        </tr>
        <!-- Aggiungere numero di righe lette maggiore o minore di @data -->
    </tbody>
</table>
