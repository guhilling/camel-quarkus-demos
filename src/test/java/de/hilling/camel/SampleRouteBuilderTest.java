package de.hilling.camel;

import io.quarkus.test.junit.QuarkusTest;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SampleRouteBuilderTest extends CamelQuarkusTestSupport {

    @Produce("direct:ftp")
    protected ProducerTemplate template;

    @EndpointInject("mock:file:sample_requests")
    protected MockEndpoint fileMock;

    @Override
    protected RoutesBuilder[] createRouteBuilders() {
        return new RoutesBuilder[] {
            new SampleRouteBuilder(),
            new SecondRouteBuilder()
        };
    }

    @BeforeEach
    protected void doAdvice() throws Exception {
        AdviceWith.adviceWith(this.context, "sampleRoute",
                              SampleRouteBuilderTest::enhanceRoute);
    }

    @Test
    void testConsumeFtpWriteToFileOne() throws Exception {
        fileMock.message(0).body().isEqualTo("Hello World");
        template.sendBody("direct:ftp", "Hello World");
        fileMock.assertIsSatisfied();
    }

    @Test
    void testConsumeFtpWriteToFileTwo() throws Exception {
        fileMock.message(0).body().isEqualTo("Hello World");
        template.sendBody("direct:ftp", "Hello World");
        fileMock.assertIsSatisfied();
    }

    private static void enhanceRoute(AdviceWithRouteBuilder route) {
        route.replaceFromWith("direct:ftp");
        route.interceptSendToEndpoint("file:.*samples.*")
             .skipSendToOriginalEndpoint()
             .to("mock:file:sample_requests");
    }

}
