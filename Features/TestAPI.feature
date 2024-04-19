Feature: API
	Scenario: Test GET 
	Given i set endpoint "https://ims-customer1-sit.apps.co.id/restv2/cs01/customers/5876S9/addresses"
	And I set request Headers
	And I add request header "Content-Type" with value "application/json"
	And I add request header "X-Node" with value "LOCAL"
	And I add request header "X-Request-ID" with value "9d275701"
	And I add request header "X-Channel-ID" with value "6017"
	And I add request header "X-User-ID" with value "098765432"
	And I add request header "X-Acq-ID" with value "213"
	When I send a GET HTTP request
	Then I verify status code is return "200 OK"
	
	Scenario: Test POST
	Given i set endpoint "https://ims-customer1-sit.apps.co.id/restv2/tp01/releaseBlock"
	And I set request Headers
	And I add request header "X-Acq-ID" with value "213"
	And I add request header "X-Channel-ID" with value "6017"
	And I add request header "X-Channel-Type" with value "6017"
	And I add request header "X-Node" with value "taspen"
	And I add request header "X-Reference-No" with value "93900232"
	And I add request header "X-Request-ID" with value "93900232"
	And I add request header "X-Terminal-Date-Time" with value "2021-03-22T14:13:47.753"
	And I add request header "X-Terminal-Name" with value "ESBTEST"
	And I add request header "X-User-ID" with value "dfgh345gh8680"
	And I add request header "Content-Type" with value "application/json"
	When I send a POST HTTP request with body '{"userName": "60a9f062-c6b9-42bf-825c-5e378c8df63a","signature": "790270c6fca270f3dbbc18365c716697","productCode": "080010","bankCode": "213","channel": "6017","terminal": "EDPAPI01","terminalName": "EDAPEM API SERV","terminalLocation": "TASPEN","transactionType": "33","billNumber": "2332232","amount": "000001328600","feeAmount": "000000000000", "bit61": "0000000601001991138005138087        CHADIJAH                      121","traxId": "00O209390023108","timeStamp": "23-03-2021 10:42:49:236"}'
	Then I verify status code is return "200 OK"