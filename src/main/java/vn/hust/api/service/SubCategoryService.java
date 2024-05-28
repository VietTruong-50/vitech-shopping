package vn.hust.api.service;

import vn.hust.api.dto.out.subcategory.SubCategoryListOut;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;

public interface SubCategoryService {
    PagingOut<SubCategoryListOut> listAllSubCategories(SortPageIn sortPageIn);

    List<SubCategoryListOut> listSubcategoryByCategory(String categoryId);
}
