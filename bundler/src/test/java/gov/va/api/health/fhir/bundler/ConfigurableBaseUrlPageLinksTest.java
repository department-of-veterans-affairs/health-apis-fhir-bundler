package gov.va.api.health.fhir.bundler;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.dstu2.api.bundle.BundleLink;
import gov.va.api.health.dstu2.api.bundle.BundleLink.LinkRelation;
import gov.va.api.health.fhir.bundler.dstu2.ConfigurableBaseUrlPageLinks;
import gov.va.api.health.fhir.bundler.dstu2.PageLinks.LinkConfig;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {
      ConfigurableBaseUrlPageLinksTest.TestConfiguration.class,
      ConfigurableBaseUrlPageLinks.class
    },
    initializers = ConfigFileApplicationContextInitializer.class)
public class ConfigurableBaseUrlPageLinksTest {

  @Autowired private ConfigurableBaseUrlPageLinks links;

  @Test
  public void allLinksPresentWhenInTheMiddle() {
    List<BundleLink> actual = links.create(forCurrentPage(2, 3, 15));
    assertThat(actual)
        .containsExactlyInAnyOrder(
            link(LinkRelation.first, 1, 3),
            link(LinkRelation.prev, 1, 3),
            link(LinkRelation.self, 2, 3),
            link(LinkRelation.next, 3, 3),
            link(LinkRelation.last, 5, 3));
  }

  private LinkConfig forCurrentPage(int page, int count, int total) {
    return LinkConfig.builder()
        .path("Whatever")
        .queryParams(
            Parameters.builder()
                .add("a", "apple")
                .add("b", "banana")
                .add("page", String.valueOf(page))
                .add("_count", String.valueOf(count))
                .build())
        .page(page)
        .recordsPerPage(count)
        .totalRecords(total)
        .build();
  }

  private BundleLink link(LinkRelation relation, int page, int count) {
    return BundleLink.builder()
        .relation(relation)
        .url("https://awesome.com/api/Whatever?a=apple&b=banana&page=" + page + "&_count=" + count)
        .build();
  }

  @Test
  public void nextAndPreviousLinksOmittedWhenOnlyOnePage() {
    List<BundleLink> actual = links.create(forCurrentPage(1, 2, 2));
    assertThat(actual)
        .containsExactlyInAnyOrder(
            link(LinkRelation.first, 1, 2),
            link(LinkRelation.self, 1, 2),
            link(LinkRelation.last, 1, 2));
  }

  @Test
  public void nextAndPreviousLinksOmittedWhenRequestedPageIsNotKnown() {
    List<BundleLink> actual = links.create(forCurrentPage(99, 2, 4));
    assertThat(actual)
        .containsExactlyInAnyOrder(
            link(LinkRelation.first, 1, 2),
            link(LinkRelation.self, 99, 2),
            link(LinkRelation.last, 2, 2));
  }

  @Test
  public void nextLinkOmittedWhenOnLastPage() {
    List<BundleLink> actual = links.create(forCurrentPage(5, 3, 15));
    assertThat(actual)
        .containsExactlyInAnyOrder(
            link(LinkRelation.first, 1, 3),
            link(LinkRelation.prev, 4, 3),
            link(LinkRelation.self, 5, 3),
            link(LinkRelation.last, 5, 3));
  }

  @Test
  public void onlySelfLinkWhenCountIsZero() {
    List<BundleLink> actual = links.create(forCurrentPage(2, 0, 15));
    assertThat(actual).containsExactlyInAnyOrder(link(LinkRelation.self, 2, 0));
  }

  @Test
  public void previousLinkOmittedWhenOnFirstPage() {
    List<BundleLink> actual = links.create(forCurrentPage(1, 3, 15));
    assertThat(actual)
        .containsExactlyInAnyOrder(
            link(LinkRelation.first, 1, 3),
            link(LinkRelation.self, 1, 3),
            link(LinkRelation.next, 2, 3),
            link(LinkRelation.last, 5, 3));
  }

  @Test
  public void readLinkCombinesConfiguredUrl() {
    assertThat(links.readLink("Whatever", "123")).isEqualTo("https://awesome.com/api/Whatever/123");
  }

  /** Loads our properties file into a ConfigurableBaseUrlPageLinksConfig bean for the test. */
  @EnableConfigurationProperties(value = ConfigurableBaseUrlPageLinksConfig.class)
  public static class TestConfiguration {}
}
