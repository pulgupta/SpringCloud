Spring Cloud Patters
==================

  - [Usage](#usage)
  - [Features] (#features)
  - [Missing features?](#missing-features)
  - [License](#license)


### Spring cloud features and Patterns

**Cloud Patters** - Cloud seems an ovious choice for projects these days. However what really important is to really understand the needs of a particular project and then implement the cloud correctly.

Things are chainging at a rapid rate and thus we should keep our system really agile and adaptable to make use of the latest and even future technologies.

>### *What’s the point of these intimidating Patters?*
These patterns though sometimes may feel over the top but really helps an orginasization to grow and still maintain its agility.

### *Why should I care?*
Because this is the future. These patters will one day become the de facto standard for an enterprise of any size or type. These patters have proved successful for major technology players and so we can't just ignore them.

## Usage

	Just download all the patters and use the ones which you want to implement.
	
## Features
  The projects have samples implemented for the below patters
    Centralized Configuration
    Service Registory
    Service Discovery
    Circuit Breaker

	Centralized Configuration or config server as it is known help us to keep all our confguration at a central place. All our systems and services will use this server to read the configurations.
	Service Registry is used to register a service as it comes up.
	Service Discovery is used by services which are dependent on other services but are not sure where to locate them.
	Circuit Breaker is used to break the interdependency of one service on another in case the services are not available. The breaker will know that a service is now available and will redirect the calls to a fall back endpoint. 
    
    
## Missing features
  There are lots more patters which can improve our cloud offerings.
  I will be adding more patters and feature to the project like security, encryptions etc.
  
## License
This software is released under the [MIT License](http://www.opensource.org/licenses/MIT).
