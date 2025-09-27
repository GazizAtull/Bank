package com.gaziz.bank.infrastructure.internal.api.presenter;

import com.generated.swaggerCodegen.model.BasicBackendResponse;
import com.generated.swaggerCodegen.model.MetaData;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public class BasePresenter {
    protected void decorate(final BasicBackendResponse basic) {
        MetaData metaData = new MetaData();
        metaData.setTimestamp(Instant.now().toString());
        basic.setMeta(metaData);
        basic.setDescription("Запрос выполнен успешно");
    }

    protected void decorateWithPageable(final BasicBackendResponse basic, final Pageable pageable, final Long total) {
        MetaData metaData = new MetaData();
        metaData.setTimestamp(Instant.now().toString());
        metaData.setPage(pageable.isUnpaged() ? 0 : pageable.getPageNumber());
        metaData.setPageSize(pageable.isUnpaged() ? 0 : pageable.getPageSize());
        metaData.setTotalItems(total);
        basic.setMeta(metaData);
    }
}
