package vn.vnpt.api.service;

import vn.vnpt.api.dto.out.category.CategoryListOut;
import vn.vnpt.api.dto.out.subcategory.SubCategoryListOut;
import vn.vnpt.common.model.PagingOut;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    PagingOut<CategoryListOut> listAllCategory();

    Map<Object, List<SubCategoryListOut>> showMenu();
}
