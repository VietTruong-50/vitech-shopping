package vn.hust.api.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import vn.hust.api.dto.out.product.ProductListOut;
import vn.hust.api.dto.out.recommend.RecommendProducts;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-04T23:49:45+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Private Build)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public List<RecommendProducts> toRecommendProducts(List<ProductListOut> productListOuts) {
        if ( productListOuts == null ) {
            return null;
        }

        List<RecommendProducts> list = new ArrayList<RecommendProducts>( productListOuts.size() );
        for ( ProductListOut productListOut : productListOuts ) {
            list.add( productListOutToRecommendProducts( productListOut ) );
        }

        return list;
    }

    protected RecommendProducts productListOutToRecommendProducts(ProductListOut productListOut) {
        if ( productListOut == null ) {
            return null;
        }

        RecommendProducts recommendProducts = new RecommendProducts();

        recommendProducts.setProductId( productListOut.getProductId() );
        recommendProducts.setProductCode( productListOut.getProductCode() );
        recommendProducts.setName( productListOut.getName() );
        recommendProducts.setFeatureImageLink( productListOut.getFeatureImageLink() );
        recommendProducts.setActualPrice( productListOut.getActualPrice() );
        recommendProducts.setPrice( productListOut.getPrice() );

        return recommendProducts;
    }
}
