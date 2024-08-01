# Ergonomic Workspace Calculator

### 🌟 Overview
  This Spring Boot application is a tool designed to create ergonomic workspace tailored to a person individual anthropometric data.
By inputting specific, individual measurements, calculator generates a customized workspace layout and produces a PDF file detailing optimal ergonomic set up.

  This should be a help for those users who spent most of their productive time at the desk sitting or standing.

### 🚀 Features
- Anthropometric Data Input: Users can input their personal body measurements. 

  <img src="https://github.com/user-attachments/assets/f48036eb-529e-4233-811f-a66e6e9fdfe4" alt="Dizainas be pavadinimo"/>

- Workspace Creation: Based on provided anthropometric measures, ergonomic workspace is calculated.

- PDF Generation: Using generated workspace metrics object PDF of workplace picture and important information is generated in PDF.

- Login and Registration: User can change password, update their anthropometric data and check their workspace PDF anytime after their register an account.

### 🛠️ Technologies Used
- Java
- Spring Boot
- Hibernate
- Spring Security with JWT
- PostgreSQL
- MapStruct
- Spring Data JPA
- Controller Advice
- Unit Testing
- Mockito
- Apache PDFBox
- Lombok

### 🧰 Setup and Installation

#### Prerequisites using DOCKER

- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/install/)

#### Steps to run using DOCKER

 - Click the link to download the setup script:
[Download setup.sh]

 - Open your terminal and navigate to the directory where the script was downloaded: "cd /path/to/sh/directory"
 - Then run: "chmod +x setup.sh"
 - Then run: "./setup.sh"
   
   The script attempts to automatically open the frontend application in your default web browser at http://localhost:4200.

#### Prerequisites using IDEA
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
  - JDK 11
  - [Git](https://git-scm.com/)
  - [Node.js and npm](https://nodejs.org/en/download/)
  - [Angular CLI](https://angular.io/cli) (install with `npm install -g @angular/cli`)

  #### Steps to run using IDE
  ###### Back-end:
 - Clone the repository "git clone https://github.com/godaasinskaite/ErgonomicCalculator" (backend)
 - Open project and update the PostgreSQL configuration with your database details:

<img width="50%" alt="pav" src="https://github.com/user-attachments/assets/4ec79343-cd79-49f5-8297-40e301e8f8c5">

  ###### Front-end:
  - Clone the repository "git clone https://github.com/godaasinskaite/ergonomic-calculator" (frontend)
  - Run the application with Maven "mvn clean spring-boot:run"
  - Open your terminal and navigate to the front-end project directory: "cd /path/to/your/frontend"
  - Instal required dependencies: "npm install"
  - Then run the application: "ng serve -o"

 #### Front-end part of this project is built with Angular and styled using Bootstrap framework. For more information: https://github.com/godaasinskaite/ergonomic-calculator

 ## 🎓 Credits:
 I am deeply grateful for the guidance and support from my mentor, Edvinas Prokofijovas, throughout this final project, which marks the results of my software development course at https://www.kodokelias.lt/
