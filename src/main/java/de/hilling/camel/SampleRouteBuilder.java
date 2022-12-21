package de.hilling.camel;

import org.apache.camel.builder.RouteBuilder;

public class SampleRouteBuilder extends RouteBuilder {

    @Override
    public void configure()  {
        from("ftp://localhost:21721/?include=file.*txt")
        .routeId("sampleRoute")
        .log("File: ${header.CamelFileName}")
        .to("file:samples/");
    }
}
