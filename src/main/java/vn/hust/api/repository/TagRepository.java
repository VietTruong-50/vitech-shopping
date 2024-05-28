package vn.hust.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.out.tag.TagListOut;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;

import java.util.List;
import java.util.Map;

@Repository
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class TagRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public List<TagListOut> listAllByCategory(String categoryId) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("tag_list",
                List.of(
                        ProcedureParameter.inputParam("prs_category_id", String.class, categoryId),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), TagListOut.class
        );

        return (List<TagListOut>) outputs.get("out_cur");
    }
}
