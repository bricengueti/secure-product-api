package com.TNBtech.secure_product_api.entities;

import jakarta.persistence.*;

@Entity
public class Jwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;


    private boolean revoked;

    private boolean expired;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Jwt(Long id, String token, boolean revoked, boolean expired, RefreshToken refreshToken, User user) {
        this.id = id;
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public Jwt() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
