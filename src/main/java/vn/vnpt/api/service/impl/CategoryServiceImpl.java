package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.out.category.CategoryListOut;
import vn.vnpt.api.dto.out.subcategory.SubCategoryListOut;
import vn.vnpt.api.repository.CategoryRepository;
import vn.vnpt.api.repository.SubCategoryRepository;
import vn.vnpt.api.service.CategoryService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

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


