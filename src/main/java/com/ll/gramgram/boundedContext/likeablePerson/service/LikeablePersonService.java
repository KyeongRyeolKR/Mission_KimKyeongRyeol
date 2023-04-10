package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if ( member.hasConnectedInstaMember() == false ) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        for(LikeablePerson likeablePerson : fromInstaMember.getFromLikeablePeople()) {
            if(isSameUsername(likeablePerson, username) && isSameTypeCode(likeablePerson, attractiveTypeCode)) {
                return RsData.of("F-3", "해당 유저는 이미 등록된 상대입니다.");
            }

            if(isSameUsername(likeablePerson, username) && !isSameTypeCode(likeablePerson, attractiveTypeCode)) {
                String beforeType = likeablePerson.getAttractiveTypeDisplayName();

                likeablePerson.setAttractiveTypeCode(attractiveTypeCode);

                String afterType = likeablePerson.getAttractiveTypeDisplayName();

                return RsData.of("S-2", "%s에 대한 호감사유를 %s에서 %s(으)로 변경합니다.".formatted(username, beforeType, afterType));
            }
        }

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감표시 생성
        member.getInstaMember().addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생성
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    public LikeablePerson findById(Long likeablePersonId) {
        return likeablePersonRepository.findById(likeablePersonId).orElse(null);
    }

    @Transactional
    public RsData<LikeablePerson> deleteById(Member loginMember, Long likeablePersonId) {
        Optional<LikeablePerson> oLikeablePerson = likeablePersonRepository.findById(likeablePersonId);

        if(oLikeablePerson.isEmpty()) {
            return RsData.of("F-2", "없는 데이터입니다.");
        }

        if(!canDelete(loginMember, oLikeablePerson.get())) {
            return RsData.of("F-1", "해당 호감 데이터는 당신의 것이 아닙니다.", oLikeablePerson.get());
        }

        likeablePersonRepository.deleteById(likeablePersonId);

        // 삭제된 Username
        String deletedUsername = oLikeablePerson.get().getToInstaMember().getUsername();

        return RsData.of("S-1", "호감 상대(%s)가 삭제되었습니다.".formatted(deletedUsername));
    }

    // member 가 likeablePerson 을 삭제할 권한이 있는지 체크
    public boolean canDelete(Member member, LikeablePerson likeablePerson) {
        return Objects.equals(member.getInstaMember().getId(), likeablePerson.getFromInstaMember().getId());
    }

    public boolean isSameUsername(LikeablePerson likeablePerson, String username) {
        return likeablePerson.getToInstaMember().getUsername().equals(username);
    }

    public boolean isSameTypeCode(LikeablePerson likeablePerson, int attractiveTypeCode) {
        return likeablePerson.getAttractiveTypeCode() == attractiveTypeCode;
    }
}
