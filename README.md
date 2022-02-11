# atm-service

atm-service is thin client which communicates straight to the consumer using cache for storing token information, 
this service uses card validation mechanism which generates token for authorization which is the second step when user enters credentials PIN or FINGERPRINT,
this endpoint generates another token for the use of user operations such as deposit, withdraw, check balance, print receipt.

atm-service has communication with bank-service using feign-client, which plays the role of the bank using SQL database.
