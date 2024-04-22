package vn.vnpt.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.vnpt.api.dto.out.category.CategoryListOut;
import vn.vnpt.api.repository.helper.ProcedureCallerV3;
import vn.vnpt.api.repository.helper.ProcedureParameter;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CategoryRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public PagingOut<CategoryListOut> listAllCategories(SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("category_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), CategoryListOut.class
        );

        List<CategoryListOut> categoryListOuts =  (List<CategoryListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, categoryListOuts);
    }
}
