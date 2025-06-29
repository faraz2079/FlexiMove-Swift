<p align="center">
  <img src="https://github.com/user-attachments/assets/cfa6afd9-67d5-4105-883e-12e0adfd4c09" alt="FlexiMove Logo" width="250"/>
</p>



**FlexiMove is a modular microservices-based urban mobility application designed for booking and managing shared vehicles like cars, bikes, and scooters. It includes frontend and backend components with proper separation of concerns and scalable service boundaries.**


### Backend Startup Instructions
Please make sure you run the Business Logic Part first in order to see the Application's Frontend in the proper way. 


**Note:**
- All services depend on `config-server` and `eureka-server`, so make sure those are up first.
- Services fetch their configuration from `http://localhost:8888` and register themselves with Eureka on `http://localhost:8761`.
- Services use Feign clients to communicate with each other using Eureka service names (e.g., `@FeignClient(name = "booking-service")`).

**Requirements:**
- Java 21
- Maven 3.8+
- (Optional) IntelliJ IDEA or any preferred IDE

**Order of Running the Services:**
1. Config Server
2. Eureka Server
3. Booking Microservice
4. Vehicle Microservice
5. User-Context Microservice
6. Payment Microservice
7. Rating Microservice

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

All the Services will Automatically register with Eureka and fetch the Configuration from the Config Server(Service Names will be shown in the Eureka Dashboard as it is illustrated in the Screenshot below)

![Screenshot 2025-06-29 at 7 48 06 PM](https://github.com/user-attachments/assets/211dfbc2-2ecb-4f73-a21b-5404841bdf29)


**How to Stop the Services:**

If you started services using `mvn spring-boot:run`, you can stop them by:
- Pressing `Ctrl+C` in the terminal window
- Or closing the running terminal tab/window


 
