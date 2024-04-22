package vn.vnpt.api.dto.Enum;

public enum ProductStatusEnum {
    // Còn hàng
    ON_STOCK(1),
    // Hét hàng
    OUT_STOCK(2);

    public final int value;

    ProductStatusEnum(int value) {
        this.value = value;
    }
}