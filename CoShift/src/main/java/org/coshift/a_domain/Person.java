package org.coshift.a_domain;

public class Person {
    private final Long id; // community member number
    private String nickname;
    private String password;
    private long timeAccountId;

    public Person(Long id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public long getTimeAccountId() {
        return timeAccountId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setTimeAccountId(long timeAccountId) {
        this.timeAccountId = timeAccountId;
    }

}
