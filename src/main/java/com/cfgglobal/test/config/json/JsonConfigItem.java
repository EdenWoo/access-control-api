package com.cfgglobal.test.config.json;

import com.querydsl.core.types.Path;
import io.vavr.collection.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
class JsonConfigItem {

    Class<?> type;
    List<Path> include = List.empty();
    List<Path> exclude = List.empty();
}

