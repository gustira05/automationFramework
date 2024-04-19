Feature: E2E

	@TestID=T39580687
	Scenario: As a user i want to be able to add new owner
	Given I open Pet Clinic website "http://10.1.77.217:8080/" with "chrome"
#	When I click "Find owners" menu by text
#	And I click "Add Owner" by text
#	And I enter "Dua" in "firstName" by ID
#	And I enter "Triliun" in "lastName" by ID
#	And I enter "Malaka" in "address" by ID
#	And I enter "Jakarta" in "city" by ID
#	And I enter "081219264" in "telephone" by ID
#	And I click "Add Owner" by text
#	Then I verify element by css selector "table:nth-child(2) > tbody > tr:nth-child(1) > td" has text "Dua Triliun"
	#	@TestID=T39580688
#	Scenario: As a user i want to be able to find owner
#	Given I open Pet Clinic website "http://10.1.77.217:8080/" with "chrome"
#	When I click "Find owners" menu by text
#	And I enter "Triliun" in "lastName" by ID
#	And I click "#search-owner-form > div:nth-child(2) > div > button" by css selector
#	Then I verify element by css selector "body > div > div > table:nth-child(2) > tbody > tr:nth-child(1) > td > b" has text "Dua Triliun"
#	
#	@TestID=T39558955
#	Scenario: As a user i want to be able to add new owner
#	Given I open Pet Clinic Website "http://10.1.77.217:8080/" with "chrome"
#	When I click "Find owners" menu by text
#	And I click "Add Owner" by text
#	And I enter "Lima" in "firstName" by ID
#	And I enter "Ribu" in "lastName" by ID
#	And I enter "Malaka" in "address" by ID
#	And I enter "Jakarta" in "city" by ID
#	And I enter "081219264" in "telephone" by ID
#	And I click "Add Owner" by text
#	Then I verify element by css selector "table:nth-child(2) > tbody > tr:nth-child(1) > td" has text "Lima Perak"
#	
#	@TestID=T39558956
#	Scenario: As a user i want to be able to find owner
#	Given I open Pet Clinic website "http://10.1.77.217:8080/" with "chrome"
#	When I click "Find owners" menu by text
#	And I enter "Ribu" in "lastName" by ID
#	And I click "#search-owner-form > div:nth-child(2) > div > button" by css selector
#	Then I verify element by css selector "body > div > div > table:nth-child(2) > tbody > tr:nth-child(1) > td > b" has text "Empat Ribu"