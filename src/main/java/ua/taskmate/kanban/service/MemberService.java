package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.dto.MemberDto;
import ua.taskmate.kanban.dto.PopulatedMemberDto;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.exception.BadRequestException;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.MemberRepository;
import ua.taskmate.kanban.util.Util;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

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

    public Member getMemberByUserIdAndBoardId(String userId, Long boardId) {
        return memberRepository.findMemberByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found!"));
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

    public List<Member> getMembersByBoardId(Long boardId) {
        return memberRepository.findMembersByBoardId(boardId);
    }

    public Member getMemberByIdOrUserIdAndBoardIdFetchAll(Long memberId, Long boardId) {
        Member member;
        if (nonNull(memberId)) {
            member = getMemberById(memberId);
        } else if (nonNull(boardId)) {
            String userId = Util.getPrincipal().getUsername();
            member = getMemberByUserIdAndBoardId(userId, boardId);
        } else {
            throw new BadRequestException("memberId or boardId must be present!");
        }

        Hibernate.initialize(member.getBoard());
        Hibernate.initialize(member.getUser());
        Hibernate.initialize(member.getComments());
        Hibernate.initialize(member.getIssues());

        return member;
    }
}
