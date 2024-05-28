package vn.hust.api.repository.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.out.user.RoleOut;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;

import java.util.ArrayList;
import java.util.List;

import static vn.hust.common.constant.ConstantString.Success;


@Repository
public class RoleRepo {
    @Autowired
    ProcedureCallerV3 procedureCallerV3;

    public List<RoleOut> check(String userId, String path) {
        var outputs = procedureCallerV3.callOneRefCursor("check_role",
                List.of(
                        ProcedureParameter.inputParam("prs_user_id", String.class, userId),
                        ProcedureParameter.inputParam("prs_api", String.class, path),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                RoleOut.class);
        var outResult = outputs.get("out_result").toString();
        if (!outResult.equals(Success)) {
            return new ArrayList<>();
        }
        var result = (List<RoleOut>) outputs.get("out_cur");
        return result == null ? new ArrayList<>() : result;
    }
}
