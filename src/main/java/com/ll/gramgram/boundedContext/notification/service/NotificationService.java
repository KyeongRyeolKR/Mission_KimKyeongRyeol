package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> findByToInstaMember(InstaMember toInstaMember) {
        return notificationRepository.findByToInstaMemberOrderByIdDesc(toInstaMember);
    }

    // 호감 생성 알림 발생 메소드
    public void like(LikeablePerson likeablePerson) {
        Notification newNotification = Notification.builder()
                .toInstaMember(likeablePerson.getToInstaMember())
                .fromInstaMember(likeablePerson.getFromInstaMember())
                .oldAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .typeCode("Like")
                .build();

        notificationRepository.save(newNotification);
    }

    // 호감 사유 수정 알림 발생 메소드
    public void modify(LikeablePerson likeablePerson, int oldAttractiveTypeCode) {
        Notification newNotification = Notification.builder()
                .toInstaMember(likeablePerson.getToInstaMember())
                .fromInstaMember(likeablePerson.getFromInstaMember())
                .oldAttractiveTypeCode(oldAttractiveTypeCode)
                .newAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .typeCode("ModifyAttractiveType")
                .build();

        notificationRepository.save(newNotification);
    }

    // 해당 인스타 유저의 모든 알림들을 찾아 읽은 시간을 최신화한다.
    public void readAll(InstaMember instaMember) {
        List<Notification> notifications = findByToInstaMember(instaMember);
        for (Notification notification : notifications) {
            if(notification.getReadDate() == null) {
                notification.updateReadDate();
            }
        }
        notificationRepository.saveAll(notifications);
    }
}
