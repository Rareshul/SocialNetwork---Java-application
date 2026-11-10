Fisier inexistet: am o problema si imi crapa in try si inca nu am reusit sa o rezolva, TO-DO pentru lab 4 :(

Repository:

Am decis sa imi fac o clasa AbstractFileRepo care implementeaza saveToFile si loadFromFile generice, folosind 2 
functii auxiliare extractEntity si createEntityToString;

extractEntity: primeste un string si ne returneza obiectul dorit (pentru loadFromFile)
createEntityToString: primeste un obiect si il transforma in string pentru a l pune in fisier

Tratarea fisierelor corupte
Verfici linie cu linie daca este formatul dorit, iar daca nu avem o linie corupta si totodata un fisier corupt si ignora linia

Sabloane
Factory: acest sablon se regaseste la repository, ne ajuta sa creem repo uril dupa nevoie

SRP: Este prezent la fiecare functie (loadFromFile())
OCP: AbstractFileRepo: este deschisa pentru a implementa noi functionalitati, dar nu o poti modifica
LSP: Repository urile care se "trag" din clasa main Repository

