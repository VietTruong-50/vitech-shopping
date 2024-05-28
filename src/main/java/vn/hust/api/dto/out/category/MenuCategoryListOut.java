package vn.hust.api.dto.out.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.hust.api.dto.out.subcategory.SubCategoryListOut;

@Data
@AllArgsConstructor
public class MenuCategoryListOut {
    private CategoryListOut categoryListOut;
    private SubCategoryListOut[] subCategoryListOuts;
}
