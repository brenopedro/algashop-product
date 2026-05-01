package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/products/2165e8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f")
    }

    response {
        status 404
        headers {
            contentType "application/problem+json"
        }
        body([
                instance: fromRequest().path(),
                type    : "/errors/not-found",
                title   : "Not found"
        ])
    }
}
