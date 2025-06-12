# Context

### Description
write 
- a function which accepts a rating, 
- and another which will get all of the agents and the average rating each one has received
  - ordered highest to lowest
  - same average : tie breaker Id


### Details


Costomer support ticketing : 
- rating agents : 1 to 5
- average rating of agents

* Write Rating - customer scale to global : write fast
* Read rating - average : memory all rating
  * sorted list of all agent by average

###
  Agent[id] *-- RatingsSummary(SumTotal, Count) +-- (RatingValue) 

