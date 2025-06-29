<p align="center">
  <img src="https://github.com/user-attachments/assets/dcc628cd-e7f1-4d0a-91dd-0a81483e5859" alt="FlexiMove Logo" width="325" height="175"/>
  <img src="https://github.com/user-attachments/assets/f0b695f7-4c5c-4112-9661-5e405d3f9b1c" alt="FlexiMove Banner" width="325" height="175"/>
</p>

**FlexiMove is a modular microservices-based mobility application designed for booking and managing shared vehicles like cars, bikes, and scooters. It includes frontend and backend components with proper separation of concerns and scalable service boundaries.**


### Backend Startup Instructions
Please make sure you run the Business Logic Part first in order to see the Application's Frontend in the proper way. 


**Note:**
- All services depend on `config-server` and `eureka-server`, so make sure those are up first.
- Services fetch their configuration from `http://localhost:8888` and register themselves with Eureka on `http://localhost:8761`, which retrieves its settings from a remote Git repository.
- Services use Feign clients to communicate with each other using Eureka service names (e.g., `@FeignClient(name = "bookingService")`).

**Prerequisites:**
- Java 21
- Maven 3.8+
- (Optional) IntelliJ IDEA or any preferred IDE

**Order of Running the Services:**
1. Config Server
2. Eureka Server
3. Booking Microservice, Vehicle Microservice, User-Context Microservice, Payment Microservice and Rating Microservice can be started in any order

You can run each Service by going to their Main Application Class (e.g. `<ServiceName>Application` Class File) and press the Run Button

Preferably, You can run the Project from the Terminal and for that you can go to the Root Directory of each Service and run: 
- `mvn spring-boot:run`

Below you can see the Ports which using them you can access those respective Services: 
- Config Server http://localhost:8888
- Eureka Server http://localhost:8761
- Booking Service http://localhost:8083
- Vehicle Service http://localhost:8085
- User Context Service http://localhost:8081
- Payment Service http://localhost:8082
- Rating Service http://localhost:8084

All the Services will Automatically register with Service Registry Eureka and fetch the Configuration from the Config Server (Service names will be shown in the Eureka Dashboard as it is illustrated in the Screenshot below)

![Screenshot 2025-06-29 at 7 48 06 PM](https://github.com/user-attachments/assets/211dfbc2-2ecb-4f73-a21b-5404841bdf29)


**How to Stop the Services:**

If you started services using `mvn spring-boot:run`, you can stop them by:
- Pressing `Ctrl+C` in the terminal window
- Or closing the running terminal tab/window


----------------

### Frontend Startup Instructions


**Prerequisites:**
- Node.js 18+
- npm (installed with Node.js)
- Angular CLI (version 16)


**For running the Frontend:** 
1. Install Angular CLI with: npm install -g @angular/cli@16
2. Navigate to the Frontend Folder: `cd fleximove-frontend`
3. Install Dependencies: `npm install`
4. Start the Frontend Service:  `ng serve`


- The Frontend then will be available on http://localhost:4200/

**How to Stop the Frontend:**

In the Terminal which the Frontend was ran using `ng serve`, you can stop it by: 
- Pressing `Ctrl+C` in the Terminal Window
- Or closing the running Terminal Tab/Window
 
