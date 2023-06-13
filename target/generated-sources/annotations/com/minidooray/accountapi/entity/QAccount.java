package com.minidooray.accountapi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = 769675103L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccount account = new QAccount("account");

    public final QAccountStatus accountStatus;

    public final QAdditionalInfo additionalInfo;

    public final StringPath id = createString("id");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public QAccount(String variable) {
        this(Account.class, forVariable(variable), INITS);
    }

    public QAccount(Path<? extends Account> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccount(PathMetadata metadata, PathInits inits) {
        this(Account.class, metadata, inits);
    }

    public QAccount(Class<? extends Account> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accountStatus = inits.isInitialized("accountStatus") ? new QAccountStatus(forProperty("accountStatus"), inits.get("accountStatus")) : null;
        this.additionalInfo = inits.isInitialized("additionalInfo") ? new QAdditionalInfo(forProperty("additionalInfo"), inits.get("additionalInfo")) : null;
    }

}

