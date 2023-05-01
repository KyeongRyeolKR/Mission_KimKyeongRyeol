package com.ll.gramgram.boundedContext.instaMember.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInstaMemberBase is a Querydsl query type for InstaMemberBase
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QInstaMemberBase extends EntityPathBase<InstaMemberBase> {

    private static final long serialVersionUID = -574092751L;

    public static final QInstaMemberBase instaMemberBase = new QInstaMemberBase("instaMemberBase");

    public final com.ll.gramgram.base.baseEntity.QBaseEntity _super = new com.ll.gramgram.base.baseEntity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath gender = createString("gender");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> likes = createNumber("likes", Long.class);

    public final NumberPath<Long> likesCountByAttractionTypeCode1 = createNumber("likesCountByAttractionTypeCode1", Long.class);

    public final NumberPath<Long> likesCountByAttractionTypeCode2 = createNumber("likesCountByAttractionTypeCode2", Long.class);

    public final NumberPath<Long> likesCountByAttractionTypeCode3 = createNumber("likesCountByAttractionTypeCode3", Long.class);

    public final NumberPath<Long> likesCountByGenderMan = createNumber("likesCountByGenderMan", Long.class);

    public final NumberPath<Long> likesCountByGenderManAndAttractiveTypeCode1 = createNumber("likesCountByGenderManAndAttractiveTypeCode1", Long.class);

    public final NumberPath<Long> likesCountByGenderManAndAttractiveTypeCode2 = createNumber("likesCountByGenderManAndAttractiveTypeCode2", Long.class);

    public final NumberPath<Long> likesCountByGenderManAndAttractiveTypeCode3 = createNumber("likesCountByGenderManAndAttractiveTypeCode3", Long.class);

    public final NumberPath<Long> likesCountByGenderWoman = createNumber("likesCountByGenderWoman", Long.class);

    public final NumberPath<Long> likesCountByGenderWomanAndAttractiveTypeCode1 = createNumber("likesCountByGenderWomanAndAttractiveTypeCode1", Long.class);

    public final NumberPath<Long> likesCountByGenderWomanAndAttractiveTypeCode2 = createNumber("likesCountByGenderWomanAndAttractiveTypeCode2", Long.class);

    public final NumberPath<Long> likesCountByGenderWomanAndAttractiveTypeCode3 = createNumber("likesCountByGenderWomanAndAttractiveTypeCode3", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public QInstaMemberBase(String variable) {
        super(InstaMemberBase.class, forVariable(variable));
    }

    public QInstaMemberBase(Path<? extends InstaMemberBase> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInstaMemberBase(PathMetadata metadata) {
        super(InstaMemberBase.class, metadata);
    }

}

