package contracts.product;

import org.springframework.cloud.contract.spec.Contract;

Contract.make {
  request {
    method GET() 
    headers {
      accept 'application/json'
    }
    url("/api/v1/products/fffee8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f")
  }

  response {
    status 200
    headers {
      contentType 'applicationJson'
    }
    body([
      id: fromRequest().path(3),
      addedAt: anyIso8601WithOffset(),
      name: "Notebook X11",
      brand: "Deep Diver",
      regularPrice: 1500.00,
      salePrice: 1000.00,
      inStock: false,
      enabled: true,
      categoryId: anyUuid(),
      description: "A gamer notebook"
    ])
  }
}
