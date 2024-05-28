package vn.hust.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.out.category.CategoryListOut;
import vn.hust.api.dto.out.subcategory.SubCategoryListOut;
import vn.hust.api.repository.CategoryRepository;
import vn.hust.api.repository.SubCategoryRepository;
import vn.hust.api.service.CategoryService;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public PagingOut<CategoryListOut> listAllCategory() {
        return categoryRepository.listAllCategories(new SortPageIn());
    }

    @Override
    public Map<Object, List<SubCategoryListOut>> showMenu() {
        Map<Object, List<SubCategoryListOut>> menu = new HashMap<>();
        List<CategoryListOut> categoryListOuts = listAllCategory().getData();

        for(var category: categoryListOuts){
            List<SubCategoryListOut> subCategoryListOuts = subCategoryRepository.listAllByCategory(new SortPageIn(), category.getCategoryId()).getData();

            menu.put(category, subCategoryListOuts);
        }

        return menu;
    }
}


