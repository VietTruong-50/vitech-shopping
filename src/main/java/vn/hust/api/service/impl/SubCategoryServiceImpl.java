package vn.hust.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hust.api.dto.out.subcategory.SubCategoryListOut;
import vn.hust.api.repository.SubCategoryRepository;
import vn.hust.api.service.SubCategoryService;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public PagingOut<SubCategoryListOut> listAllSubCategories(SortPageIn sortPageIn) {
        return subCategoryRepository.getAllSubCategories(sortPageIn);
    }

    @Override
    public List<SubCategoryListOut> listSubcategoryByCategory(String categoryId) {
        return subCategoryRepository.listAllByCategory(new SortPageIn(), categoryId).getData();
    }

}
