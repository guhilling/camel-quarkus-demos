package de.hilling.camel;

import org.apache.camel.builder.RouteBuilder;

public class ThirdRouteBuilder extends RouteBuilder {

    @Override
    public void configure()  {
        from("ftp://localhost:21722/?include=file.*txt")
        .routeId("secondRoute")
        .log("File: ${header.CamelFileName}")
        .to("file:samples/");
    }
}
