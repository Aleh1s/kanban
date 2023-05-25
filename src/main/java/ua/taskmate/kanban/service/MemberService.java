package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Member with userId %d does not exist", id)));
    }

    public Member getMemberByUserId(String userId) {
        return memberRepository.findMemberByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Member with userId %s does not exist", userId)));
    }

    public Member getPopulatedMemberById(Long id) {
        Member member = getMemberById(id);
        return null;
    }

    @Transactional
    public void save(Member member) {
        member.setCreatedAt(LocalDateTime.now());
        memberRepository.save(member);
    }

    public List<Member> getMembersByUserIdFetchBoard(String userId) {
        return memberRepository.findMembersByUserIdFetchBoard(userId);
    }

    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }
}
