package vn.hust.api.dto.mapper;

import org.mapstruct.Mapper;
import vn.hust.api.dto.out.product.ProductListOut;
import vn.hust.api.dto.out.recommend.RecommendProducts;

import java.util.List;

@Mapper
public interface ProductMapper extends DefaultMapper {

    List<RecommendProducts> toRecommendProducts(List<ProductListOut> productListOuts);
}

