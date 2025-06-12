package com.sindeaux.openapi_codegen_example.application.web.controller

import com.sindeaux.openapi_codegen_example.application.web.apis.ExampleEndpointApi
import org.springframework.http.ResponseEntity

class ExampleEndpointController() : ExampleEndpointApi {

    override fun exampleMethod() : ResponseEntity<Unit> {
        print("You have a request for your endpoint in method exampleMethod()")
        return ResponseEntity.ok().build()
    }
}