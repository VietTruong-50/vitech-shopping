package vn.vnpt.api.dto.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

import java.util.regex.Pattern;


public interface DefaultMapper {

    Pattern pattern = Pattern.compile("\\s+");

    @Named("removeExtraSpaces")
    default String removeExtraSpaces(String str) {
        return StringUtils.isEmpty(str) ? str : pattern.matcher(str).replaceAll(StringUtils.SPACE).trim();
    }

}
