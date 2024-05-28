package vn.hust.api.service;

import vn.hust.api.dto.out.category.CategoryListOut;
import vn.hust.api.dto.out.subcategory.SubCategoryListOut;
import vn.hust.common.model.PagingOut;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    PagingOut<CategoryListOut> listAllCategory();

    Map<Object, List<SubCategoryListOut>> showMenu();
}
