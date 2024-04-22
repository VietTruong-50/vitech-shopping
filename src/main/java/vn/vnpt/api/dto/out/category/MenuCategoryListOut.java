package vn.vnpt.api.dto.out.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.vnpt.api.dto.out.subcategory.SubCategoryListOut;

import java.util.List;

@Data
@AllArgsConstructor
public class MenuCategoryListOut {
    private CategoryListOut categoryListOut;
    private SubCategoryListOut[] subCategoryListOuts;
}
