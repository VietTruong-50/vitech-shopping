package vn.vnpt.api.dto.out.recommend;

import lombok.Data;
import vn.vnpt.api.repository.helper.Col;

@Data
public class ClickData {
    @Col("event_detail")
    private String productId;
}
