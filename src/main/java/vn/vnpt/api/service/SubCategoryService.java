package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.subcategory.SubCategoryListOut;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;

public interface SubCategoryService {
    PagingOut<SubCategoryListOut> listAllSubCategories(SortPageIn sortPageIn);

    List<SubCategoryListOut> listSubcategoryByCategory(String categoryId);
}
