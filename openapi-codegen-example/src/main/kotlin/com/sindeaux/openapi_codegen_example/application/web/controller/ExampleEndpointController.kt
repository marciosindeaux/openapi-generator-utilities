package com.sindeaux.openapi_codegen_example.application.web.controller

import com.sindeaux.openapi_codegen_example.application.web.apis.ExampleEndpointApi
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleEndpointController() : ExampleEndpointApi {

    override fun exampleMethod() : ResponseEntity<Void> {
        print("You have a request for your endpoint in method exampleMethod()")
        return ResponseEntity.ok().build()
    }
}