## DESCRIPTION DES SERVICES :

* AppManager : https://stark-savannah-65713.herokuapp.com/appmanager/
		
  * GET : **/**
  
    - **json[]**

  * POST : **/add**, "name" = string, "approved" = boolean
  
    - **json**

  * DELETE : **/delete/{name}**, {name} = string
  
    - **boolean**
    

* LoanApproval : https://stark-savannah-65713.herokuapp.com/loanapproval/

  * GET : **/{name}/{amount}**, {name} = string, {amount} = long
  
    - **json**

## REPARTITION DU TRAVAIL

* Hugo : AppManager, LoanApproval

* Djamel : AccManager, Check_account
