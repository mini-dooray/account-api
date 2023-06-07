package com.minidooray.accountapi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountStatus is a Querydsl query type for AccountStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountStatus extends EntityPathBase<AccountStatus> {

    private static final long serialVersionUID = -2016688079L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccountStatus accountStatus = new QAccountStatus("accountStatus");

    public final DatePath<java.time.LocalDate> accessDate = createDate("accessDate", java.time.LocalDate.class);

    public final QAccount account;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QAccountStatus(String variable) {
        this(AccountStatus.class, forVariable(variable), INITS);
    }

    public QAccountStatus(Path<? extends AccountStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccountStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccountStatus(PathMetadata metadata, PathInits inits) {
        this(AccountStatus.class, metadata, inits);
    }

    public QAccountStatus(Class<? extends AccountStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account"), inits.get("account")) : null;
    }

}

