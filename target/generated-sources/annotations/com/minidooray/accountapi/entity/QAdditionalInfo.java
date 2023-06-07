package com.minidooray.accountapi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdditionalInfo is a Querydsl query type for AdditionalInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdditionalInfo extends EntityPathBase<AdditionalInfo> {

    private static final long serialVersionUID = 2112078851L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdditionalInfo additionalInfo = new QAdditionalInfo("additionalInfo");

    public final QAccount account;

    public final StringPath email = createString("email");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public QAdditionalInfo(String variable) {
        this(AdditionalInfo.class, forVariable(variable), INITS);
    }

    public QAdditionalInfo(Path<? extends AdditionalInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdditionalInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdditionalInfo(PathMetadata metadata, PathInits inits) {
        this(AdditionalInfo.class, metadata, inits);
    }

    public QAdditionalInfo(Class<? extends AdditionalInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account"), inits.get("account")) : null;
    }

}

