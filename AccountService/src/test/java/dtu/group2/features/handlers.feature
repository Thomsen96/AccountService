Feature: Check Account Service handlers act as expected

  Scenario: handleAccountStatusRequest
    When an AccountStatusRequest event is received
    Then an AccountStatusResponse is sent
    And the event response in the message was success "true"
    
##		Requires setup of user in the bank first, and that happens in another step defs ....
  #Scenario: Valid createCustomerAccountRequest
    #When a valid createCustomerAccountRequest event is received
    #Then a createCustomerAccountRequest is sent
    #And the event response in the message was success "true"
    
  Scenario: Invalid createCustomerAccountRequest
    When an invalid createCustomerAccountRequest event is received
    Then a createCustomerAccountRequest is sent
    And the event response in the message was success "false"

##		Requires setup of user in the bank first, and that happens in another step defs ....
  #Scenario: Valid createMerchantAccountRequest
    #When a valid createMerchantAccountRequest event is received
    #Then a createMerchantAccountResponse is sent
    #And the event response in the message was success "true"
    
  Scenario: Invalid createMerchantAccountRequest
    When an invalid createMerchantAccountRequest event is received
    Then a createMerchantAccountResponse is sent
    And the event response in the message was success "false"
