package com.ll.gramgram.boundedContext.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = -298148144L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotification notification = new QNotification("notification");

    public final com.ll.gramgram.base.baseEntity.QBaseEntity _super = new com.ll.gramgram.base.baseEntity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember fromInstaMember;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final NumberPath<Integer> newAttractiveTypeCode = createNumber("newAttractiveTypeCode", Integer.class);

    public final StringPath newGender = createString("newGender");

    public final NumberPath<Integer> oldAttractiveTypeCode = createNumber("oldAttractiveTypeCode", Integer.class);

    public final StringPath oldGender = createString("oldGender");

    public final DateTimePath<java.time.LocalDateTime> readDate = createDateTime("readDate", java.time.LocalDateTime.class);

    public final com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember toInstaMember;

    public final StringPath typeCode = createString("typeCode");

    public QNotification(String variable) {
        this(Notification.class, forVariable(variable), INITS);
    }

    public QNotification(Path<? extends Notification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotification(PathMetadata metadata, PathInits inits) {
        this(Notification.class, metadata, inits);
    }

    public QNotification(Class<? extends Notification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromInstaMember = inits.isInitialized("fromInstaMember") ? new com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember(forProperty("fromInstaMember")) : null;
        this.toInstaMember = inits.isInitialized("toInstaMember") ? new com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember(forProperty("toInstaMember")) : null;
    }

}

