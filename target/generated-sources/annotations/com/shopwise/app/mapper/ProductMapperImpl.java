package com.shopwise.app.mapper;

import com.shopwise.app.dto.request.CreateProductRequest;
import com.shopwise.app.dto.request.UpdateProductRequest;
import com.shopwise.app.dto.response.ProductResponse;
import com.shopwise.app.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T11:06:04+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Ubuntu)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(CreateProductRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setName( dto.getName() );
        product.setDescription( dto.getDescription() );
        product.setPrice( dto.getPrice() );

        return product;
    }

    @Override
    public void updateEntity(UpdateProductRequest dto, Product entity) {
        if ( dto == null ) {
            return;
        }

        entity.setName( dto.getName() );
        entity.setDescription( dto.getDescription() );
        entity.setPrice( dto.getPrice() );
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setId( product.getId() );
        productResponse.setName( product.getName() );
        productResponse.setDescription( product.getDescription() );
        productResponse.setPrice( product.getPrice() );
        productResponse.setCreatedAt( product.getCreatedAt() );
        productResponse.setUpdatedAt( product.getUpdatedAt() );

        return productResponse;
    }

    @Override
    public List<ProductResponse> toResponseList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( products.size() );
        for ( Product product : products ) {
            list.add( toResponse( product ) );
        }

        return list;
    }
}
