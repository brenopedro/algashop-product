package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/products/fffee8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f/enable")

        response {
            status 204
        }
    }
}
