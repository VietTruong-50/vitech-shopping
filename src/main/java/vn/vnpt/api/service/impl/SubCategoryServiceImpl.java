package vn.vnpt.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.vnpt.api.dto.out.subcategory.SubCategoryListOut;
import vn.vnpt.api.repository.SubCategoryRepository;
import vn.vnpt.api.service.SubCategoryService;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

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
