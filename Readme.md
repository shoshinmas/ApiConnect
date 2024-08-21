Sure! Here's a sample `README.md` file for your project:

---

# GitHub Repository Fetcher

This is a Spring Boot application that fetches GitHub repositories for a given user, filters out forked repositories, and returns branch information along with the last commit SHA. The application can also save the fetched data as an XML file.

## Features

- **Fetch Repositories:** Given a GitHub username, fetch all non-forked repositories.
- **Branch Information:** For each repository, display branch names and the last commit SHA.
- **Save as XML:** Option to save the fetched data as an XML file.
- **Error Handling:** Returns a `404` status with a custom message if the user does not exist.

## Prerequisites

- Java 21
- Maven 3.6+
- A valid GitHub API token (if you need to connect to the GitHub API).

## Getting Started

### Clone the repository

```bash
git clone https://github.com/yourusername/github-repo-fetcher.git
cd github-repo-fetcher
```

### Configuration

Edit the `application.properties` file located in `src/main/resources/`:

```properties
spring.application.name=github-repo-fetcher
github.api.url=https://api.github.com/users/{username}/repos
server.port=8080
```

If you have a GitHub API token, add it as well:

```properties
github.api.token=your_github_token_here
```

### Build and Run

Use Maven to build the application:

```bash
mvn clean install
```

Run the application:

```bash
mvn spring-boot:run
```

### API Endpoints

- **Fetch Repositories:** `GET /api/github/{username}/repositories`
    - Fetches all non-forked repositories of the specified GitHub user.

- **Download and Save as XML:** `GET /api/github/{username}/repositories/download`
    - Fetches repositories and saves the data as an XML file.

### Example Request

Fetch repositories for user `shoshinmas`:

```bash
curl -H "Accept: application/json" http://localhost:8080/api/github/shoshinmas/repositories
```

### Example Response

```json
[
    {
        "name": "repo-name",
        "owner": "shoshinmas",
        "branches": [
            {
                "name": "main",
                "lastCommitSha": "abc123def456"
            }
        ]
    }
]
```

## Testing

The project includes comprehensive unit tests for the services and controllers.

### Running Tests

To run the tests, use:

```bash
mvn test
```

The tests cover:

- **GitHubService Test:** Ensures the correct processing of repository data and handling of various scenarios like missing users.
- **JsonService Test:** Tests JSON structure creation and XML export functionality.
- **Controller Test:** Ensures the API behaves as expected, with both successful responses and error handling.

## Troubleshooting

- **404 Not Found:** If you get a 404 response, ensure that the username exists on GitHub.
- **401 Unauthorized:** If you're using the GitHub API token, ensure it's correctly configured in `application.properties`.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License.