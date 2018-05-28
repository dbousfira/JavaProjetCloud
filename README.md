## DESCRIPTION DES SERVICES :

* AppManager : https://stark-savannah-65713.herokuapp.com/appmanager/
		
  * GET : **/**

  * POST : **/add**, "name" = string, "approved" = boolean

  * DELETE : **/delete/{name}**, {name} = string

* LoanApproval : https://stark-savannah-65713.herokuapp.com/loanapproval/

  * GET : **/{name}/{amount}**, {name} = string, {amount} = long
  

## REPARTITION DU TRAVAIL

* Hugo : AppManager, LoanApproval

* Djamel : AccManager, Check_account
