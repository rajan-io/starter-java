# Payment Processing System

## Requirements

### Functional Requirements
* User --> Payment Service --> Payment Gateway;  
   Payment Gateway takes 500ms-2000ms to process payments

  -- out of scope --  
- reconciliations of payment on timeouts for user

### Non-Functional Requirements
* 100K request/seconds peak. 10M active users
* Availability under load
* Cost effective on resource provisioning

-- out of scope --
- Availability in case of disaster
- Rate limiting, throttling down-stream systems


## API

### Initiating payment request
```http
POST /api/payment

HTTP/1.1 202 Accepted
{
  "transactionID": "transactionID",
  "monitorUrl": "/api/status/{transactionID}",
  "eta" : "500 ms"
}
```

### Get payment status
```http
GET /api/status/{transactionId}

HTTP/1.1 200 OK
{
  "status": "completed"
}
```


## System Design
### Competent design diagram
![System-Design](./c2-functional-view.svg "Component design view")


## ADRs: Key Decisions

- Making system non blocking async it can serve more requests per seconds.  
  Assumption : Hardware capacity and implementation it is able to handle 1K concurrent http request for payment per server-node of payment capture service.
    - Payment request blocked for 500ms, can support 2K request/second per node => 50 nodes  
    - Kafka write latency 20-50ms, it can support 20K request/second per node => 5 nodes
- Async api for payment allows handling large requests per second but increases latency of individual payment. 
  - Option of accepting payment request and pushing into message queue, allows system to handle burst of traffic.
  - It enables options for sending bulk messages to payment gateway to reduce network latency of overall system.
  - It makes it more fault-tolerant in case of intermittent failure of payment gateway.
- There is trade-off in favour of accepting more payment request, but it increases more http request load as status monitoring. 
  - It can be mitigated further by using dynamic polling duration using "eta" for reducing status poll load.
  - Payment Status leverages cache with latest update from payment gateway event stream.
  - It enables scaling out/in of resources effectively.
- Reactive system implementation with async non-blocking IO. 
  -  for JVM based implementation Netty server with Spring Web-Flux or Kotlin Coroutines specially for Payment Capture Service for handing IO bound processing.
  -  Kafka as pub/sub and Redis as distributed cache.

