# Etoro-accounting
Etoro tax calculation - helps in calculating the income tax you have to pay after your earnings on Etoro. Implemented for Hungarian tax law, but easily expandable.

## How to run it
1. Download your Etoro balance sheet from the Etoro website (xslx), for any time period you want
2. Run the app ```java hu.tomosvari.etoro.EtoroCalculator /path/to/etoro.xlsx```
3. On the console the app will output some stats and the tax you have to pay

## Tax law modules

**DISCLAIMER** - I am no accountant, I implemented these modules to my best knowledge, but by using this app all responsibility is yours in case the code is not compliant with the laws in any way.

### Hungary

After your Etoro earnings you have to pay 15% income tax. Transactions on your Etoro account should count as "Ellenorzott tokepiaci ugylet" by the rules in this case you have to pay tax after the net income (income - losses) counting all the closed transactions. The code goes through all closed transactions, exchanges the USD closing values to HUF using the MNB (Magyar Nemzeti Bank, Hungarian National Bank) daily mean exchange rate for the closing day obtained from a publicly available MNB API. After this it calculates the net income and the 15% you have to pay as tax. The main advatage of this module is the currency exchange as exchanging all the closing values to HUF at the given MNB rate for that day would be difficult manually.
