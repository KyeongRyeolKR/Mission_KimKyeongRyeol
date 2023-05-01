package com.ll.gramgram.boundedContext.instaMember.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInstaMemberSnapshot is a Querydsl query type for InstaMemberSnapshot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInstaMemberSnapshot extends EntityPathBase<InstaMemberSnapshot> {

    private static final long serialVersionUID = -1054594780L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInstaMemberSnapshot instaMemberSnapshot = new QInstaMemberSnapshot("instaMemberSnapshot");

    public final QInstaMemberBase _super = new QInstaMemberBase(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath eventTypeCode = createString("eventTypeCode");

    //inherited
    public final StringPath gender = _super.gender;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QInstaMember instaMember;

    //inherited
    public final NumberPath<Long> likes = _super.likes;

    //inherited
    public final NumberPath<Long> likesCountByAttractionTypeCode1 = _super.likesCountByAttractionTypeCode1;

    //inherited
    public final NumberPath<Long> likesCountByAttractionTypeCode2 = _super.likesCountByAttractionTypeCode2;

    //inherited
    public final NumberPath<Long> likesCountByAttractionTypeCode3 = _super.likesCountByAttractionTypeCode3;

    //inherited
    public final NumberPath<Long> likesCountByGenderMan = _super.likesCountByGenderMan;

    //inherited
    public final NumberPath<Long> likesCountByGenderManAndAttractiveTypeCode1 = _super.likesCountByGenderManAndAttractiveTypeCode1;

    //inherited
    public final NumberPath<Long> likesCountByGenderManAndAttractiveTypeCode2 = _super.likesCountByGenderManAndAttractiveTypeCode2;

    //inherited
    public final NumberPath<Long> likesCountByGenderManAndAttractiveTypeCode3 = _super.likesCountByGenderManAndAttractiveTypeCode3;

    //inherited
    public final NumberPath<Long> likesCountByGenderWoman = _super.likesCountByGenderWoman;

    //inherited
    public final NumberPath<Long> likesCountByGenderWomanAndAttractiveTypeCode1 = _super.likesCountByGenderWomanAndAttractiveTypeCode1;

    //inherited
    public final NumberPath<Long> likesCountByGenderWomanAndAttractiveTypeCode2 = _super.likesCountByGenderWomanAndAttractiveTypeCode2;

    //inherited
    public final NumberPath<Long> likesCountByGenderWomanAndAttractiveTypeCode3 = _super.likesCountByGenderWomanAndAttractiveTypeCode3;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath username = createString("username");

    public QInstaMemberSnapshot(String variable) {
        this(InstaMemberSnapshot.class, forVariable(variable), INITS);
    }

    public QInstaMemberSnapshot(Path<? extends InstaMemberSnapshot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInstaMemberSnapshot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInstaMemberSnapshot(PathMetadata metadata, PathInits inits) {
        this(InstaMemberSnapshot.class, metadata, inits);
    }

    public QInstaMemberSnapshot(Class<? extends InstaMemberSnapshot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.instaMember = inits.isInitialized("instaMember") ? new QInstaMember(forProperty("instaMember")) : null;
    }

}

