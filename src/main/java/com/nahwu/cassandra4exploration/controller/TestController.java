package com.nahwu.cassandra4exploration.controller;

import com.nahwu.cassandra4exploration.entity.TestDbObject;
import com.nahwu.cassandra4exploration.entity.TestRequest;
import com.nahwu.cassandra4exploration.service.TestService2Impl;
import com.nahwu.cassandra4exploration.service.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestServiceImpl testService;

    @Autowired
    private TestService2Impl testService2;

    @PostMapping("/v1/test/api/echo")
    @Operation(summary = "Test API and reply with the same request payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API working",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestDbObject.class))})})
    public ResponseEntity<?> testApiEcho(
            @RequestBody TestRequest testRequest) {
        return new ResponseEntity<>(testRequest, HttpStatus.OK);
    }

    @PostMapping("/v1/test/api/length")
    @Operation(summary = "Test API and reply with the request length")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API working",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))})})
    public ResponseEntity<?> testApiLength(
            @RequestBody TestRequest testRequest) {
        return new ResponseEntity<>(testRequest.getPayload().length(), HttpStatus.OK);
    }

    @PostMapping("/v1/test/cassandra/test")
    @Operation(summary = "Test Cassandra API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API working",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))})})
    public ResponseEntity<?> testCassandra() {
        Iterable<TestDbObject> books = testService.insertAndSelectObjects();
        logger.info(books.iterator().toString());

        Iterable<TestDbObject> books2 = testService2.insertAndSelectObjects();
        logger.info(books2.iterator().toString());

        return new ResponseEntity<>("Got a response!", HttpStatus.OK);
    }

    @PostMapping("/v1/test/cassandra/mass-insert")
    @Operation(summary = "Test Cassandra API - Mass insertion of data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API working",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))})})
    public ResponseEntity<?> writeLotsOfDataToCassandra(
            @RequestBody TestRequest testRequest) {
        testService.writeLotsOfData(testRequest.getInsertionSize());
        return new ResponseEntity<>("Written!", HttpStatus.OK);
    }
}
