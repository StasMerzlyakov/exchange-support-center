package ru.otus.exchange.receiver.rest;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static ru.otus.exchange.common.Constants.REQUEST_ID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "receiver")
public interface ReceiverSwaggerApi {


    @Operation(summary = "message receiver", description = "receive and store message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully retrieved message", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    void receive(
            @Parameter(required = true,
                    description = "request identifier",
                    example = "12345678-1234-5678-1234-567812345678",
                    name = REQUEST_ID,
                    in = HEADER)
            String requestId,
            @RequestBody(required = true,
                    description = "contend defined by soapenv-exchange.xsd",
                    content = @Content(mediaType = "application/xml;charset=utf-8"))
            byte[] message);
}
