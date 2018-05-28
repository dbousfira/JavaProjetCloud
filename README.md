## DESCRIPTION DES SERVICES :

* AppManager : https://stark-savannah-65713.herokuapp.com/appmanager/
		
  * GET : **/**
  
    - **json[]** (name: string, approved: boolean)

  * POST : **/add**, "name" = string, "approved" = boolean
  
    - **json** (name: string, approved: boolean)

  * DELETE : **/delete/{name}**, {name} = string
  
    - **json** (success: boolean)
    

* LoanApproval : https://stark-savannah-65713.herokuapp.com/loanapproval/

  * GET : **/{name}/{amount}**, {name} = string, {amount} = long
  
    - **json** (account: json, approved: boolean)

## REPARTITION DU TRAVAIL :

* Hugo : AppManager, LoanApproval

* Djamel : AccManager, CheckAccount
