package ru.otus.exchange.receiver.api.v1.swagger;

import static ru.otus.exchange.common.Constants.REQUEST_ID;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Tag(name = "Book API", description = "It just saying hello!")
@Api(value = "/receiver/v1")
public interface ReceiverSwaggerApi {

    @POST
    @Path("/")
    @Consumes("application/xml;charset=utf-8")
    @ApiOperation(value = "message receive message", tags = "receiver")
    void receive(
            @ApiParam(value = "request identifier", required = true) @HeaderParam(REQUEST_ID) String requestId,
            @ApiParam(
                            value = "input message",
                            required = true,
                            defaultValue = "contend defined by soapenv-exchange.xsd")
                    @RequestBody
                    byte[] message);
}
