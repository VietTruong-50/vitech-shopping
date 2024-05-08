package vn.vnpt.api.dto.out.product;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

import java.util.Map;

@Data
public class ProductDetailOut {
    @Col("product_id")
    private String id;
    @Col("name")
    private String name;
    @Col("product_code")
    private String productCode;
    @Col("description")
    private String description;
    @Col("summary")
    private String summary;
    @Col("price")
    private Long price;
    @Col("actual_price")
    private Long actualPrice;
    @Col("feature_image_link")
    private String featureImageLink;
    @Col("subcategory_name")
    private String subCategoryName;
    @Col("subcategory_id")
    private String subCategoryId;
    @Col("category_name")
    private String categoryName;
    @Col("category_id")
    private String categoryId;
    @Col("parameters")
    private String parametersJson;
    @Col("status")
    private Integer status;
    @Col("image_links")
    private String imageLinks;

    private Map<String, Object> parameters;
}
