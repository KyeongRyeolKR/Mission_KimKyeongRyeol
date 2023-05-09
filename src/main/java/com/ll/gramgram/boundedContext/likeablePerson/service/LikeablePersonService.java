package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.event.EventAfterLike;
import com.ll.gramgram.base.event.EventAfterModifyAttractiveType;
import com.ll.gramgram.base.event.EventBeforeCancelLike;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public RsData<LikeablePerson> like(Member actor, String username, int attractiveTypeCode) {
        RsData canLikeRsData = canLike(actor, username, attractiveTypeCode);

        if (canLikeRsData.isFail()) return canLikeRsData;

        if (canLikeRsData.getResultCode().equals("S-2")) return modifyAttractive(actor, username, attractiveTypeCode);

        InstaMember fromInstaMember = actor.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(actor.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .modifyUnlockDate(AppConfig.genLikeablePersonModifyUnlockDate())
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감표시 생겼어.
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어.
        toInstaMember.addToLikeablePerson(likeablePerson);

        publisher.publishEvent(new EventAfterLike(this, likeablePerson));

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    public Optional<LikeablePerson> findById(Long id) {
        return likeablePersonRepository.findById(id);
    }

    @Transactional
    public RsData cancel(LikeablePerson likeablePerson) {
        publisher.publishEvent(new EventBeforeCancelLike(this, likeablePerson));

        // 너가 생성한 좋아요가 사라졌어.
        likeablePerson.getFromInstaMember().removeFromLikeablePerson(likeablePerson);

        // 너가 받은 좋아요가 사라졌어.
        likeablePerson.getToInstaMember().removeToLikeablePerson(likeablePerson);

        likeablePersonRepository.delete(likeablePerson);

        String likeCanceledUsername = likeablePerson.getToInstaMember().getUsername();
        return RsData.of("S-1", "%s님에 대한 호감을 취소하였습니다.".formatted(likeCanceledUsername));
    }

    public RsData canCancel(Member actor, LikeablePerson likeablePerson) {
        if (likeablePerson == null) return RsData.of("F-1", "이미 취소되었습니다.");

        // 수행자의 인스타계정 번호
        long actorInstaMemberId = actor.getInstaMember().getId();
        // 삭제 대상의 작성자(호감표시한 사람)의 인스타계정 번호
        long fromInstaMemberId = likeablePerson.getFromInstaMember().getId();

        if (actorInstaMemberId != fromInstaMemberId)
            return RsData.of("F-2", "취소할 권한이 없습니다.");

        if (!likeablePerson.isModifyUnlocked())
            return RsData.of("F-3", "아직 취소할 수 없습니다. %s에는 가능합니다.".formatted(likeablePerson.getModifyUnlockDateRemainStrHuman()));

        return RsData.of("S-1", "취소가 가능합니다.");
    }

    private RsData canLike(Member actor, String username, int attractiveTypeCode) {
        if (!actor.hasConnectedInstaMember()) {
            return RsData.of("F-1", "먼저 본인의 인스타그램 아이디를 입력해주세요.");
        }

        InstaMember fromInstaMember = actor.getInstaMember();

        if (fromInstaMember.getUsername().equals(username)) {
            return RsData.of("F-2", "본인을 호감상대로 등록할 수 없습니다.");
        }

        // 액터가 생성한 `좋아요` 들 가져오기
        List<LikeablePerson> fromLikeablePeople = fromInstaMember.getFromLikeablePeople();

        // 그 중에서 좋아하는 상대가 username 인 녀석이 혹시 있는지 체크
        LikeablePerson fromLikeablePerson = fromLikeablePeople
                .stream()
                .filter(e -> e.getToInstaMember().getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (fromLikeablePerson != null && fromLikeablePerson.getAttractiveTypeCode() == attractiveTypeCode) {
            return RsData.of("F-3", "이미 %s님에 대해서 호감표시를 했습니다.".formatted(username));
        }

        long likeablePersonFromMax = AppConfig.getLikeablePersonFromMax();

        if (fromLikeablePerson != null) {
            return RsData.of("S-2", "%s님에 대해서 호감표시가 가능합니다.".formatted(username));
        }

        if (fromLikeablePeople.size() >= likeablePersonFromMax) {
            return RsData.of("F-4", "최대 %d명에 대해서만 호감표시가 가능합니다.".formatted(likeablePersonFromMax));
        }

        return RsData.of("S-1", "%s님에 대해서 호감표시가 가능합니다.".formatted(username));
    }

    public Optional<LikeablePerson> findByFromInstaMember_usernameAndToInstaMember_username(String fromInstaMemberUsername, String toInstaMemberUsername) {
        return likeablePersonRepository.findByFromInstaMember_usernameAndToInstaMember_username(fromInstaMemberUsername, toInstaMemberUsername);
    }

    @Transactional
    public RsData<LikeablePerson> modifyAttractive(Member actor, Long id, int attractiveTypeCode) {
        Optional<LikeablePerson> likeablePersonOptional = findById(id);

        if (likeablePersonOptional.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 호감표시입니다.");
        }

        LikeablePerson likeablePerson = likeablePersonOptional.get();

        return modifyAttractive(actor, likeablePerson, attractiveTypeCode);
    }

    @Transactional
    public RsData<LikeablePerson> modifyAttractive(Member actor, LikeablePerson likeablePerson, int attractiveTypeCode) {
        RsData canModifyRsData = canModify(actor, likeablePerson);

        if (canModifyRsData.isFail()) {
            return canModifyRsData;
        }

        String oldAttractiveTypeDisplayName = likeablePerson.getAttractiveTypeDisplayName();
        String username = likeablePerson.getToInstaMember().getUsername();

        modifyAttractionTypeCode(likeablePerson, attractiveTypeCode);

        String newAttractiveTypeDisplayName = likeablePerson.getAttractiveTypeDisplayName();

        return RsData.of("S-3", "%s님에 대한 호감사유를 %s에서 %s(으)로 변경합니다.".formatted(username, oldAttractiveTypeDisplayName, newAttractiveTypeDisplayName), likeablePerson);
    }

    @Transactional
    public RsData<LikeablePerson> modifyAttractive(Member actor, String username, int attractiveTypeCode) {
        // 액터가 생성한 `좋아요` 들 가져오기
        List<LikeablePerson> fromLikeablePeople = actor.getInstaMember().getFromLikeablePeople();

        LikeablePerson fromLikeablePerson = fromLikeablePeople
                .stream()
                .filter(e -> e.getToInstaMember().getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (fromLikeablePerson == null) {
            return RsData.of("F-7", "호감표시를 하지 않았습니다.");
        }

        return modifyAttractive(actor, fromLikeablePerson, attractiveTypeCode);
    }

    private void modifyAttractionTypeCode(LikeablePerson likeablePerson, int attractiveTypeCode) {
        int oldAttractiveTypeCode = likeablePerson.getAttractiveTypeCode();
        RsData rsData = likeablePerson.updateAttractionTypeCode(attractiveTypeCode);

        if (rsData.isSuccess()) {
            publisher.publishEvent(new EventAfterModifyAttractiveType(this, likeablePerson, oldAttractiveTypeCode, attractiveTypeCode));
        }
    }

    public RsData canModify(Member actor, LikeablePerson likeablePerson) {
        if (!actor.hasConnectedInstaMember()) {
            return RsData.of("F-1", "먼저 본인의 인스타그램 아이디를 입력해주세요.");
        }

        InstaMember fromInstaMember = actor.getInstaMember();

        if (!Objects.equals(likeablePerson.getFromInstaMember().getId(), fromInstaMember.getId())) {
            return RsData.of("F-2", "해당 호감표시에 대해서 사유변경을 수행할 권한이 없습니다.");
        }

        if (!likeablePerson.isModifyUnlocked())
            return RsData.of("F-3", "아직 호감사유변경을 할 수 없습니다. %s에는 가능합니다.".formatted(likeablePerson.getModifyUnlockDateRemainStrHuman()));


        return RsData.of("S-1", "호감사유변경이 가능합니다.");
    }

    // 매개변수를 기준으로 목록화 해주는 메소드
    public List<LikeablePerson> listing(InstaMember instaMember, String gender, int attractiveTypeCode, int sortCode) {
        List<LikeablePerson> likeablePeople = instaMember.getToLikeablePeople();

        // gender 값이 "M" 또는 "W" 일 경우
        if(gender.equals("M") || gender.equals("W")) {
            likeablePeople = filteringBy(gender, likeablePeople);
        }

        // attractiveTypeCode 값이 1 ~ 3 사이일 경우
        if(attractiveTypeCode > 0 && attractiveTypeCode < 4) {
            likeablePeople = filteringBy(attractiveTypeCode, likeablePeople);
        }

        // sortCode 기준으로 정렬 후 반환
        return likeablePeople.stream()
                .sorted(compareTo(sortCode))
                .collect(Collectors.toList());
    }

    // attractiveTypeCode 를 기준으로 필터링 해주는 메소드
    private List<LikeablePerson> filteringBy(int attractiveTypeCode, List<LikeablePerson> likeablePeople) {
        return likeablePeople.stream()
                .filter(e -> e.getAttractiveTypeCode() == attractiveTypeCode)
                .collect(Collectors.toList());
    }

    // gender 를 기준으로 필터링 해주는 메소드
    private List<LikeablePerson> filteringBy(String gender, List<LikeablePerson> likeablePeople) {
        return likeablePeople.stream()
                .filter(e -> e.getFromInstaMember().getGender().equals(gender))
                .collect(Collectors.toList());
    }

    // attractiveTypeCode 를 기준으로 그에 맞는 Comparator 객체를 반환해주는 메소드
    private Comparator<LikeablePerson> compareTo(int typeCode) {
        return switch (typeCode) {
            // 최신순 : 날짜순(오래된순)의 반대
            case 1 -> compareTo(2).reversed();
            // 날짜순 : 생성일자가 작을 수록 오래된 순 -> createDate 오름차순
            case 2 -> Comparator.comparing(LikeablePerson::getCreateDate);
            // 인기 많은 순 : 인기 적은 순의 반대
            case 3 -> compareTo(4).reversed();
            // 인기 적은 순 : 각 호감표시 주인의 [내가 받은 호감리스트]의 크기를 기준으로 오름차순
            case 4 -> Comparator.comparingInt(o -> o.getFromInstaMember().getToLikeablePeople().size());
            // 성별순 : 각 호감표시 주인의 성별을 내림차순으로 정렬하고, 같은 성별일 경우엔 최신순(compareTo(1))으로 한번 더 정렬
            // 내림차순으로 정렬하는 이유 -> 문자열을 오름차순으로 정렬하면 사전순 정렬이기에 남성("M")이 먼저 나오게 되므로, 내림차순으로 정렬
            case 5 -> Comparator.comparing((LikeablePerson o) -> o.getFromInstaMember().getGender()).reversed().thenComparing(compareTo(1));
            // 호감사유순 : attractiveTypeCode 오름차순으로 정렬하고, 같은 사유일 경우엔 최신순(compareTo(1))으로 한번 더 정렬
            case 6 -> Comparator.comparingInt(LikeablePerson::getAttractiveTypeCode).thenComparing(compareTo(1));
            // 잘못된 인자를 넣을 경우 -> 최신순
            // case 1 을 삭제하고 default 에 compare(2).reversed(); 를 넣어줘도 되지만, 명시적으로 보여주기 위해 하지 않음
            default -> compareTo(1);
        };
    }
}
