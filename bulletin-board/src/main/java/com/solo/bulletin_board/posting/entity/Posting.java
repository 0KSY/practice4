package com.solo.bulletin_board.posting.entity;

import com.solo.bulletin_board.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postingId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
