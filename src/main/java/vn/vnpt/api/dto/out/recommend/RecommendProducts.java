package vn.vnpt.api.dto.out.recommend;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class RecommendProducts {
    @Col("product_id")
    private String productId;
    @Col("product_code")
    private String productCode;
    @Col("name")
    private String name;
    @Col("feature_image_link")
    private String featureImageLink;
    @Col("actual_price")
    private Long actualPrice;
    @Col("price")
    private Long price;
}
