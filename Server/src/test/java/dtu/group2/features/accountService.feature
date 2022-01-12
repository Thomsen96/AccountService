Feature: Account

# Create
	Scenario: Successfully create a bank account
		Given a customer with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691"
		And the customer have a balance of "420"
		When the bank creates an account with an accountID
		Then a customer account exists with that accountID
		
	Scenario: Create an account for an empty customer
		Given a user with no first name, last name or cpr number
		And the customer have a balance of "0"
		When the bank creates an account with an accountID
		Then A "Missing CPR number" exception is raised

	Scenario: Create an account with invalid balance for customer
		Given a customer with first name "Johnson", last name "McJohnson" and cpr number "666999-6969"
		And the customer have a balance of "HejMedDig"
		When the bank creates an account with an accountID
		Then A "Character H is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark." exception is raised
		
# Delete
	Scenario: Successfully delete an account
		Given a customer with first name "Johnson", last name "McJohnson" and cpr number "666999-6969"
		And the customer have a balance of "420"
		When the bank creates an account with an accountID
		And the customer account is deleted
		And the customer account is fetched
		Then A "Account does not exist" exception is raised

	Scenario: Delete account that does not exist
		Given account ID "abc"
		When the customer account is deleted
		Then A "Account does not exist" exception is raised

	Scenario: Test messagequeue
		Given a customer with first name "Johnson", last name "McJohnson" and cpr number "666999-6969"
		And the customer have a balance of "420"
		When the bank creates an account with an accountID
		Then a messagequeue message is produced

	Scenario: getCustomer request
		Given a customer with first name "JohnsonSon", last name "McJohnsonSon" and cpr number "6669991-69691"
		And the customer have a balance of "42011"
		When the bank creates an account with an accountID
		When a request is received
		Then a uid is received and customer returned

	Scenario: VerifyToken request
		Given a customer with first name "JohnsonSon", last name "McJohnsonSon" and cpr number "6669991-69691"
		And the customer have a balance of "42011"
		When the bank creates an account with an accountID
		When a request is received for verification
		Then a uid is received and verification of the custumer is returned



