package com.sivalabs.bookstore.products;

import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Consumes("application/json")
@Produces("application/json")
@Path("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GET
    public List<Product> allProducts() {
        return productService.getAllProducts();
    }

    @GET()
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).status(Response.Status.OK).build();
    }

    @POST
    public Response createTodo(@Valid Product product) {
        return Response.ok(productService.createTodo(product))
                .status(Response.Status.CREATED)
                .build();
    }
}
