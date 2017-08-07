## TDD-pingis Asiakaspalaveri 7.8.

Brief:
Pelimekaniikan "vuorottelun" toteuttaminen jatkuu. Toiveena ensi viikon demo kahdella käyttäjällä. 

A: Olisi ehkä myös hyödyllistä, että feedback-sivulla olisi progressbar, tai voi miettiä myös sitä vaihtoehtoa, että pysytään tehtäväntekosivulla samalla kun TMC-submissionia tehdään.

A: Toiveena pikkujuttuja, Id:eitä ei kannata pistää nousevasti numeerisiksi, jottei järjestystä voida arvata ja siten hakea suoraan sivuja osoiteriviltä 
Me: Uusin toteutus toimii Id:eillä, jotka eivät ole järjestyksessä

A: Arto: näyttää hyvältä, positiivinen yllätys userboardin tomaatista (asiakasta on kuunneltu)

A: Mihin te tarvitsette sitä Pointsia? Onko aikaisempi bugi, joka on jäänyt TMC-palautejärjestelmään? (Pitäisi toimia) Voi olla TestScanner, joka vaatii annotointia -> bugista tiedot Henkalle?
Me: Meillä submission antoi virheitä, joten pääteltiin että Points-annotaatio tarvitaan
A: Jos saatte std-out-stringin näkyviin, siitä voitte päätellä ovatko Pointsit tarpeellisia vai ei.

A: Tallentuvatko kirjoitetut testit jo tietokantaan?
Me: eivät vielä. Toteutus viimeistellään seuraavan sprintin aikana.

Me: mitkä odotukset seuraavalta sprintiltä?
A: pelissä eteneminen, vuorottelumekanismin toiminta
A: Hienoa olisi jos pystyisitte demoamaan pelimekaniikkaa jo kahdella koneella, vastatusten

A: Onko deployattuna jo yleisesti saatavilla? Springin paikallinen toteutus Oauthin kannalta on hieman raivostuttava, siitä on olemassa issue.

A: Pystyykö järjestelmä jossain vaiheessa tunnistamaan, että missä vaiheessa käyttäjä ei enää jaksa keskittyä (keskittyminen herpaantuminen heikentää myös oppimistulosta)? Siis jonkinlainen optimaalinen tehtävientekoaika per päivä. Menen ehkä itseni edelle, tämä on toteutuksen kannalta vasta hyvin kaukana tulevaisuudessa.

A: Vinkkinä, keskittykää mieluummin uuden toiminnallisuuden toteuttamiseen, Refaktorointi-taskit eivät välttämättä hyviä, koska refaktorointia voi tehdä loputtomasti.


