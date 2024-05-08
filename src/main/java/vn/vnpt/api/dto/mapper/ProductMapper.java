package vn.vnpt.api.dto.mapper;

import org.mapstruct.Mapper;
import vn.vnpt.api.dto.out.product.ProductListOut;
import vn.vnpt.api.dto.out.recommend.RecommendProducts;

import java.util.List;

@Mapper
public interface ProductMapper extends DefaultMapper {

    List<RecommendProducts> toRecommendProducts(List<ProductListOut> productListOuts);
}

