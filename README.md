# atm-service

atm-service is thin client which communicates straight to the consumer using cache for storing token information, 
this service uses card validation mechanism which generates token for authorization which is the second step when user enters credentials PIN or FINGERPRINT,
this endpoint generates another token for the use of user operations such as deposit, withdraw, check balance, print receipt.

atm-service has communication to bank-service using feign-client (with basic authentication for demonstration), 
which plays the role of the bank using SQL database.

to access swagger use following link http://localhost:8080/atm-service/swagger-ui.html/

test data for usage:

| account_number | PIN  |   balance   | card_number      | finger_print   |
|  ------------- | ---- |  ---------- | ---------------- | -------------  |        
| e941c222-cbdc	 | 111  |  30000.00	  | 5488123412341234 |	bb05b5a8-aa57 |
| 6f5e713a-1ac0	 | 333  |  300.00	    | 5488123412341232 |  bb05b5a8-aa57 |
| 5f20ac8d-b05b	 | 123  |  0.00	      | 5488123412341235 |	bb05b5a8-aa57 |
| 53bea438-2cf6	 | 222  |  300.00	    | 5488123412341233 |	bb05b5a8-aa57 |
| 4a391332-7e28	 | 444  |  300.00	    | 5488123412341231 |	bb05b5a8-aa57 |
