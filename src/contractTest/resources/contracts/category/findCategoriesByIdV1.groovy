package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/categories/fffee8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f")
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                id     : anyUuid(),
                name   : "Electronics",
                enabled: true
        ])
    }
}