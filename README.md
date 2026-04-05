# Email SMTP Spring Boot

## Project Overview
This project is a Spring Boot application that provides a simple way to send emails using SMTP. It showcases how to set up email services in Java and integrate them with a Spring Boot application.

## Features
- Send emails with text and HTML content.
- Supports attachments.
- Configurable SMTP server settings.
- Easy integration with Spring Boot.

## Prerequisites
- JDK 11 or higher
- Maven or Gradle
- An SMTP server (e.g., Gmail, SendGrid, etc.)

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Rakesh-Routhu/email-smtp-springboot.git
   ```
2. Navigate into the project directory:
   ```bash
   cd email-smtp-springboot
   ```
3. Install the dependencies:
   ```bash
   mvn install  # for Maven
   # or
   ./gradlew build  # for Gradle
   ```

## Configuration
1. Open the `application.properties` file located in `src/main/resources/`.
2. Update the following properties with your SMTP server details:
   ```properties
   spring.mail.host=smtp.example.com
   spring.mail.port=587
   spring.mail.username=your-email@example.com
   spring.mail.password=your-email-password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

## API Endpoints
- **Send Email**: `/api/send-email`
  - Method: `POST`
  - Request body:
    ```json
    {
      "to": "recipient@example.com",
      "subject": "Email Subject",
      "body": "Email body content"
    }
    ```

## Usage Examples
- To send an email:
   Use Postman or any HTTP client to post to `/api/send-email`. The request should contain the required JSON payload as shown above.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author
Rakesh Routhu