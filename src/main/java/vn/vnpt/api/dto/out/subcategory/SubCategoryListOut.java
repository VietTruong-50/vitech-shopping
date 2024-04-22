package vn.vnpt.api.dto.out.subcategory;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class SubCategoryListOut {
    @Col("subcategory_id")
    private String subCategoryId;
    @Col("subcategory_name")
    private String name;
    @Col("icon")
    private String icon;
}
