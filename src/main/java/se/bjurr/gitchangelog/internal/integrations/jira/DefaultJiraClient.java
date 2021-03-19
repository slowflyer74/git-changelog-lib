package se.bjurr.gitchangelog.internal.integrations.jira;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static java.util.concurrent.TimeUnit.MINUTES;

import com.google.common.base.Optional;
import java.util.Map;
import se.bjurr.gitchangelog.api.exceptions.GitChangelogIntegrationException;
import se.bjurr.gitchangelog.internal.integrations.rest.RestClient;

public class DefaultJiraClient extends JiraClient {

  private RestClient client;

  public DefaultJiraClient(String api) {
    super(api);
    this.client = new RestClient(1, MINUTES);
  }

  @Override
  public JiraClient withBasicCredentials(String username, String password) {
    this.client = client.withBasicAuthCredentials(username, password);
    return this;
  }

  @Override
  public JiraClient withTokenCredentials(String token) {
    this.client = client.withTokenAuthCredentials(token);
    return this;
  }

  @Override
  public JiraClient withHeaders(Map<String, String> headers) {
    this.client = client.withHeaders(headers);
    return this;
  }

  @Override
  public Optional<JiraIssue> getIssue(String issue) throws GitChangelogIntegrationException {
    String endpoint = getEndpoint(issue);
    Optional<String> json = client.get(endpoint);
    if (json.isPresent()) {
      JiraIssue jiraIssue = toJiraIssue(issue, json.get());
      return of(jiraIssue);
    }
    return absent();
  }

  public JiraClient withAdditionalFields(Map<String, String> fields) {
    return this;
  }
}
