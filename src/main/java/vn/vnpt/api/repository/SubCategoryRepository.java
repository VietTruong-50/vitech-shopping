package vn.vnpt.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.vnpt.api.dto.out.subcategory.SubCategoryListOut;
import vn.vnpt.api.repository.helper.ProcedureCallerV3;
import vn.vnpt.api.repository.helper.ProcedureParameter;
import vn.vnpt.common.model.PagingOut;
import vn.vnpt.common.model.SortPageIn;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class SubCategoryRepository {
    private final ProcedureCallerV3 procedureCallerV3;
    public PagingOut<SubCategoryListOut> getAllSubCategories(SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("subcategory_list",
                List.of(
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), SubCategoryListOut.class
        );

        List<SubCategoryListOut> outList = (List<SubCategoryListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public PagingOut<SubCategoryListOut> listAllByCategory(SortPageIn sortPageIn, String categoryId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("subcategory_list_by_category",
                List.of(
                        ProcedureParameter.inputParam("prs_category_id", String.class, categoryId),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), SubCategoryListOut.class
        );

        List<SubCategoryListOut> outList = (List<SubCategoryListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

}
