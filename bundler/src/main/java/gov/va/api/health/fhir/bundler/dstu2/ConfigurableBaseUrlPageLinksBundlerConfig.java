package gov.va.api.health.fhir.bundler.dstu2;

import gov.va.api.health.fhir.bundler.ConfigurableBaseUrlPageLinksConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This is a convenience class meant to import everything needed to apply a bundler service that
 * uses a configurable base url page links service to an application.
 */
@Configuration
@Import({
  Bundler.class,
  ConfigurableBaseUrlPageLinks.class,
  ConfigurableBaseUrlPageLinksConfig.class
})
public class ConfigurableBaseUrlPageLinksBundlerConfig {}
