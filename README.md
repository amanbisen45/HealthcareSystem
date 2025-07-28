HealthcareSystem Application
This is a Java-based web application built to manage healthcare services for patients, doctors, and admins. It allows users to handle appointments, upload and access 
health records securely, and manage roles efficiently. The project uses Spring Boot and integrates with Google Drive to store patient health records.

I) Features:

1) Patient
-Register and log in
-Book appointments with doctors
-Upload and view personal health records (stored securely on Google Drive)

2) Doctor
-View and manage appointment requests
-Access health records of patients who have confirmed appointments

3) Admin
-Manage users (patients and doctors)
-View all appointments
-Access all uploaded health records

4) Google Drive Integration
-Health records are uploaded to Google Drive

5) Access is restricted based on role and appointment status

6) Only authorized users (doctors/admins) can view or download records

II) Security:

1) User authentication and role-based access control are implemented

2)Passwords are securely stored using hashing

3) credentials.json for Google Drive access is ignored in Git and has been removed from commit history

III) Tech Stack:
-Java with Spring Boot
-HTML and Thymeleaf
-MySQL Database
-Google Drive API
-Maven Build Tool

IV) How to Run:
1) Clone the repository or download the ZIP file

2) Set up the MySQL database with required schema and tables

3) Create your own Google Cloud project and generate a credentials.json file

4) Place the credentials.json inside src/main/resources/

5) Open the project in your IDE (Eclipse or IntelliJ) and run the application

V) Note:
Make sure you do not upload your credentials.json file to GitHub. It's sensitive and must remain local and secure.

VI) Project Contributors:
-Aman Bisen
-Aditi Sandbhor
