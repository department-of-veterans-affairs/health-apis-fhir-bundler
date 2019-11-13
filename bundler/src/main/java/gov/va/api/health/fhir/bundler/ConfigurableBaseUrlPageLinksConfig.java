package gov.va.api.health.fhir.bundler;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/** Configuration class used to configure the ConfigurableBaseUrlPageLinks service. */
@Data
@Configuration
@ConfigurationProperties
@Accessors(fluent = false)
public class ConfigurableBaseUrlPageLinksConfig implements InitializingBean {

  /**
   * The published URL for links which is likely not the hostname of the machine running this
   * application.
   */
  private String bundlerPageLinksBaseUrl;

  /** These base path for resources, e.g. api */
  private String bundlerPageLinksBasePath;

  /** Concatenated link url and path base that is concatenated further by the service. */
  private String pageLinksBase;

  @Override
  public void afterPropertiesSet() throws IllegalArgumentException {
    Assert.notNull(
        bundlerPageLinksBaseUrl,
        "ConfigurableBaseUrlPageLinksConfig bundlerPageLinksBaseUrl must not be null.");
    Assert.notNull(
        bundlerPageLinksBasePath,
        "ConfigurableBaseUrlPageLinksConfig bundlerPageLinksBasePath must not be null.");
    pageLinksBase =
        new StringBuilder(bundlerPageLinksBaseUrl)
            .append('/')
            .append(bundlerPageLinksBasePath)
            .append('/')
            .toString();
  }
}
