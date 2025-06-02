












# Context

- proximity by lat,long to find top five resutrant
  - ranking by rating

* user provide   
  - range bound
  - current location 


## scale
* 50M total, 10 M dailly users avg usage twice day
* 5M total
* image/contents

# NFR
- high priority for low latency, tolerate consistancy
- high avaibility 

--- outscope ---
- obsiberbility



# API
Core Entities
- Business
- User location
- Search-Result
  - list of ranked business

GET /api/business/?curLat= &curLong &range=5km

{
    [
        {
            businessId: "123",
            displayName; ""biz",
            logoImage: "url",
            address: "",
            _link: [
                { "view", "/biz/1/detais"}
            ]
        }
    ]
}
