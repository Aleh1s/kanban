package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.dto.MemberDto;
import ua.taskmate.kanban.dto.PopulatedMemberDto;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    public List<Member> getMembersByUserIdFetchBoard(String userId) {
        return memberRepository.findMembersByUserIdFetchBoard(userId);
    }

    public Optional<Member> findMemberByUserIdAndBoardId(String userId, Long boardId) {
        return memberRepository.findMemberByUserIdAndBoardId(userId, boardId);
    }

    public boolean existsMemberByUserIdAndBoardId(String userId, Long boardId) {
        return memberRepository.existsMemberByUserIdAndBoardId(userId, boardId);
    }

    public List<PopulatedMemberDto> populateMembers(List<Member> members) {
        List<Long> memberIds = members.stream()
                .map(Member::getId)
                .toList();
        members = memberRepository.findMembersFetchUserByMemberIds(memberIds);
        return members.stream()
                .map(member -> PopulatedMemberDto.builder()
                        .member(MemberDto.builder()
                                .id(member.getId())
                                .createdAt(member.getCreatedAt())
                                .updatedAt(member.getUpdatedAt())
                                .role(member.getRole())
                                .build())
                        .userId(member.getUser().getId())
                        .email(member.getUser().getEmail())
                        .profileImageUrl(member.getUser().getProfileImageUrl())
                        .firstName(member.getUser().getFistName())
                        .lastName(member.getUser().getLastName())
                        .build())
                .toList();
    }
}
