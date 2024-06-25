package vn.hust.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.hust.api.dto.in.product.ProductFilterIn;
import vn.hust.api.dto.out.product.ProductAttributeOut;
import vn.hust.api.dto.out.product.ProductDetailOut;
import vn.hust.api.dto.out.product.ProductListOut;
import vn.hust.api.dto.out.recommend.RecommendProducts;
import vn.hust.api.repository.helper.ProcedureCallerV3;
import vn.hust.api.repository.helper.ProcedureParameter;
import vn.hust.common.Common;
import vn.hust.common.exception.NotFoundException;
import vn.hust.common.model.PagingOut;
import vn.hust.common.model.SortPageIn;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ProductRepository {
    private final ProcedureCallerV3 procedureCallerV3;

    public ProductDetailOut getProductDetail(String productId) {
        var outputs = procedureCallerV3.callOneRefCursor("product_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                ProductDetailOut.class
        );
        List<ProductDetailOut> outList = (List<ProductDetailOut>) outputs.get("out_cur");
        if (outList == null || outList.isEmpty()) {
            throw new NotFoundException("Product not found!");
        }

        return outList.get(0);
    }

    public List<ProductAttributeOut> getProductAttribute(String productId) {
        var outputs = procedureCallerV3.callOneRefCursor("product_attribute_detail",
                List.of(
                        ProcedureParameter.inputParam("prs_product_id", String.class, productId),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ),
                ProductAttributeOut.class
        );

        return (List<ProductAttributeOut>) outputs.get("out_cur");
    }

    public PagingOut<ProductListOut> getAllProducts(SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("product_list_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ProductListOut.class
        );

        List<ProductListOut> outList = (List<ProductListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public PagingOut<ProductListOut> getAllProductByCategory(String categoryId, SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("product_category_list",
                List.of(
                        ProcedureParameter.inputParam("prs_category_id", String.class, categoryId),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ProductListOut.class
        );

        List<ProductListOut> outList = (List<ProductListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public PagingOut<ProductListOut> getAllProductBySubCategory(String subCategoryId, SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("product_subcategory_list",
                List.of(
                        ProcedureParameter.inputParam("prs_subcategory_id", String.class, subCategoryId),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ProductListOut.class
        );

        List<ProductListOut> outList = (List<ProductListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public PagingOut<ProductListOut> dynamicFilter(ProductFilterIn productFilterIn, SortPageIn sortPageIn) {
        Map<String, Object> outputs = procedureCallerV3.callOneRefCursor("product_dynamic_filter",
                List.of(
                        ProcedureParameter.inputParam("prs_subcategory_ids", String.class, productFilterIn.getSubCategoriesList()),
                        ProcedureParameter.inputParam("prs_tag_ids", String.class, productFilterIn.getTagsList()),
                        ProcedureParameter.inputParam("prs_min_price", Long.class, productFilterIn.getMinPrice()),
                        ProcedureParameter.inputParam("prs_max_price", Long.class, productFilterIn.getMaxPrice()),
                        ProcedureParameter.inputParam("prs_status", Integer.class, !Common.isNullOrEmpty(productFilterIn.getStatusEnum()) ? productFilterIn.getStatusEnum().value : null),
                        ProcedureParameter.inputParam("prs_category_id", String.class, productFilterIn.getCategoryId()),
                        ProcedureParameter.inputParam("prs_properties_sort", String.class, sortPageIn.getPropertiesSort()),
                        ProcedureParameter.inputParam("prs_sort", String.class, sortPageIn.getSort()),
                        ProcedureParameter.inputParam("prn_page_index", Integer.class, sortPageIn.getPage()),
                        ProcedureParameter.inputParam("prn_page_size", Integer.class, sortPageIn.getMaxSize()),
                        ProcedureParameter.inputParam("prs_key_search", String.class, sortPageIn.getKeySearch()),
                        ProcedureParameter.outputParam("out_total", Long.class),
                        ProcedureParameter.outputParam("out_result", String.class),
                        ProcedureParameter.refCursorParam("out_cur")
                ), ProductListOut.class
        );
        List<ProductListOut> outList = (List<ProductListOut>) outputs.get("out_cur");

        return PagingOut.of((Number) outputs.get("out_total"), sortPageIn, outList);
    }

    public List<RecommendProducts> getTopSellerProducts(int limit) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Determine the start and end dates of the current quarter
        int month = currentDate.getMonthValue();
        LocalDate startDate;
        LocalDate endDate;

        if (month <= 3) {
            startDate = LocalDate.of(currentDate.getYear(), 1, 1);
            endDate = LocalDate.of(currentDate.getYear(), 3, 31);
        } else if (month <= 6) {
            startDate = LocalDate.of(currentDate.getYear(), 4, 1);
            endDate = LocalDate.of(currentDate.getYear(), 6, 30);
        } else if (month <= 9) {
            startDate = LocalDate.of(currentDate.getYear(), 7, 1);
            endDate = LocalDate.of(currentDate.getYear(), 9, 30);
        } else {
            startDate = LocalDate.of(currentDate.getYear(), 10, 1);
            endDate = LocalDate.of(currentDate.getYear(), 12, 31);
        }

        // Format dates as strings
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        // Call the stored procedure with the quarter dates
        var outputs = procedureCallerV3.callOneRefCursor("get_product_reports", List.of(
                ProcedureParameter.inputParam("prs_create_date_from", String.class, !Common.isNullOrEmpty(startDate) ? startDateStr : null),
                ProcedureParameter.inputParam("prs_create_date_to", String.class, !Common.isNullOrEmpty(endDate) ? endDateStr : null),
                ProcedureParameter.inputParam("prs_limit", Integer.class, limit),
                ProcedureParameter.inputParam("prs_type", Integer.class, 1),
                ProcedureParameter.refCursorParam("out_cur")
        ), RecommendProducts.class);

        // Return the result
        return (List<RecommendProducts>) outputs.get("out_cur");
    }

    public List<RecommendProducts> getRecentView(int limit, String id) {
        var outputs = procedureCallerV3.callOneRefCursor("get_recent_view", List.of(
                ProcedureParameter.inputParam("prs_limit", Integer.class, limit),
                ProcedureParameter.inputParam("prs_user_id", String.class, id),
                ProcedureParameter.refCursorParam("out_cur")
        ), RecommendProducts.class);
        return (List<RecommendProducts>) outputs.get("out_cur");
    }
}
