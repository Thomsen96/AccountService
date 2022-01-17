Feature: Account

# Create
	Scenario: Successfully create a customer account
		Given a user with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		Then a customer account exists with that accountID

	Scenario: Successfully create a merchant account
		Given a user with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a merchant tries to create an account
		Then a merchant account exists with that accountID

# Delete
	Scenario: Successfully delete a customer account
		Given a user with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		And the customer account is deleted
		Then the customer no longer exists

	Scenario: Successfully delete a merchant account
		Given a user with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a merchant tries to create an account
		And the merchant account is deleted
		Then the merchant no longer exists

	Scenario: Delete account that does not exist
		Given account ID "abc"
		When the customer account is deleted
		Then the customer no longer exists

	Scenario: Test messagequeue
		Given a user with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		Then a messagequeue message is produced

	Scenario: getCustomer request
		Given a user with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a customer tries to create an account
		When a request is received
		Then a uid is received and customer returned

	Scenario: getMerchant request
		Given a user with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
		When a merchant tries to create an account
		When a request is received
		Then a uid is received and merchant returned

#	Scenario: VerifyToken request
#		Given a user with first name "Johnson1", last name "McJohnson1" and cpr number "666999-69691" and balance of "420"
#		When a customer tries to create an account
#		When a request is received for verification
#		Then a uid is received and verification of the custumer is returned



