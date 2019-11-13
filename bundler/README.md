# bundler

The `Bundler` service is capable of producing type specific bundles for resources. It leverages supporting helper functions in a provided context to create new instances of specific bundle and entry types. Paging links are supported via an injectable `PageLinks`.

The `ConfigurableBaseUrlPageLinks` service is an implementation of the `PageLinks` interface used by the `Bundler`. 

The source is written for the `dstu2` resource.  However, other resources are generated from the `dstu2` implementation.  The following resources are supported:

* dstu2
* stu3
* r4

## Usage

The services must be imported into the application for the appropriate resource.

1. Add the appropriate dependency to the application pom.xml. 
   Note this example for `r4`:
   ```
   <dependency>
      <groupId>gov.va.api.health.fhir</groupId>
      <artifactId>bundler</artifactId>
      <classifier>r4</classifier>
      <version>${fhir-bundler.version}</version>
    </dependency>
   ```
2. Import the bundler service and any additional supporting service or configuration classes into the application. 

   NOTE: this example uses the provided `ConfigurableBaseUrlPageLinks` service.
   ```
   @Import({ConfigurableBaseUrlPageLinksBundlerConfig.class})
   ```
3. Apply the appropriate properties for the application. 
   
   NOTE: this example uses the provided `ConfigurableBaseUrlPageLinks` service.

   ```
   pageLinksBaseUrl=https://awesome.com
   pageLinksBasePath=api
   ```
4. Applications may then call the `Bundler` `bundle` method as appropriate.

## Known Issues/Concerns

1. The services within this library are based on `health-apis-data-query` but refactored for easier configuration.  Several applications appear to have a copy of these services.  Unfortunately, they may have slight differences.  It is recommended to refactor applications that happen to have their own copy of these services to use this common implementation and to enhance this implementation as necessary to support the desired use cases as appropriate.
