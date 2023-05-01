package com.ll.gramgram.boundedContext.instaMember.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInstaMember is a Querydsl query type for InstaMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInstaMember extends EntityPathBase<InstaMember> {

    private static final long serialVersionUID = -53334208L;

    public static final QInstaMember instaMember = new QInstaMember("instaMember");

    public final QInstaMemberBase _super = new QInstaMemberBase(this);

    public final StringPath accessToken = createString("accessToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final ListPath<com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson> fromLikeablePeople = this.<com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson>createList("fromLikeablePeople", com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson.class, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.class, PathInits.DIRECT2);

    //inherited
    public final StringPath gender = _super.gender;

    //inherited
    public final NumberPath<Long> id = _super.id;

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

    public final StringPath oauthId = createString("oauthId");

    public final ListPath<com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson> toLikeablePeople = this.<com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson>createList("toLikeablePeople", com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson.class, com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QInstaMember(String variable) {
        super(InstaMember.class, forVariable(variable));
    }

    public QInstaMember(Path<? extends InstaMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInstaMember(PathMetadata metadata) {
        super(InstaMember.class, metadata);
    }

}

