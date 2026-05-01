package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        headers {
            accept 'application/json'
        }
        urlPath("/api/v1/categories/fffee8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f")

        response {
            status 204
        }
    }
}
