package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method PUT()
        url "/api/v1/categories/fffee8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f"
        headers {
            contentType 'application/json'
            accept 'application/json'
        }
        body(
                name    : value(
                        test("Electronics"),
                        stub(nonBlank())
                ),
                enabled : value(
                        test(true),
                        stub(anyBoolean())
                )
        )
    }
    response {
        status 200
        headers {
            contentType 'application/json'
        }
        body(
                id      : fromRequest().path(3),
                name    : fromRequest().body('$.name'),
                enabled : fromRequest().body('$.enabled')
        )
    }
}
