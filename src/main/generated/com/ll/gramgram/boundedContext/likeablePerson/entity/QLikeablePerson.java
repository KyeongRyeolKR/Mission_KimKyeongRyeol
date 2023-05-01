package com.ll.gramgram.boundedContext.likeablePerson.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikeablePerson is a Querydsl query type for LikeablePerson
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeablePerson extends EntityPathBase<LikeablePerson> {

    private static final long serialVersionUID = 1545229958L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikeablePerson likeablePerson = new QLikeablePerson("likeablePerson");

    public final com.ll.gramgram.base.baseEntity.QBaseEntity _super = new com.ll.gramgram.base.baseEntity.QBaseEntity(this);

    public final NumberPath<Integer> attractiveTypeCode = createNumber("attractiveTypeCode", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember fromInstaMember;

    public final StringPath fromInstaMemberUsername = createString("fromInstaMemberUsername");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final DateTimePath<java.time.LocalDateTime> modifyUnlockDate = createDateTime("modifyUnlockDate", java.time.LocalDateTime.class);

    public final com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember toInstaMember;

    public final StringPath toInstaMemberUsername = createString("toInstaMemberUsername");

    public QLikeablePerson(String variable) {
        this(LikeablePerson.class, forVariable(variable), INITS);
    }

    public QLikeablePerson(Path<? extends LikeablePerson> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikeablePerson(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikeablePerson(PathMetadata metadata, PathInits inits) {
        this(LikeablePerson.class, metadata, inits);
    }

    public QLikeablePerson(Class<? extends LikeablePerson> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromInstaMember = inits.isInitialized("fromInstaMember") ? new com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember(forProperty("fromInstaMember")) : null;
        this.toInstaMember = inits.isInitialized("toInstaMember") ? new com.ll.gramgram.boundedContext.instaMember.entity.QInstaMember(forProperty("toInstaMember")) : null;
    }

}

