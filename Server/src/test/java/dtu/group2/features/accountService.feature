Feature: Account

# Create
	Scenario: Successfully create a customer account
		Given a customer with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		Then a customer account exists with that accountID

# Delete
	Scenario: Successfully delete an account
		Given a customer with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		And the customer account is deleted
		And the customer account is fetched
		Then A "Account does not exist" exception is raised

	Scenario: Delete account that does not exist
		Given account ID "abc"
		When the customer account is deleted
		Then A "Account does not exist" exception is raised

	Scenario: Test messagequeue
		Given a customer with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		Then a messagequeue message is produced

	Scenario: getCustomer request
		Given a customer with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		When a request is received
		Then a uid is received and customer returned

	Scenario: VerifyToken request
		Given a customer with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		When a request is received for verification
		Then a uid is received and verification of the custumer is returned



