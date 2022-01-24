package com.ssafy.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.core.code.JoinCode;
import com.ssafy.core.code.YNCode;
import com.ssafy.core.converter.JoinCodeConverter;
import com.ssafy.core.converter.YNCodeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user",
    uniqueConstraints = {
            @UniqueConstraint(
                    columnNames={"uid"}
            )
    }
)
// 회원 테이블
public class User extends BaseEntity implements UserDetails {

    // User 테이블의 키값 = 회원의 고유 키값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 회원이 가입한 타입 (none:일반, sns:소셜회원가입)
    @Convert(converter = JoinCodeConverter.class)
    @Column(nullable = false, length = 5)
    private JoinCode joinType;

    // 회원아이디(일반:SSAID, 소셜회원가입:발급번호)
    @Column(nullable = false, unique = true, length = 120)
    private String uid;

    // 비밀번호
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 255)
    private String password;


    // 회원 닉네임
    @Column(nullable = false, length = 64)
    private String nickname;

    // 회원 프로필 이미지
    @Column(nullable = false, length = 255)
    private String img;

    // 음소거 설정 (Y:on, N:off)
    @Convert(converter = YNCodeConverter.class)
    @Column(nullable = false, length = 1, columnDefinition = "varchar(1) default 'N'")
    private YNCode mute;

    // 게임 진행 상태 (Y:진행, N:대기)
    @Convert(converter = YNCodeConverter.class)
    @Column(nullable = false, length = 1, columnDefinition = "varchar(1) default 'N'")
    private YNCode state;

    // 장비 푸시용 토큰
    @Column(length = 255)
    private String token;

    // 사용 여부
    @Convert(converter = YNCodeConverter.class)
    @Column(nullable = false, length = 1, columnDefinition = "varchar(1) default 'Y'")
    private YNCode isBind;





    // =================================================================================================
    // JWT
    // =================================================================================================
    @Column(length = 100)
    private String provider;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.uid;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // =================================================================================================



    public void updateImg(String img){
        this.img = img;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }


    public void updateIsBind(YNCode isBind){
        this.isBind = isBind;
    }

    public void updatePush(YNCode mute){
        this.mute = mute;
    }

    public void updateState(YNCode push){
        this.state = state;
    }


    public void updateToken(String token){
        this.token = token;
    }

}
