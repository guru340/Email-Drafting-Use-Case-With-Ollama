# Email Drafting with Ollama

A Spring Boot application that demonstrates how to build an AI-powered email drafting service using **Spring AI** and **Ollama**. The application runs a local Large Language Model (LLM) to generate professional, concise, and context-aware email drafts from natural-language instructions.

The project uses the `mistral:7b` model through Ollama and exposes a REST API for generating email content.

---

## Overview

Writing professional emails can be repetitive and time-consuming. This project demonstrates how a locally hosted LLM can be integrated into a Spring Boot application to automate the initial email drafting process.

The application accepts a natural-language request such as:

> Write a professional email requesting two days of leave from my manager next week.

The request is processed by the Ollama-hosted Mistral model and returned as a professionally formatted email.

The project is designed as a practical example of integrating local LLMs with Spring AI.

---

## Key Features

* AI-powered email generation
* Local LLM execution using Ollama
* Spring AI integration with `ChatClient`
* REST API for email generation
* Configurable Ollama model
* Configurable temperature and token limits
* Custom Ollama Modelfile for email-specific instructions
* Maven-based Spring Boot project

---

## Technology Stack

| Technology     | Version / Configuration |
| -------------- | ----------------------- |
| Java           | 25                      |
| Spring Boot    | 4.1.1                   |
| Spring AI      | 2.0.1                   |
| Ollama         | Local instance          |
| Language Model | Mistral 7B              |
| Build Tool     | Maven                   |
| API Style      | REST                    |

---

## Architecture

The application follows a simple request-response architecture:

```text
Client
  |
  | HTTP POST
  v
Spring Boot REST Controller
  |
  | Spring AI ChatClient
  v
Spring AI
  |
  | Ollama API
  v
Ollama
  |
  | Mistral 7B
  v
Generated Email Draft
```

### Request Flow

1. The client sends an email drafting request to the REST API.
2. The Spring Boot controller receives the request.
3. Spring AI's `ChatClient` prepares the prompt.
4. The request is sent to the locally running Ollama instance.
5. The Mistral 7B model generates the email.
6. The generated email is returned to the client as a plain-text response.

---

## Project Structure

```text
Email-Drafting-Use-Case-With-Ollama/
└── Email-Drafting-Use-Case-With-Ollama/
    ├── pom.xml
    ├── mvnw
    ├── mvnw.cmd
    │
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── com/example/Email/Drafting/Use/Case/With/Ollama/
        │   │       ├── Confurigation/
        │   │       │   └── OllamaConfurigation.java
        │   │       │
        │   │       ├── Controller/
        │   │       │   └── OllamaController.java
        │   │       │
        │   │       └── EmailDraftingUseCaseWithOllamaApplication.java
        │   │
        │   └── resources/
        │       └── application.yaml
        │
        ├── ollamafile/
        │   └── custom-mistral-modelfile
        │
        └── test/
            └── java/
```

---

## Prerequisites

The following software is required:

* Java 25 or later
* Git
* Ollama
* Mistral 7B model

### Verify Java

```bash
java -version
```

The project is configured for Java 25.

### Install Ollama

Install Ollama from the official website:

https://ollama.com/

Verify the installation:

```bash
ollama --version
```

---

## Ollama Setup

This project uses the `mistral:7b` model.

Pull the model using:

```bash
ollama pull mistral:7b
```

Verify that the model is available:

```bash
ollama list
```

The application expects Ollama to be available at:

```text
http://localhost:11434
```

Make sure the Ollama service is running before starting the Spring Boot application.

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/guru340/Email-Drafting-Use-Case-With-Ollama.git
```

Navigate to the Spring Boot project:

```bash
cd Email-Drafting-Use-Case-With-Ollama/Email-Drafting-Use-Case-With-Ollama
```

### Start the Application

Using the Maven Wrapper on Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

---

# REST API

## Draft Email

Generates a professional email based on the provided user input.

### Endpoint

```http
POST /api/ollama/chat/draft-email
```

### Content Type

```text
text/plain
```

### Request Body

The request body should contain the context or instructions describing the email that needs to be generated.

### Example Request

```bash
curl -X POST "http://localhost:8080/api/ollama/chat/draft-email" \
  -H "Content-Type: text/plain" \
  --data "Write a professional email asking my manager for two days of leave next week."
```

### Example Response

```text
Subject: Leave Request for Next Week

Dear [Manager Name],

I would like to request two days of leave next week due to a personal commitment. Please let me know if you require any additional information.

Thank you for your consideration.

Best regards,
[Your Name]
```

The exact response will depend on the model and the input provided.

---

# Implementation

## Ollama Configuration

The application creates a Spring AI `ChatClient` using the configured `OllamaChatModel`.

The configuration is defined in:

```text
src/main/java/com/example/Email/Drafting/Use/Case/With/Ollama/Confurigation/OllamaConfurigation.java
```

Conceptually, the configuration follows:

```java
@Bean("ollamaChatClient")
ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
    return ChatClient.builder(ollamaChatModel).build();
}
```

This allows the application to use Spring AI's `ChatClient` abstraction for communicating with Ollama.

---

## Email Drafting Controller

The REST endpoint is implemented in:

```text
src/main/java/com/example/Email/Drafting/Use/Case/With/Ollama/Controller/OllamaController.java
```

The controller exposes:

```http
POST /api/ollama/chat/draft-email
```

The endpoint receives the user's input and sends it to the configured `ChatClient`.

A system prompt is applied to guide the model toward generating:

* Professional emails
* Concise responses
* Clear communication
* Polite language
* Formal tone
* Context-specific content

---

# Configuration

The application's configuration is located at:

```text
src/main/resources/application.yaml
```

Current configuration:

```yaml
spring:
  application:
    name: Email-Drafting-Use-Case-With-Ollamaollama

  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: mistral:7b
          temperature: 0.7
          num-predict: 512

server:
  port: 8080
```

### Configuration Properties

| Property                                    | Description                                    |
| ------------------------------------------- | ---------------------------------------------- |
| `spring.ai.ollama.base-url`                 | URL of the Ollama server                       |
| `spring.ai.ollama.chat.options.model`       | Ollama model used for generation               |
| `spring.ai.ollama.chat.options.temperature` | Controls the randomness of generated content   |
| `spring.ai.ollama.chat.options.num-predict` | Maximum number of tokens generated             |
| `server.port`                               | Port on which the Spring Boot application runs |

---

# Custom Ollama Model

The repository includes a custom Ollama Modelfile:

```text
src/ollamafile/custom-mistral-modelfile
```

The Modelfile is based on:

```text
mistral:7b
```

It configures the model specifically for professional email drafting.

The current Modelfile contains parameters such as:

```text
FROM mistral:7b

PARAMETER temperature 0.7
PARAMETER num_ctx 512
```

It also defines a system instruction that guides the model to produce professional, concise, clear, polite, and context-aware emails.

---

## Creating the Custom Model

After pulling the base model, create the custom model:

```bash
ollama create email-drafter -f src/ollamafile/custom-mistral-modelfile
```

Verify that the model was created:

```bash
ollama list
```

You should see the newly created model:

```text
email-drafter
```

---

## Using the Custom Model

To use the custom model instead of `mistral:7b`, update `application.yaml`:

```yaml
spring:
  ai:
    ollama:
      chat:
        options:
          model: email-drafter
```

Restart the Spring Boot application after changing the configuration.

---

# Dependencies

The project uses the following primary dependencies:

### Spring Boot Web MVC

Provides the REST API and web application infrastructure.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

### Spring AI Ollama

Provides integration between Spring AI and Ollama.

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>
```

---

# Testing

The project contains a test source directory under:

```text
src/test/java
```

Run the test suite using:

### Linux / macOS

```bash
./mvnw test
```

### Windows

```powershell
mvnw.cmd test
```

---

# Troubleshooting

## Ollama Connection Refused

If the application cannot connect to Ollama, verify that Ollama is running.

The configured endpoint is:

```text
http://localhost:11434
```

Check the available models:

```bash
ollama list
```

---

## Mistral Model Not Found

If `mistral:7b` is unavailable, run:

```bash
ollama pull mistral:7b
```

Then verify:

```bash
ollama list
```

---

## Java Version Error

Check the installed Java version:

```bash
java -version
```

The project specifies Java 25.

---





# License

No license file is currently included in this repository.

If this project is intended for public distribution, consider adding an appropriate open-source license.

---

# Author

**guru340**

GitHub Repository:

https://github.com/guru340/Email-Drafting-Use-Case-With-Ollama

---

## Acknowledgements

This project uses:

* Spring Boot
* Spring AI
* Ollama
* Mistral
* Maven
